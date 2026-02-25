package com.noborder.mixin;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
public class WorldMixin {
	@Inject(method = "isValid(Lnet/minecraft/util/math/BlockPos;)Z", at = @At("HEAD"), cancellable = true)
	private static void noborder$allowAnyHorizontalPosition(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
		cir.setReturnValue(true);
	}
}
