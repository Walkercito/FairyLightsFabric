package de.lucalabs.fairylights.connection;

import de.lucalabs.fairylights.blocks.FairyLightBlocks;
import de.lucalabs.fairylights.fastener.Fastener;
import de.lucalabs.fairylights.feature.FeatureType;
import de.lucalabs.fairylights.feature.light.Light;
import de.lucalabs.fairylights.feature.light.LightBehavior;
import de.lucalabs.fairylights.items.HangingLightsConnectionItem;
import de.lucalabs.fairylights.items.LightVariant;
import de.lucalabs.fairylights.items.SimpleLightVariant;
import de.lucalabs.fairylights.sounds.FairyLightSounds;
import de.lucalabs.fairylights.string.StringType;
import de.lucalabs.fairylights.string.StringTypes;
import de.lucalabs.fairylights.util.ItemHelper;
import de.lucalabs.fairylights.util.Tags;
import java.util.*;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LightBlock;
import net.minecraft.world.phys.Vec3;

public final class HangingLightsConnection extends HangingFeatureConnection<Light<?>> {
    private static final int MAX_LIGHT = 15;
    private static final int LIGHT_UPDATE_WAIT = 400;
    private static final int LIGHT_UPDATE_RATE = 10;

    private final Set<BlockPos> litBlocks = new HashSet<>();
    private final Set<BlockPos> oldLitBlocks = new HashSet<>();

    private StringType string;
    private List<ItemStack> pattern;
    private boolean isOn = true;
    private int lightUpdateTime = (int) (Math.random() * LIGHT_UPDATE_WAIT / 2);

    private int lightUpdateIndex;

    public HangingLightsConnection(
            final ConnectionType<? extends HangingLightsConnection> type,
            final Level world,
            final Fastener<?> fastenerOrigin,
            final UUID uuid) {

        super(type, world, fastenerOrigin, uuid);
        this.string = StringTypes.BLACK_STRING;
        this.pattern = new ArrayList<>();
    }

    public StringType getString() {
        return this.string;
    }

    @Override
    public boolean interact(
            final Player player,
            final Vec3 hit,
            final FeatureType featureType,
            final int feature,
            final ItemStack heldStack,
            final InteractionHand hand) {

        if (featureType == FEATURE && heldStack.is(Tags.LIGHTS)) {
            final int index = feature % this.pattern.size();
            final ItemStack light = this.pattern.get(index);
            if (!ItemStack.matches(light, heldStack)) {
                final ItemStack placed = heldStack.split(1);
                this.pattern.set(index, placed);
                ItemHelper.giveItemToPlayer(player, light);
                this.computeCatenary();
                this.world.playSound(
                        null,
                        hit.x,
                        hit.y,
                        hit.z,
                        FairyLightSounds.FEATURE_COLOR_CHANGE,
                        SoundSource.BLOCKS,
                        1,
                        1);
                return true;
            }
        }

        if (super.interact(player, hit, featureType, feature, heldStack, hand)) {
            return true;
        }

        this.isOn = !this.isOn;
        final SoundEvent lightSnd;
        final float pitch;

        if (this.isOn) {
            lightSnd = FairyLightSounds.FEATURE_LIGHT_TURNON;
            pitch = 0.6F;
        } else {
            lightSnd = FairyLightSounds.FEATURE_LIGHT_TURNOFF;
            pitch = 0.5F;
        }

        this.world.playSound(null, hit.x, hit.y, hit.z, lightSnd, SoundSource.BLOCKS, 1, pitch);
        this.computeCatenary();
        return true;
    }

    @Override
    public void onUpdate() {
        final boolean on = !this.isDynamic() && this.isOn;
        for (final Light<?> light : this.features) {
            light.tick(this.world, this.fastener.getConnectionPoint());
        }
        if (on && this.features.length > 0) {
            this.lightUpdateTime++;
            if (this.lightUpdateTime > LIGHT_UPDATE_WAIT && this.lightUpdateTime % LIGHT_UPDATE_RATE == 0) {
                if (this.lightUpdateIndex >= this.features.length) {
                    this.lightUpdateIndex = 0;
                    this.lightUpdateTime = this.world.random.nextInt(LIGHT_UPDATE_WAIT / 2);
                } else {
                    this.setLight(BlockPos.containing(this.features[this.lightUpdateIndex++].getAbsolutePoint(this.fastener)));
                }
            }
        }
    }

    private void updateNeighbors(final Fastener<?> fastener) {
        this.world.updateNeighbourForOutputSignal(fastener.getPos(), FairyLightBlocks.FASTENER);
    }

    @Override
    protected Light<?>[] createFeatures(final int length) {
        return new Light<?>[length];
    }

    @Override
    protected boolean canReuse(final Light<?> feature, final int index) {
        return ItemStack.matches(feature.getItem(), this.getPatternStack(index));
    }

    @Override
    protected Light<?> createFeature(final int index, final Vec3 point, final float yaw, final float pitch) {
        final ItemStack lightData = this.getPatternStack(index);
        return this.createLight(index, point, yaw, pitch, lightData, LightVariant.get(lightData).orElse(SimpleLightVariant.FAIRY_LIGHT));
    }

    private ItemStack getPatternStack(final int index) {
        return this.pattern.isEmpty() ? ItemStack.EMPTY : this.pattern.get(index % this.pattern.size());
    }

    @Override
    protected void updateFeature(final Light<?> light) {
        super.updateFeature(light);
        if (!this.isDynamic() && this.isOn) {
            final BlockPos pos = BlockPos.containing(light.getAbsolutePoint(this.fastener));
            this.litBlocks.add(pos);
            this.setLight(pos);
        }
    }

    private <T extends LightBehavior> Light<T> createLight(final int index, final Vec3 point, final float yaw, final float pitch, final ItemStack stack, final LightVariant<T> variant) {
        return new Light<>(index, point, yaw, pitch, stack, variant, 0.125F);
    }

    @Override
    protected float getFeatureSpacing() {
        if (this.pattern.isEmpty()) {
            return SimpleLightVariant.FAIRY_LIGHT.getSpacing();
        }
        float spacing = 0;
        for (final ItemStack patternLightData : this.pattern) {
            final float lightSpacing = LightVariant.get(patternLightData).orElse(SimpleLightVariant.FAIRY_LIGHT).getSpacing();
            if (lightSpacing > spacing) {
                spacing = lightSpacing;
            }
        }
        return spacing;
    }

    @Override
    protected void onBeforeUpdateFeatures() {
        this.oldLitBlocks.clear();
        this.oldLitBlocks.addAll(this.litBlocks);
        this.litBlocks.clear();
    }

    @Override
    protected void onAfterUpdateFeatures() {
        final boolean on = !this.isDynamic() && this.isOn;
        for (final Light<?> light : this.features) {
            light.power(on, this.isDynamic() || this.prevCatenary == null);
        }
        this.oldLitBlocks.removeAll(this.litBlocks);
        final Iterator<BlockPos> oldIter = this.oldLitBlocks.iterator();
        while (oldIter.hasNext()) {
            this.removeLight(oldIter.next());
            oldIter.remove();
        }
    }

    @Override
    public void onRemove() {
        for (final BlockPos pos : this.litBlocks) {
            this.removeLight(pos);
        }
    }

    private void removeLight(final BlockPos pos) {
        if (this.world.getBlockState(pos).is(Blocks.LIGHT)) {
            this.world.removeBlock(pos, false);
        }
    }

    private void setLight(final BlockPos pos) {
        if (this.world.isLoaded(pos) && this.world.isEmptyBlock(pos) && this.world.getBrightness(LightLayer.BLOCK, pos) < MAX_LIGHT) {
            this.world.setBlock(pos, Blocks.LIGHT.defaultBlockState().setValue(LightBlock.LEVEL, 15), Block.UPDATE_CLIENTS);
        }
    }

    @Override
    public CompoundTag serialize() {
        final CompoundTag compound = super.serialize();
        compound.putBoolean("isOn", this.isOn);
        final ListTag litBlocks = new ListTag();
        for (final BlockPos litBlock : this.litBlocks) {
            litBlocks.add(NbtUtils.writeBlockPos(litBlock));
        }
        compound.put("litBlocks", litBlocks);
        return compound;
    }

    @Override
    public void deserialize(final CompoundTag compound) {
        super.deserialize(compound);
        this.isOn = compound.getBoolean("isOn");
        this.litBlocks.clear();
        final ListTag litBlocks = compound.getList("litBlocks", Tag.TAG_COMPOUND);
        for (int i = 0; i < litBlocks.size(); i++) {
            this.litBlocks.add(NbtUtils.readBlockPos(litBlocks.getCompound(i)));
        }
    }

    @Override
    public CompoundTag serializeLogic() {
        final CompoundTag compound = super.serializeLogic();
        HangingLightsConnectionItem.setString(compound, this.string);
        final ListTag tagList = new ListTag();
        for (final ItemStack light : this.pattern) {
            tagList.add(light.save(new CompoundTag()));
        }
        compound.put("pattern", tagList);
        return compound;
    }

    @Override
    public void deserializeLogic(final CompoundTag compound) {
        super.deserializeLogic(compound);
        this.string = HangingLightsConnectionItem.getString(compound);
        final ListTag patternList = compound.getList("pattern", Tag.TAG_COMPOUND);
        this.pattern = new ArrayList<>();
        for (int i = 0; i < patternList.size(); i++) {
            final CompoundTag lightCompound = patternList.getCompound(i);
            this.pattern.add(ItemStack.of(lightCompound));
        }
    }
}
