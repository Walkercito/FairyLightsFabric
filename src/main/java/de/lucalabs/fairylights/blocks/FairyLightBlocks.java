package de.lucalabs.fairylights.blocks;

import de.lucalabs.fairylights.FairyLights;
import de.lucalabs.fairylights.items.LightVariant;
import de.lucalabs.fairylights.items.SimpleLightVariant;
import java.util.function.Function;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public final class FairyLightBlocks {

    public static final FastenerBlock FASTENER = register("fastener", key ->
            new FastenerBlock(
                    BlockBehaviour.Properties.of()
                            .setId(key)
                            .mapColor(MapColor.METAL)
                            .forceSolidOn()
                            .strength(3.5F)
                            .sound(SoundType.LANTERN)));

    public static final LightBlock FAIRY_LIGHT = registerLight("fairy_light", SimpleLightVariant.FAIRY_LIGHT);
    public static final LightBlock PAPER_LANTERN = registerLight("paper_lantern", SimpleLightVariant.PAPER_LANTERN);
    public static final LightBlock ORB_LANTERN = registerLight("orb_lantern", SimpleLightVariant.ORB_LANTERN);
    public static final LightBlock FLOWER_LIGHT = registerLight("flower_light", SimpleLightVariant.FLOWER_LIGHT);
    public static final LightBlock CANDLE_LANTERN_LIGHT = registerLight("candle_lantern_light", SimpleLightVariant.CANDLE_LANTERN_LIGHT);
    public static final LightBlock JACK_O_LANTERN = registerLight("jack_o_lantern", SimpleLightVariant.JACK_O_LANTERN);
    public static final LightBlock SKULL_LIGHT = registerLight("skull_light", SimpleLightVariant.SKULL_LIGHT);
    public static final LightBlock GHOST_LIGHT = registerLight("ghost_light", SimpleLightVariant.GHOST_LIGHT);
    public static final LightBlock SPIDER_LIGHT = registerLight("spider_light", SimpleLightVariant.SPIDER_LIGHT);
    public static final LightBlock WITCH_LIGHT = registerLight("witch_light", SimpleLightVariant.WITCH_LIGHT);
    public static final LightBlock SNOWFLAKE_LIGHT = registerLight("snowflake_light", SimpleLightVariant.SNOWFLAKE_LIGHT);
    public static final LightBlock HEART_LIGHT = registerLight("heart_light", SimpleLightVariant.HEART_LIGHT);
    public static final LightBlock MOON_LIGHT = registerLight("moon_light", SimpleLightVariant.MOON_LIGHT);
    public static final LightBlock STAR_LIGHT = registerLight("star_light", SimpleLightVariant.STAR_LIGHT);
    public static final LightBlock ICICLE_LIGHTS = registerLight("icicle_lights", SimpleLightVariant.ICICLE_LIGHTS);
    public static final LightBlock METEOR_LIGHT = registerLight("meteor_light", SimpleLightVariant.METEOR_LIGHT);
    public static final LightBlock CANDLE_LANTERN = registerLight("candle_lantern", SimpleLightVariant.CANDLE_LANTERN);
    public static final LightBlock OIL_LANTERN = registerLight("oil_lantern", SimpleLightVariant.OIL_LANTERN);
    public static final LightBlock INCANDESCENT_LIGHT = registerLight("incandescent_light", SimpleLightVariant.INCANDESCENT_LIGHT);

    private FairyLightBlocks() {
    }

    private static LightBlock registerLight(final String name, final LightVariant<?> variant) {
        return register(name, key -> new LightBlock(
                BlockBehaviour.Properties.of()
                        .setId(key)
                        .mapColor(MapColor.METAL)
                        .forceSolidOn()
                        .strength(3.5F)
                        .sound(SoundType.LANTERN)
                        .lightLevel(state -> state.getValue(LightBlock.LIT) ? 15 : 0)
                        .noCollision(),
                variant));
    }

    private static <T extends Block> T register(final String name, final Function<ResourceKey<Block>, T> factory) {
        final Identifier identifier = Identifier.fromNamespaceAndPath(FairyLights.ID, name);
        final ResourceKey<Block> key = ResourceKey.create(Registries.BLOCK, identifier);
        return Registry.register(BuiltInRegistries.BLOCK, key, factory.apply(key));
    }

    public static void initialize() {
        FairyLights.LOGGER.info("Registering blocks");
    }
}
