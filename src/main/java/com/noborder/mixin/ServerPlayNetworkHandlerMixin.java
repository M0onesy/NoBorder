package com.noborder.mixin;

import java.util.Set;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerPosition;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin {
	private static final double OUTER_BOUND = 29_999_984.0D;

	@Shadow
	@Final
	private ServerPlayerEntity player;

	@Shadow
	public abstract void syncWithPlayerPosition();

	private static boolean noborder$isOuter(double x, double z) {
		return Math.abs(x) > OUTER_BOUND || Math.abs(z) > OUTER_BOUND;
	}

	@Inject(method = "clampHorizontal", at = @At("HEAD"), cancellable = true)
	private static void noborder$skipHorizontalClamp(double value, CallbackInfoReturnable<Double> cir) {
		cir.setReturnValue(value);
	}

	@Inject(method = "clampVertical", at = @At("HEAD"), cancellable = true)
	private static void noborder$skipVerticalClamp(double value, CallbackInfoReturnable<Double> cir) {
		cir.setReturnValue(value);
	}

	@Inject(
		method = "onPlayerMove",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/network/NetworkThreadUtils;forceMainThread(Lnet/minecraft/network/packet/Packet;Lnet/minecraft/network/listener/PacketListener;Lnet/minecraft/server/world/ServerWorld;)V",
			shift = At.Shift.AFTER
		),
		cancellable = true
	)
	private void noborder$hardBypassOuterPlayerMove(PlayerMoveC2SPacket packet, CallbackInfo ci) {
		double targetX = packet.getX(this.player.getX());
		double targetY = packet.getY(this.player.getY());
		double targetZ = packet.getZ(this.player.getZ());
		float targetYaw = MathHelper.wrapDegrees(packet.getYaw(this.player.getYaw()));
		float targetPitch = MathHelper.wrapDegrees(packet.getPitch(this.player.getPitch()));

		boolean outerNow = noborder$isOuter(this.player.getX(), this.player.getZ());
		boolean outerTarget = noborder$isOuter(targetX, targetZ);
		if (!outerNow && !outerTarget) {
			return;
		}

		if (!Double.isFinite(targetX) || !Double.isFinite(targetY) || !Double.isFinite(targetZ) || !Float.isFinite(targetYaw) || !Float.isFinite(targetPitch)) {
			return;
		}

		if (this.player.hasVehicle()) {
			this.player.setAngles(targetYaw, targetPitch);
			ci.cancel();
			return;
		}

		this.player.updatePositionAndAngles(targetX, targetY, targetZ, targetYaw, targetPitch);
		this.player.setOnGround(packet.isOnGround());
		this.player.getWorld().getChunkManager().updatePosition(this.player);
		this.syncWithPlayerPosition();
		ci.cancel();
	}

	@Inject(
		method = "onVehicleMove",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/network/NetworkThreadUtils;forceMainThread(Lnet/minecraft/network/packet/Packet;Lnet/minecraft/network/listener/PacketListener;Lnet/minecraft/server/world/ServerWorld;)V",
			shift = At.Shift.AFTER
		),
		cancellable = true
	)
	private void noborder$hardBypassOuterVehicleMove(VehicleMoveC2SPacket packet, CallbackInfo ci) {
		Entity rootVehicle = this.player.getRootVehicle();
		if (rootVehicle == this.player || rootVehicle.getControllingPassenger() != this.player) {
			return;
		}

		Vec3d targetPos = packet.position();
		double targetX = targetPos.getX();
		double targetY = targetPos.getY();
		double targetZ = targetPos.getZ();

		boolean outerNow = noborder$isOuter(this.player.getX(), this.player.getZ());
		boolean outerVehicle = noborder$isOuter(rootVehicle.getX(), rootVehicle.getZ());
		boolean outerTarget = noborder$isOuter(targetX, targetZ);
		if (!outerNow && !outerVehicle && !outerTarget) {
			return;
		}

		if (!Double.isFinite(targetX) || !Double.isFinite(targetY) || !Double.isFinite(targetZ)) {
			return;
		}

		float targetYaw = MathHelper.wrapDegrees(packet.yaw());
		float targetPitch = MathHelper.wrapDegrees(packet.pitch());
		rootVehicle.updatePositionAndAngles(targetX, targetY, targetZ, targetYaw, targetPitch);
		rootVehicle.setOnGround(packet.onGround());
		this.player.getWorld().getChunkManager().updatePosition(this.player);
		this.syncWithPlayerPosition();
		ci.cancel();
	}

	@Redirect(
		method = "requestTeleport(Lnet/minecraft/entity/player/PlayerPosition;Ljava/util/Set;)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/server/network/ServerPlayerEntity;setPosition(Lnet/minecraft/entity/player/PlayerPosition;Ljava/util/Set;)V"
		)
	)
	private void noborder$applyOuterTeleportWithoutSetPosition(ServerPlayerEntity serverPlayer, PlayerPosition position, Set<PositionFlag> flags) {
		PlayerPosition resolved = PlayerPosition.apply(PlayerPosition.fromEntity(serverPlayer), position, flags);
		Vec3d targetPos = resolved.position();
		if (!noborder$isOuter(targetPos.getX(), targetPos.getZ())) {
			serverPlayer.setPosition(position, flags);
			return;
		}

		serverPlayer.refreshPositionAndAngles(
			targetPos.getX(),
			targetPos.getY(),
			targetPos.getZ(),
			resolved.yaw(),
			resolved.pitch()
		);
		serverPlayer.setVelocity(resolved.deltaMovement());
	}
}
