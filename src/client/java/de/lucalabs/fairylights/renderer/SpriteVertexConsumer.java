package de.lucalabs.fairylights.renderer;

import com.mojang.blaze3d.vertex.VertexConsumer;
import de.lucalabs.fairylights.renderer.block.entity.ForwardingVertexConsumer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public final class SpriteVertexConsumer extends ForwardingVertexConsumer {

    private final VertexConsumer delegate;
    private final TextureAtlasSprite sprite;

    public SpriteVertexConsumer(final VertexConsumer delegate, final TextureAtlasSprite sprite) {
        this.delegate = delegate;
        this.sprite = sprite;
    }

    @Override
    protected VertexConsumer delegate() {
        return this.delegate;
    }

    @Override
    public VertexConsumer setUv(final float u, final float v) {
        return this.delegate.setUv(this.sprite.getU(u), this.sprite.getV(v));
    }
}
