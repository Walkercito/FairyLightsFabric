package de.lucalabs.fairylights.renderer;

import de.lucalabs.fairylights.FairyLights;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.resources.Identifier;

public class RenderConstants {

    public static final float HIGHLIGHT_ALPHA = 0.4F;

    public static final SpriteId CONNECTIONS = new SpriteId(
            TextureAtlas.LOCATION_BLOCKS,
            Identifier.fromNamespaceAndPath(FairyLights.ID, "entity/connections"));

    public static final SpriteId SOLID_TEXTURE = CONNECTIONS;
    public static final SpriteId TRANSLUCENT_TEXTURE = CONNECTIONS;
}
