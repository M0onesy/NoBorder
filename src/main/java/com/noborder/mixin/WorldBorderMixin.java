package com.noborder.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.border.WorldBorder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WorldBorder.class)
public class WorldBorderMixin {
	@Inject(method = "contains(Lnet/minecraft/util/math/BlockPos;)Z", at = @At("HEAD"), cancellable = true)
	private void noborder$containsBlockPos(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(true);
	}

	@Inject(method = "contains(Lnet/minecraft/util/math/Vec3d;)Z", at = @At("HEAD"), cancellable = true)
	private void noborder$containsVec3d(Vec3d pos, CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(true);
	}

	@Inject(method = "contains(Lnet/minecraft/util/math/ChunkPos;)Z", at = @At("HEAD"), cancellable = true)
	private void noborder$containsChunkPos(ChunkPos pos, CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(true);
	}

	@Inject(method = "contains(Lnet/minecraft/util/math/Box;)Z", at = @At("HEAD"), cancellable = true)
	private void noborder$containsBox(Box box, CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(true);
	}

	@Inject(method = "contains(DD)Z", at = @At("HEAD"), cancellable = true)
	private void noborder$containsXZ(double x, double z, CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(true);
	}

	@Inject(method = "contains(DDD)Z", at = @At("HEAD"), cancellable = true)
	private void noborder$containsXYZ(double x, double y, double z, CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(true);
	}

	@Inject(method = "getDistanceInsideBorder(DD)D", at = @At("HEAD"), cancellable = true)
	private void noborder$getDistanceInsideBorderXZ(double x, double z, CallbackInfoReturnable<Double> cir) {
		cir.setReturnValue(Double.POSITIVE_INFINITY);
	}

	@Inject(method = "canCollide(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Box;)Z", at = @At("HEAD"), cancellable = true)
	private void noborder$canCollide(Entity entity, Box box, CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(false);
	}

	@Inject(method = "asVoxelShape()Lnet/minecraft/util/shape/VoxelShape;", at = @At("HEAD"), cancellable = true)
	private void noborder$asVoxelShape(CallbackInfoReturnable<VoxelShape> cir) {
		cir.setReturnValue(VoxelShapes.empty());
	}
}
