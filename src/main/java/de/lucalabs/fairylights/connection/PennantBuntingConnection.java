package de.lucalabs.fairylights.connection;

import de.lucalabs.fairylights.fastener.Fastener;
import de.lucalabs.fairylights.feature.FeatureType;
import de.lucalabs.fairylights.feature.Pennant;
import de.lucalabs.fairylights.items.DyeableItem;
import de.lucalabs.fairylights.items.components.FairyLightItemComponents;
import de.lucalabs.fairylights.items.components.FairyLightItemComponents;
import de.lucalabs.fairylights.sounds.FairyLightSounds;
import de.lucalabs.fairylights.util.ItemHelper;
import de.lucalabs.fairylights.util.OreDictUtils;
import de.lucalabs.fairylights.util.Tags;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public final class PennantBuntingConnection extends HangingFeatureConnection<Pennant> {
    private List<ItemStack> pattern;

    public PennantBuntingConnection(
            final ConnectionType<? extends PennantBuntingConnection> type,
            final Level world,
            final Fastener<?> fastener,
            final UUID uuid) {

        super(type, world, fastener, uuid);
        this.pattern = new ArrayList<>();
    }

    @Override
    public float getRadius() {
        return 0.045F;
    }

    @Override
    public boolean interact(
            final Player player,
            final Vec3 hit,
            final FeatureType featureType,
            final int feature,
            final ItemStack heldStack,
            final InteractionHand hand) {

        if (featureType == FEATURE && heldStack.is(Tags.PENNANTS)) { // OreDictUtils.isDye(heldStack)) {
            final int index = feature % this.pattern.size();
            final ItemStack pennant = this.pattern.get(index);
            if (!ItemStack.matches(pennant, heldStack)) {
                final ItemStack placed = heldStack.split(1);
                this.pattern.set(index, placed);

                ItemHelper.giveItemToPlayer(player, pennant);

                this.computeCatenary();
                this.world.playSound(null, hit.x, hit.y, hit.z, FairyLightSounds.FEATURE_COLOR_CHANGE, SoundSource.BLOCKS, 1, 1);
                return true;
            }
        }

        if (featureType == FEATURE && OreDictUtils.isDye(heldStack) && !this.pattern.isEmpty()) {
            final int index = feature % this.pattern.size();
            final ItemStack pennant = this.pattern.get(index).copyWithCount(1);
            DyeableItem.setColor(pennant, OreDictUtils.getDyeColor(heldStack));
            this.pattern.set(index, pennant);
            if (!player.getAbilities().instabuild) {
                heldStack.shrink(1);
            }
            this.computeCatenary();
            this.world.playSound(null, hit.x, hit.y, hit.z, FairyLightSounds.FEATURE_COLOR_CHANGE, SoundSource.BLOCKS, 1, 1);
            return true;
        }
        return super.interact(player, hit, featureType, feature, heldStack, hand);
    }

    @Override
    protected void onUpdate() {
        super.onUpdate();
        for (final Pennant light : this.features) {
            light.tick(this.world);
        }
    }

    @Override
    protected boolean canReuse(final Pennant feature, final int index) {
        return false;
    }

    @Override
    protected Pennant[] createFeatures(final int length) {
        return new Pennant[length];
    }

    @Override
    protected Pennant createFeature(final int index, final Vec3 point, final float yaw, final float pitch) {
        final ItemStack data = this.pattern.isEmpty() ? ItemStack.EMPTY : this.pattern.get(index % this.pattern.size());
        return new Pennant(index, point, yaw, pitch, DyeableItem.getColor(data), data.getItem());
    }

    @Override
    protected float getFeatureSpacing() {
        return 0.6875F;
    }

    @Override
    public CompoundTag serializeLogic() {
        final CompoundTag compound = super.serializeLogic();
        final ListTag patternList = new ListTag();
        for (final ItemStack entry : this.pattern) {
            patternList.add(FairyLightItemComponents.saveStack(entry));
        }
        compound.put("pattern", patternList);
        return compound;
    }

    @Override
    public void deserializeLogic(final CompoundTag compound) {
        super.deserializeLogic(compound);
        this.pattern = new ArrayList<>();
        for (final Tag entry : compound.getListOrEmpty("pattern")) {
            if (entry instanceof CompoundTag light) {
                this.pattern.add(FairyLightItemComponents.loadStack(light));
            }
        }
    }
}
