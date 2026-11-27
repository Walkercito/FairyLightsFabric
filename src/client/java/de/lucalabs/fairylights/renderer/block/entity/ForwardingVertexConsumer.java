package de.lucalabs.fairylights.renderer.block.entity;

import com.mojang.blaze3d.vertex.VertexConsumer;

public abstract class ForwardingVertexConsumer implements VertexConsumer {
    protected abstract VertexConsumer delegate();

    @Override
    public VertexConsumer addVertex(final float x, final float y, final float z) {
        return this.delegate().addVertex(x, y, z);
    }

    @Override
    public VertexConsumer setColor(final int r, final int g, final int b, final int a) {
        return this.delegate().setColor(r, g, b, a);
    }

    @Override
    public VertexConsumer setColor(final int color) {
        return this.delegate().setColor(color);
    }

    @Override
    public VertexConsumer setUv(final float u, final float v) {
        return this.delegate().setUv(u, v);
    }

    @Override
    public VertexConsumer setUv1(final int u, final int v) {
        return this.delegate().setUv1(u, v);
    }

    @Override
    public VertexConsumer setUv2(final int u, final int v) {
        return this.delegate().setUv2(u, v);
    }

    @Override
    public VertexConsumer setNormal(final float x, final float y, final float z) {
        return this.delegate().setNormal(x, y, z);
    }

    @Override
    public VertexConsumer setLineWidth(final float width) {
        return this.delegate().setLineWidth(width);
    }
}
