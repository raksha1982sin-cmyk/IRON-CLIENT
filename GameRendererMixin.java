package com.ironclient.mixin;

import com.ironclient.util.IronConfig;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyReturnValue;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    // Disable FOV speed effect
    @ModifyReturnValue(method = "getFov", at = @At("RETURN"))
    private double modifyFov(double original) {
        if (IronConfig.fovEffectDisabled) {
            // Return base FOV without speed/status effect modifiers
            // We approximate by returning the options FOV value directly
            return net.minecraft.client.MinecraftClient.getInstance()
                .options.getFov().getValue();
        }
        return original;
    }

    // Disable damage tilt
    @ModifyReturnValue(method = "getNightVisionStrength", at = @At("RETURN"))
    private float modifyNightVision(float original) {
        return original; // passthrough, handled via status effect
    }
}
