package com.noborder.mixin;

import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
	@Redirect(
		method = "tick()V",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;clamp(DDD)D", ordinal = 0)
	)
	private double noborder$skipXClampInTick(double value, double min, double max) {
		return value;
	}

	@Redirect(
		method = "tick()V",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;clamp(DDD)D", ordinal = 1)
	)
	private double noborder$skipZClampInTick(double value, double min, double max) {
		return value;
	}
}
