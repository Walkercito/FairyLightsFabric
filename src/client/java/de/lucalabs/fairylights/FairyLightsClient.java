package de.lucalabs.fairylights;

import de.lucalabs.fairylights.blocks.entity.FairyLightBlockEntities;
import de.lucalabs.fairylights.entity.FairyLightEntities;
import de.lucalabs.fairylights.fastener.RegularBlockView;
import de.lucalabs.fairylights.items.ItemColorManager;
import de.lucalabs.fairylights.model.light.*;
import de.lucalabs.fairylights.renderer.FairyLightModelLayers;
import de.lucalabs.fairylights.renderer.block.entity.*;
import de.lucalabs.fairylights.renderer.entity.FenceFastenerRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

public class FairyLightsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockEntityRenderers.register(
                FairyLightBlockEntities.FASTENER,
                context -> new FastenerBlockEntityRenderer(context, new RegularBlockView()));

        BlockEntityRenderers.register(
                FairyLightBlockEntities.LIGHT,
                LightBlockEntityRenderer::new
        );

        EntityRendererRegistry.register(FairyLightEntities.FASTENER, FenceFastenerRenderer::new);

        ModelLayerRegistry.registerModelLayer(FairyLightModelLayers.BOW, BowModel::createLayer);
        ModelLayerRegistry.registerModelLayer(FairyLightModelLayers.GARLAND_RINGS, GarlandVineRenderer.RingsModel::createLayer);
        ModelLayerRegistry.registerModelLayer(FairyLightModelLayers.TINSEL_STRIP, GarlandTinselRenderer.StripModel::createLayer);
        ModelLayerRegistry.registerModelLayer(FairyLightModelLayers.FAIRY_LIGHT, FairyLightModel::createLayer);
        ModelLayerRegistry.registerModelLayer(FairyLightModelLayers.PAPER_LANTERN, PaperLanternModel::createLayer);
        ModelLayerRegistry.registerModelLayer(FairyLightModelLayers.ORB_LANTERN, OrbLanternModel::createLayer);
        ModelLayerRegistry.registerModelLayer(FairyLightModelLayers.FLOWER_LIGHT, FlowerLightModel::createLayer);
        ModelLayerRegistry.registerModelLayer(FairyLightModelLayers.CANDLE_LANTERN_LIGHT, ColorCandleLanternModel::createLayer);
        ModelLayerRegistry.registerModelLayer(FairyLightModelLayers.JACK_O_LANTERN, JackOLanternLightModel::createLayer);
        ModelLayerRegistry.registerModelLayer(FairyLightModelLayers.SKULL_LIGHT, SkullLightModel::createLayer);
        ModelLayerRegistry.registerModelLayer(FairyLightModelLayers.GHOST_LIGHT, GhostLightModel::createLayer);
        ModelLayerRegistry.registerModelLayer(FairyLightModelLayers.SPIDER_LIGHT, SpiderLightModel::createLayer);
        ModelLayerRegistry.registerModelLayer(FairyLightModelLayers.WITCH_LIGHT, WitchLightModel::createLayer);
        ModelLayerRegistry.registerModelLayer(FairyLightModelLayers.SNOWFLAKE_LIGHT, SnowflakeLightModel::createLayer);
        ModelLayerRegistry.registerModelLayer(FairyLightModelLayers.HEART_LIGHT, HeartLightModel::createLayer);
        ModelLayerRegistry.registerModelLayer(FairyLightModelLayers.MOON_LIGHT, MoonLightModel::createLayer);
        ModelLayerRegistry.registerModelLayer(FairyLightModelLayers.STAR_LIGHT, StarLightModel::createLayer);
        ModelLayerRegistry.registerModelLayer(FairyLightModelLayers.ICICLE_LIGHTS_1, () -> IcicleLightsModel.createLayer(1));
        ModelLayerRegistry.registerModelLayer(FairyLightModelLayers.ICICLE_LIGHTS_2, () -> IcicleLightsModel.createLayer(2));
        ModelLayerRegistry.registerModelLayer(FairyLightModelLayers.ICICLE_LIGHTS_3, () -> IcicleLightsModel.createLayer(3));
        ModelLayerRegistry.registerModelLayer(FairyLightModelLayers.ICICLE_LIGHTS_4, () -> IcicleLightsModel.createLayer(4));
        ModelLayerRegistry.registerModelLayer(FairyLightModelLayers.METEOR_LIGHT, MeteorLightModel::createLayer);
        ModelLayerRegistry.registerModelLayer(FairyLightModelLayers.CANDLE_LANTERN, CandleLanternModel::createLayer);
        ModelLayerRegistry.registerModelLayer(FairyLightModelLayers.OIL_LANTERN, OilLanternModel::createLayer);
        ModelLayerRegistry.registerModelLayer(FairyLightModelLayers.INCANDESCENT_LIGHT, IncandescentLightModel::createLayer);
        ModelLayerRegistry.registerModelLayer(FairyLightModelLayers.TINSEL_WIRE, GarlandTinselRenderer::wireLayer);
        ModelLayerRegistry.registerModelLayer(FairyLightModelLayers.VINE_WIRE, GarlandVineRenderer::wireLayer);
        ModelLayerRegistry.registerModelLayer(FairyLightModelLayers.PENNANT_WIRE, PennantBuntingRenderer::wireLayer);
        ModelLayerRegistry.registerModelLayer(FairyLightModelLayers.LIGHTS_WIRE, HangingLightsRenderer::wireLayer);

        ModelLoadingRegistry.INSTANCE.registerModelProvider((manager, out) -> {
            // Tell Fabric to load this model during resource reloading
            out.accept(FenceFastenerRenderer.MODEL);
            PennantBuntingRenderer.MODELS.forEach(out);
        });


        ItemColorManager.setupColors();
    }

}