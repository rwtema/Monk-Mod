package com.rwtema.monkmod.abilities;

import com.rwtema.monkmod.MonkManager;
import com.rwtema.monkmod.MonkTextures;
import com.rwtema.monkmod.network.MessageBlink;
import com.rwtema.monkmod.network.MonkNetwork;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class MonkAbilityBlink extends MonkAbility {
	private boolean wasPressed = false;
	private int cooldown = 0;

	public MonkAbilityBlink() {
		super("blink");
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent event) {
		if (Minecraft.getMinecraft().isGamePaused()) {
			return;
		}

		if (cooldown > 0) {
			cooldown--;
		}

		Minecraft minecraft = Minecraft.getMinecraft();
		EntityPlayerSP player = minecraft.player;
		if (player == null || minecraft.currentScreen != null) return;
		if (wasPressed) {
			if (!MonkManager.getAbilityLevel(player, this)) return;

			Entity viewEntity = minecraft.player;
			if (viewEntity == null) return;

			Vec3d vec3d = viewEntity.getPositionEyes((float) 0);
			Vec3d vec3d1 = viewEntity.getLook((float) 0);
			int range = 16;
			Vec3d vec3d2 = vec3d.addVector(vec3d1.x * (double) range, vec3d1.y * (double) range, vec3d1.z * (double) range);
			Vec3d pointPos = vec3d2;
			World world = viewEntity.world;
			RayTraceResult rayTraceResult = world.rayTraceBlocks(vec3d, vec3d2, false, false, true);

			AxisAlignedBB entityBoundingBox = player.getEntityBoundingBox().offset(-player.posX, -player.posY, -player.posZ);

			AxisAlignedBB resultBounds = null;

			if (rayTraceResult != null && rayTraceResult.hitVec != null) {


				pointPos = rayTraceResult.hitVec;
				BlockPos pos = rayTraceResult.getBlockPos();

				if (rayTraceResult.sideHit.getAxis() != EnumFacing.Axis.Y) {
					AxisAlignedBB collisionBoundingBox = world.getBlockState(pos).getCollisionBoundingBox(world, pos);
					if (collisionBoundingBox != null) {
						collisionBoundingBox = collisionBoundingBox.offset(pos);
						if ((rayTraceResult.hitVec.y) > ((collisionBoundingBox.maxY + collisionBoundingBox.minY) / 2)) {
							if (world.isAirBlock(pos.up()) && world.isAirBlock(pos.up().offset(rayTraceResult.sideHit))) {
								AxisAlignedBB offset = entityBoundingBox.offset(pos.getX() + 0.5, collisionBoundingBox.maxY, pos.getZ() + 0.5);
								if (world.getCollisionBoxes(player, offset).isEmpty()) {
									resultBounds = offset;
								}
							}
						}
					}
				}

				if (resultBounds == null) {
					BlockPos p = pos;
					AxisAlignedBB offsetBounds = entityBoundingBox.offset(p.getX() + 0.5, p.getY(), p.getZ() + 0.5);
					if (world.getCollisionBoxes(player, offsetBounds).isEmpty()) {
						resultBounds = offsetBounds;
					} else {
						p = pos.offset(rayTraceResult.sideHit);
						offsetBounds = entityBoundingBox.offset(p.getX() + 0.5, p.getY(), p.getZ() + 0.5);
						if (world.getCollisionBoxes(player, offsetBounds).isEmpty()) {
							resultBounds = offsetBounds;
						} else {
							offsetBounds = offsetBounds.offset(0, -1, 0);
							if (world.getCollisionBoxes(player, offsetBounds).isEmpty()) {
								resultBounds = offsetBounds;
							}
						}
					}
				}
			}

			if (resultBounds == null) {
				AxisAlignedBB offsetBounds = entityBoundingBox.offset(pointPos);
				if (world.getCollisionBoxes(player, offsetBounds).isEmpty()) {
					resultBounds = offsetBounds;
				} else {
					BlockPos p = new BlockPos(pointPos);
					offsetBounds = entityBoundingBox.offset(p.getX() + 0.5, p.getY(), p.getZ() + 0.5);
					if (world.getCollisionBoxes(player, offsetBounds).isEmpty()) {
						resultBounds = offsetBounds;
					} else {
						offsetBounds = offsetBounds.offset(0, -1, 0);
						if (world.getCollisionBoxes(player, offsetBounds).isEmpty()) {
							resultBounds = offsetBounds;
						}
					}
				}
			}


			if (MonkTextures.blink.isKeyDown()) {
				if (resultBounds != null) {
					double r = 204 / 255F;
					double g = 0;
					double b = 250 / 255F;
					world.spawnParticle(EnumParticleTypes.REDSTONE,
							pointPos.x, pointPos.y, pointPos.z,
							r, g, b
					);

					Random rand = world.rand;
					float chance = 1 - (cooldown / 20F);

					double v = 0.2;
					for (int j = 0; j < 4; j++) {
						for (int i = 0; i <= 2; i++) {
							if (world.rand.nextFloat() < chance)
								world.spawnParticle(EnumParticleTypes.REDSTONE,
										(resultBounds.minX + resultBounds.maxX) / 2 + rand.nextGaussian() * v,
										resultBounds.minY + i + rand.nextGaussian() * v,
										(resultBounds.minZ + resultBounds.maxZ) / 2 + rand.nextGaussian() * v,
										r, g, b
								);
						}
					}
					for (int i = 0; i < 10; i++) {
						if (world.rand.nextFloat() < chance)
							world.spawnParticle(EnumParticleTypes.REDSTONE,
									(resultBounds.minX + resultBounds.maxX) / 2, resultBounds.minY + rand.nextFloat() * 2, (resultBounds.minZ + resultBounds.maxZ) / 2,
									r / 2, g / 2, b / 2
							);
					}

				} else {
					world.spawnParticle(EnumParticleTypes.REDSTONE,
							pointPos.x, pointPos.y, pointPos.z,
							0.5, 0, 0
					);
				}
			} else {
				if (cooldown != 0) return;
				if (event != null && resultBounds != null) {
					Vec3d vec3d3 = new Vec3d((resultBounds.minX + resultBounds.maxX) / 2, resultBounds.minY, (resultBounds.minZ + resultBounds.maxZ) / 2);
					MonkNetwork.net.sendToServer(new MessageBlink(vec3d3));
					cooldown = 40;
				}
			}
		}
		wasPressed = MonkTextures.blink.isKeyDown();
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void runData(InputEvent.KeyInputEvent event) {

//		wasPressed = MonkTextures.blink.isKeyDown();
	}
}
