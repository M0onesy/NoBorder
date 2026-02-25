package com.noborder.mixin;

import net.minecraft.server.command.TeleportCommand;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TeleportCommand.class)
public class TeleportCommandMixin {
	@Redirect(
		method = "teleport",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;isValid(Lnet/minecraft/util/math/BlockPos;)Z")
	)
	private static boolean noborder$allowTeleportOutsideWorldBounds(BlockPos pos) {
		return true;
	}
}
