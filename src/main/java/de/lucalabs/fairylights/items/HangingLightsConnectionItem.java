package de.lucalabs.fairylights.items;

import de.lucalabs.fairylights.connection.ConnectionTypes;
import de.lucalabs.fairylights.items.components.FairyLightItemComponents;
import de.lucalabs.fairylights.registries.FairyLightRegistries;
import de.lucalabs.fairylights.string.StringType;
import de.lucalabs.fairylights.string.StringTypes;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

public final class HangingLightsConnectionItem extends ConnectionItem {
    public HangingLightsConnectionItem(final Properties properties) {
        super(properties, ConnectionTypes.HANGING_LIGHTS);
    }

    public static StringType getString(final CompoundTag tag) {
        final Identifier name = Identifier.tryParse(tag.getStringOr("string", ""));
        final StringType string = name == null ? null : FairyLightRegistries.STRING_TYPES.getValue(name);
        return string == null ? StringTypes.BLACK_STRING : string;
    }

    public static void setString(final CompoundTag tag, final StringType string) {
        final Identifier name = FairyLightRegistries.STRING_TYPES.getKey(string);
        if (name == null) {
            throw new NullPointerException("Missing registry name: " + string);
        }
        tag.putString("string", name.toString());
    }

    public static StringType getString(final ItemStack stack) {
        final Identifier name = stack.get(FairyLightItemComponents.STRING);
        final StringType string = name == null ? null : FairyLightRegistries.STRING_TYPES.getValue(name);
        return string == null ? StringTypes.BLACK_STRING : string;
    }

    public static void setString(final ItemStack stack, final StringType string) {
        final Identifier name = Objects.requireNonNull(FairyLightRegistries.STRING_TYPES.getKey(string));
        stack.set(FairyLightItemComponents.STRING, name);
    }

    @Override
    public CompoundTag serializeLogic(final ItemStack stack) {
        final CompoundTag tag = new CompoundTag();
        setString(tag, getString(stack));
        final ListTag list = new ListTag();
        for (final ItemStack light : stack.getOrDefault(FairyLightItemComponents.PATTERN, List.<ItemStack>of())) {
            list.add(FairyLightItemComponents.saveStack(light));
        }
        tag.put("pattern", list);
        return tag;
    }

    @Override
    public void deserializeLogic(final ItemStack stack, final CompoundTag tag) {
        stack.set(FairyLightItemComponents.STRING, Objects.requireNonNull(FairyLightRegistries.STRING_TYPES.getKey(getString(tag))));
        final List<ItemStack> pattern = new ArrayList<>();
        for (final Tag entry : tag.getListOrEmpty("pattern")) {
            if (entry instanceof CompoundTag compound) {
                pattern.add(FairyLightItemComponents.loadStack(compound));
            }
        }
        stack.set(FairyLightItemComponents.PATTERN, pattern);
    }

    @Override
    public void appendHoverText(
            final ItemStack stack,
            final Item.TooltipContext context,
            final TooltipDisplay display,
            final Consumer<Component> tooltip,
            final TooltipFlag flag) {
        final Identifier name = stack.get(FairyLightItemComponents.STRING);
        if (name != null) {
            tooltip.accept(Component.translatable("item." + name.getNamespace() + "." + name.getPath()).withStyle(ChatFormatting.GRAY));
        }
        final List<ItemStack> pattern = stack.get(FairyLightItemComponents.PATTERN);
        if (pattern != null && !pattern.isEmpty()) {
            tooltip.accept(Component.empty());
            for (final ItemStack lightStack : pattern) {
                tooltip.accept(lightStack.getHoverName());
                lightStack.getItem().appendHoverText(lightStack, context, display, tooltip, flag);
            }
        }
    }
}
