package com.rwtema.monkmod.network;

import com.rwtema.monkmod.MonkManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageBlink extends MonkNetwork.MessageClientToServer {
	private Vec3d target;

	public MessageBlink() {
	}

	public MessageBlink(Vec3d target) {

		this.target = target;
	}

	@Override
	protected void runServer(MessageContext ctx, EntityPlayerMP player) {
		if (!MonkManager.getAbilityLevel(player, "blink")) {
			return;
		}

		AxisAlignedBB entityBoundingBox = player.getEntityBoundingBox();
		AxisAlignedBB offset = entityBoundingBox
				.offset(target)
				.offset(-player.posX, -player.posY, -player.posZ);
		if (player.world.getCollisionBoxes(player, offset).isEmpty()) {
			if (!player.isPlayerSleeping()) {
				EnderTeleportEvent event = new EnderTeleportEvent(player,
						target.x, target.y, target.z,
						10);
				if (!MinecraftForge.EVENT_BUS.post(event)) {
					if (player.isRiding()) {
						player.dismountRidingEntity();
					}

					player.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1, 1);
					player.setPositionAndUpdate(event.getTargetX(), event.getTargetY(), event.getTargetZ());
					player.fallDistance = 0.0F;
					player.attackEntityFrom(DamageSource.FALL, event.getAttackDamage());
					player.world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, player.getSoundCategory(), (float) 1, (float) 1);
				}
			}
		}

	}

	@Override
	public void fromBytes(ByteBuf buf) {
		target = new Vec3d(buf.readFloat(), buf.readFloat(), buf.readFloat());
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeFloat((float) target.x);
		buf.writeFloat((float) target.y);
		buf.writeFloat((float) target.z);
	}
}
