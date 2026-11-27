package de.lucalabs.fairylights.items;

import de.lucalabs.fairylights.FairyLights;
import de.lucalabs.fairylights.connection.ConnectionTypes;
import de.lucalabs.fairylights.registries.FairyLightRegistries;
import de.lucalabs.fairylights.string.StringType;
import de.lucalabs.fairylights.string.StringTypes;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public final class HangingLightsConnectionItem extends ConnectionItem {
    public HangingLightsConnectionItem(final Properties properties) {
        super(properties, ConnectionTypes.HANGING_LIGHTS);
    }

    public static StringType getString(final CompoundTag tag) {
        return Objects.requireNonNull(FairyLightRegistries.STRING_TYPES.get(Identifier.tryParse(tag.getString("string"))));
    }

    public static void setString(final CompoundTag tag, final StringType string) {
        final Identifier name = FairyLightRegistries.STRING_TYPES.getKey(string);
        if (name == null) {
            throw new NullPointerException("Missing registry name: " + string);
        }
        tag.putString("string", name.toString());
    }

    @Override
    public void appendHoverText(final ItemStack stack, @Nullable final Level world, final List<Component> tooltip, final TooltipFlag context) {
        final CompoundTag compound = stack.getTag();
        if (compound != null) {
            final Identifier name = Identifier.tryParse(compound.getString("string"));
            if (name != null) {
                tooltip.add(Component.translatable("item." + name.getNamespace() + "." + name.getPath()).withStyle(ChatFormatting.GRAY));
            }
        }
        if (compound != null && compound.contains("pattern", Tag.TAG_LIST)) {
            final ListTag tagList = compound.getList("pattern", Tag.TAG_COMPOUND);
            final int tagCount = tagList.size();
            if (tagCount > 0) {
                tooltip.add(Component.empty());
            }
            for (int i = 0; i < tagCount; i++) {
                final ItemStack lightStack = ItemStack.of(tagList.getCompound(i));
                tooltip.add(lightStack.getHoverName());
                lightStack.getItem().appendHoverText(lightStack, world, tooltip, context);
            }
        }
    }
}
