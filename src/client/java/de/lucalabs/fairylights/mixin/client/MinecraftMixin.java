package de.lucalabs.fairylights.mixin.client;

import de.lucalabs.fairylights.events.ClientEventHandler;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Inject(method = "pick", at = @At("RETURN"))
    private void fairylights$afterPick(final float partialTicks, final CallbackInfo ci) {
        ClientEventHandler.updateHitConnection();
    }
}
