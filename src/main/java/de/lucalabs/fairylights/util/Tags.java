package de.lucalabs.fairylights.util;

import de.lucalabs.fairylights.FairyLights;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public final class Tags {
    public static final TagKey<Item> DYES = TagKey.create(Registries.ITEM, Identifier.tryBuild("c", "dyes"));
    public static final TagKey<Item> DYES_WHITE = TagKey.create(Registries.ITEM, Identifier.tryBuild("c", "white_dyes"));
    public static final TagKey<Item> DYES_ORANGE = TagKey.create(Registries.ITEM, Identifier.tryBuild("c", "orange_dyes"));
    public static final TagKey<Item> DYES_MAGENTA = TagKey.create(Registries.ITEM, Identifier.tryBuild("c", "magenta_dyes"));
    public static final TagKey<Item> DYES_LIGHT_BLUE = TagKey.create(Registries.ITEM, Identifier.tryBuild("c", "light_blue_dyes"));
    public static final TagKey<Item> DYES_YELLOW = TagKey.create(Registries.ITEM, Identifier.tryBuild("c", "yellow_dyes"));
    public static final TagKey<Item> DYES_LIME = TagKey.create(Registries.ITEM, Identifier.tryBuild("c", "lime_dyes"));
    public static final TagKey<Item> DYES_PINK = TagKey.create(Registries.ITEM, Identifier.tryBuild("c", "pink_dyes"));
    public static final TagKey<Item> DYES_GRAY = TagKey.create(Registries.ITEM, Identifier.tryBuild("c", "gray_dyes"));
    public static final TagKey<Item> DYES_LIGHT_GRAY = TagKey.create(Registries.ITEM, Identifier.tryBuild("c", "light_gray_dyes"));
    public static final TagKey<Item> DYES_CYAN = TagKey.create(Registries.ITEM, Identifier.tryBuild("c", "cyan_dyes"));
    public static final TagKey<Item> DYES_PURPLE = TagKey.create(Registries.ITEM, Identifier.tryBuild("c", "purple_dyes"));
    public static final TagKey<Item> DYES_BLUE = TagKey.create(Registries.ITEM, Identifier.tryBuild("c", "blue_dyes"));
    public static final TagKey<Item> DYES_BROWN = TagKey.create(Registries.ITEM, Identifier.tryBuild("c", "brown_dyes"));
    public static final TagKey<Item> DYES_GREEN = TagKey.create(Registries.ITEM, Identifier.tryBuild("c", "green_dyes"));
    public static final TagKey<Item> DYES_RED = TagKey.create(Registries.ITEM, Identifier.tryBuild("c", "red_dyes"));
    public static final TagKey<Item> DYES_BLACK = TagKey.create(Registries.ITEM, Identifier.tryBuild("c", "black_dyes"));

    public static final TagKey<Block> FENCES = TagKey.create(Registries.BLOCK, Identifier.tryBuild("c", "fences"));

    public static final TagKey<Item> LIGHTS = TagKey.create(Registries.ITEM, Identifier.tryBuild(FairyLights.ID, "lights"));
    public static final TagKey<Item> TWINKLING_LIGHTS = TagKey.create(Registries.ITEM, Identifier.tryBuild(FairyLights.ID, "twinkling_lights"));
    public static final TagKey<Item> PENNANTS = TagKey.create(Registries.ITEM, Identifier.tryBuild(FairyLights.ID, "pennants"));
    public static final TagKey<Item> DYEABLE = TagKey.create(Registries.ITEM, Identifier.tryBuild(FairyLights.ID, "dyeable"));
    public static final TagKey<Item> DYEABLE_LIGHTS = TagKey.create(Registries.ITEM, Identifier.tryBuild(FairyLights.ID, "dyeable_lights"));

    private Tags() {
    }

    public static void initialize() {
        FairyLights.LOGGER.info("Initializing tags");
    }
}
