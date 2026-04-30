package com.ironclient.mixin;

import com.ironclient.util.IronConfig;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.particle.ParticleEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {

    // Cancel crystal particle spawning
    @Inject(method = "spawnParticle", at = @At("HEAD"), cancellable = true)
    private void onSpawnParticle(ParticleEffect parameters, boolean alwaysSpawn,
                                  boolean canSpawnOnMinimal, double x, double y, double z,
                                  double velocityX, double velocityY, double velocityZ,
                                  CallbackInfo ci) {
        if (IronConfig.noCrystalParticles) {
            // Check if particle is from end crystal by checking nearby entities
            net.minecraft.client.MinecraftClient client =
                net.minecraft.client.MinecraftClient.getInstance();
            if (client.world != null) {
                boolean nearCrystal = client.world.getEntitiesByClass(
                    EndCrystalEntity.class,
                    new net.minecraft.util.math.Box(x-3, y-3, z-3, x+3, y+3, z+3),
                    e -> true
                ).size() > 0;
                if (nearCrystal) ci.cancel();
            }
        }
    }

    // Custom block highlight color - injected into drawBlockOutline
    @Inject(method = "drawBlockOutline", at = @At("HEAD"), cancellable = true)
    private void onDrawBlockOutline(MatrixStack matrices,
                                     net.minecraft.client.render.VertexConsumer vertexConsumer,
                                     Entity entity,
                                     double cameraX, double cameraY, double cameraZ,
                                     net.minecraft.util.math.BlockPos pos,
                                     net.minecraft.block.BlockState state,
                                     CallbackInfo ci) {
        if (!IronConfig.blockHighlightEnabled) {
            ci.cancel();
        }
        // Color modification handled via shader/vertex consumer override
    }
}
