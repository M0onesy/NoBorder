package com.noborder.mixin;

import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Entity.class)
public class EntityMixin {
	@Redirect(
		method = "updatePosition(DDD)V",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;clamp(DDD)D", ordinal = 0)
	)
	private double noborder$skipXClampInUpdatePosition(double value, double min, double max) {
		return value;
	}

	@Redirect(
		method = "updatePosition(DDD)V",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;clamp(DDD)D", ordinal = 1)
	)
	private double noborder$skipZClampInUpdatePosition(double value, double min, double max) {
		return value;
	}

	@Redirect(
		method = "readData(Lnet/minecraft/storage/ReadView;)V",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;clamp(DDD)D", ordinal = 0)
	)
	private double noborder$skipXClampInReadData(double value, double min, double max) {
		return value;
	}

	@Redirect(
		method = "readData(Lnet/minecraft/storage/ReadView;)V",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;clamp(DDD)D", ordinal = 2)
	)
	private double noborder$skipZClampInReadData(double value, double min, double max) {
		return value;
	}
}
