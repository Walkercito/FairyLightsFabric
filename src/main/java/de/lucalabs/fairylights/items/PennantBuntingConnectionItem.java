package de.lucalabs.fairylights.items;

import de.lucalabs.fairylights.connection.ConnectionTypes;
import de.lucalabs.fairylights.items.components.FairyLightItemComponents;
import de.lucalabs.fairylights.util.styled.StyledString;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

public class PennantBuntingConnectionItem extends ConnectionItem {
    public PennantBuntingConnectionItem(final Item.Properties properties) {
        super(properties, ConnectionTypes.PENNANT_BUNTING);
    }

    @Override
    public CompoundTag serializeLogic(final ItemStack stack) {
        final CompoundTag tag = new CompoundTag();
        final CompoundTag text = stack.get(FairyLightItemComponents.TEXT);
        if (text != null) {
            tag.put("text", text);
        }
        final ListTag list = new ListTag();
        for (final ItemStack pennant : stack.getOrDefault(FairyLightItemComponents.PATTERN, List.<ItemStack>of())) {
            list.add(FairyLightItemComponents.saveStack(pennant));
        }
        tag.put("pattern", list);
        return tag;
    }

    @Override
    public void deserializeLogic(final ItemStack stack, final CompoundTag tag) {
        stack.set(FairyLightItemComponents.TEXT, tag.getCompoundOrEmpty("text"));
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
        super.appendHoverText(stack, context, display, tooltip, flag);
        final CompoundTag text = stack.get(FairyLightItemComponents.TEXT);
        if (text != null) {
            final StyledString s = StyledString.deserialize(text);
            if (!s.isEmpty()) {
                tooltip.accept(Component.translatable("format.fairylights.text", s.toTextText()).withStyle(ChatFormatting.GRAY));
            }
        }
        final List<ItemStack> pattern = stack.get(FairyLightItemComponents.PATTERN);
        if (pattern != null && !pattern.isEmpty()) {
            tooltip.accept(Component.empty());
            for (final ItemStack item : pattern) {
                tooltip.accept(item.getHoverName());
            }
        }
    }
}
