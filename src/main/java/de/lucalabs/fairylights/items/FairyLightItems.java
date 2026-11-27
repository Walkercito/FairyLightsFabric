package de.lucalabs.fairylights.items;

import de.lucalabs.fairylights.FairyLights;
import de.lucalabs.fairylights.blocks.FairyLightBlocks;
import de.lucalabs.fairylights.blocks.LightBlock;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public final class FairyLightItems {

    public static final ConnectionItem HANGING_LIGHTS = register("hanging_lights", key -> new HangingLightsConnectionItem(properties(key)));
    public static final ConnectionItem PENNANT_BUNTING = register("pennant_bunting", key -> new PennantBuntingConnectionItem(properties(key)));

    public static final ConnectionItem TINSEL = register("tinsel", key -> new TinselConnectionItem(properties(key)));
    public static final ConnectionItem GARLAND = register("garland", key -> new GarlandConnectionItem(properties(key)));

    public static final LightItem FAIRY_LIGHT = register("fairy_light", key -> new ColorLightItem(FairyLightBlocks.FAIRY_LIGHT, properties(key).stacksTo(16)));
    public static final LightItem PAPER_LANTERN = register("paper_lantern", key -> new ColorLightItem(FairyLightBlocks.PAPER_LANTERN, properties(key).stacksTo(16)));
    public static final LightItem ORB_LANTERN = register("orb_lantern", key -> new ColorLightItem(FairyLightBlocks.ORB_LANTERN, properties(key).stacksTo(16)));
    public static final LightItem FLOWER_LIGHT = register("flower_light", key -> new ColorLightItem(FairyLightBlocks.FLOWER_LIGHT, properties(key).stacksTo(16)));
    public static final LightItem CANDLE_LANTERN_LIGHT = register("candle_lantern_light", key -> new ColorLightItem(FairyLightBlocks.CANDLE_LANTERN_LIGHT, properties(key).stacksTo(16)));
    public static final LightItem JACK_O_LANTERN = register("jack_o_lantern", key -> new ColorLightItem(FairyLightBlocks.JACK_O_LANTERN, properties(key).stacksTo(16)));
    public static final LightItem SKULL_LIGHT = register("skull_light", key -> new ColorLightItem(FairyLightBlocks.SKULL_LIGHT, properties(key).stacksTo(16)));
    public static final LightItem GHOST_LIGHT = register("ghost_light", key -> new ColorLightItem(FairyLightBlocks.GHOST_LIGHT, properties(key).stacksTo(16)));
    public static final LightItem SPIDER_LIGHT = register("spider_light", key -> new ColorLightItem(FairyLightBlocks.SPIDER_LIGHT, properties(key).stacksTo(16)));
    public static final LightItem WITCH_LIGHT = register("witch_light", key -> new ColorLightItem(FairyLightBlocks.WITCH_LIGHT, properties(key).stacksTo(16)));
    public static final LightItem SNOWFLAKE_LIGHT = register("snowflake_light", key -> new ColorLightItem(FairyLightBlocks.SNOWFLAKE_LIGHT, properties(key).stacksTo(16)));
    public static final LightItem HEART_LIGHT = register("heart_light", key -> new ColorLightItem(FairyLightBlocks.HEART_LIGHT, properties(key).stacksTo(16)));
    public static final LightItem MOON_LIGHT = register("moon_light", key -> new ColorLightItem(FairyLightBlocks.MOON_LIGHT, properties(key).stacksTo(16)));
    public static final LightItem STAR_LIGHT = register("star_light", key -> new ColorLightItem(FairyLightBlocks.STAR_LIGHT, properties(key).stacksTo(16)));
    public static final LightItem ICICLE_LIGHTS = register("icicle_lights", key -> new ColorLightItem(FairyLightBlocks.ICICLE_LIGHTS, properties(key).stacksTo(16)));
    public static final LightItem METEOR_LIGHT = register("meteor_light", key -> new ColorLightItem(FairyLightBlocks.METEOR_LIGHT, properties(key).stacksTo(16)));
    public static final LightItem CANDLE_LANTERN = register("candle_lantern", key -> new LightItem(FairyLightBlocks.CANDLE_LANTERN, properties(key).stacksTo(16)));
    public static final LightItem OIL_LANTERN = register("oil_lantern", key -> new LightItem(FairyLightBlocks.OIL_LANTERN, properties(key).stacksTo(16)));
    public static final LightItem INCANDESCENT_LIGHT = register("incandescent_light", key -> new LightItem(FairyLightBlocks.INCANDESCENT_LIGHT, properties(key).stacksTo(16)));

    public static final Item TRIANGLE_PENNANT = register("triangle_pennant", key -> new PennantItem(properties(key)));
    public static final Item SQUARE_PENNANT = register("square_pennant", key -> new PennantItem(properties(key)));

    private FairyLightItems() {
    }

    private static Item.Properties properties(final ResourceKey<Item> key) {
        return new Item.Properties().setId(key);
    }

    private static <T extends Item> T register(final String name, final Function<ResourceKey<Item>, T> factory) {
        final Identifier identifier = Identifier.fromNamespaceAndPath(FairyLights.ID, name);
        final ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, identifier);
        return Registry.register(BuiltInRegistries.ITEM, key, factory.apply(key));
    }

    public static void initialize() {
        FairyLights.LOGGER.info("Registering items");
    }

    public static Stream<LightItem> lights() {
        return BuiltInRegistries.ITEM.entrySet().stream()
                .map(Map.Entry::getValue)
                .filter(LightItem.class::isInstance)
                .map(LightItem.class::cast);
    }
}
