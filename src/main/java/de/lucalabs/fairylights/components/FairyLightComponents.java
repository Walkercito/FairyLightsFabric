package de.lucalabs.fairylights.components;

import de.lucalabs.fairylights.FairyLights;
import de.lucalabs.fairylights.blocks.entity.FastenerBlockEntity;
import de.lucalabs.fairylights.entity.FenceFastenerEntity;
import de.lucalabs.fairylights.fastener.BlockFastener;
import de.lucalabs.fairylights.fastener.FenceFastener;
import de.lucalabs.fairylights.fastener.PlayerFastener;
import de.lucalabs.fairylights.fastener.RegularBlockView;
import de.lucalabs.fairylights.items.LightItem;
import de.lucalabs.fairylights.items.LightVariant;
import org.ladysnake.cca.api.v3.block.BlockComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.block.BlockComponentInitializer;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.item.ItemComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.item.ItemComponentInitializer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

public class FairyLightComponents implements EntityComponentInitializer, BlockComponentInitializer, ItemComponentInitializer {

    public static final Identifier FASTENER_ID = Identifier.tryBuild(FairyLights.ID, "fastener");
    public static final ComponentKey<FastenerComponent> FASTENER =
            ComponentRegistry.getOrCreate(FASTENER_ID, FastenerComponent.class);

    public static final Identifier LIGHT_VARIANT_ID = Identifier.tryBuild(FairyLights.ID, "light_variant");
    public static final ComponentKey<LightVariantComponent> LIGHT_VARIANT =
            ComponentRegistry.getOrCreate(LIGHT_VARIANT_ID, LightVariantComponent.class);

    @Override
    public void registerBlockComponentFactories(BlockComponentFactoryRegistry registry) {
        registry.registerFor(
                FastenerBlockEntity.class,
                FASTENER,
                be -> new FastenerComponent().setFastener(new BlockFastener(be, new RegularBlockView())));
    }

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerFor(Player.class, FASTENER, e -> new FastenerComponent().setFastener(new PlayerFastener(e)));
        registry.registerFor(FenceFastenerEntity.class, FASTENER, e -> new FastenerComponent().setFastener(new FenceFastener(e)));
    }

    @Override
    public void registerItemComponentFactories(ItemComponentFactoryRegistry registry) {
        registry.register(i -> i instanceof LightItem, LIGHT_VARIANT, i -> {
            LightVariant<?> lightVariant = ((LightItem) i.getItem()).getBlock().getVariant();
            return new LightVariantComponent(i, LIGHT_VARIANT).setLightVariant(lightVariant);
        });
    }
}
