package com.tom.vivecraftcompat.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.vivecraft.client_vr.render.helpers.VREffectsHelper;
import org.vivecraft.common.utils.math.Matrix4f;

import net.minecraft.world.phys.Vec3;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.vertex.PoseStack;

import com.tom.vivecraftcompat.overlay.OverlayManager;

@Mixin(value = VREffectsHelper.class)
public abstract class VREffectsHelperMixin {
	static @Shadow(remap = false) void render2D(float partialTicks, RenderTarget framebuffer, Vec3 pos, Matrix4f rot, boolean depthAlways, PoseStack poseStack) {}
	public static @Shadow(remap = false) boolean shouldOccludeGui() { return false; }

	@Inject(at = @At(value = "INVOKE", target = "Lorg/vivecraft/client_vr/gameplay/screenhandlers/RadialHandler;isShowing()Z", remap = false), method = "renderGuiAndShadow", remap = false)
	private static void renderHudLayers(PoseStack poseStack, float partialTicks, boolean depthAlways, boolean shadowFirst, CallbackInfo cbi) {
		OverlayManager.renderLayers(l -> render2D(partialTicks, l.getFramebuffer(), l.getPos(), l.getRotation(), depthAlways, poseStack));
	}

	@Inject(at = @At("HEAD"), method = "shouldRenderCrosshair", remap = false, cancellable = true)
	private static void shouldRenderCrosshair(CallbackInfoReturnable<Boolean> cbi) {
		if (OverlayManager.isUsingController())cbi.setReturnValue(false);
	}
}
