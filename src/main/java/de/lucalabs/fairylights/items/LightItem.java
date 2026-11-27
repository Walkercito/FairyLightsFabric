package de.lucalabs.fairylights.items;

import de.lucalabs.fairylights.blocks.LightBlock;
import de.lucalabs.fairylights.items.components.FairyLightItemComponents;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.jetbrains.annotations.Nullable;

public class LightItem extends BlockItem {
    private final LightBlock light;

    public LightItem(final LightBlock light, final Properties properties) {
        super(light, properties);
        this.light = light;
    }

    @Override
    public LightBlock getBlock() {
        return this.light;
    }

    @Override
    public void appendHoverText(
            final ItemStack stack,
            final Item.TooltipContext context,
            final TooltipDisplay display,
            final Consumer<Component> tooltip,
            final TooltipFlag flag) {
        super.appendHoverText(stack, context, display, tooltip, flag);
        if (stack.getOrDefault(FairyLightItemComponents.TWINKLE, false)) {
            tooltip.accept(Component.translatable("item.fairylights.twinkle").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        }
        final List<Integer> colors = stack.get(FairyLightItemComponents.COLORS);
        if (colors != null) {
            for (final int color : colors) {
                tooltip.accept(DyeableItem.getColorName(color).copy().withStyle(ChatFormatting.GRAY));
            }
        }
    }
}
