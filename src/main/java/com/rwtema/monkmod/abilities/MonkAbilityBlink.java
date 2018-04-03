package com.rwtema.monkmod.abilities;

import com.rwtema.monkmod.MonkManager;
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
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Random;

public class MonkAbilityBlink extends MonkAbility {
	boolean buttonPressed = false;

	public MonkAbilityBlink(String name) {
		super(name);
	}

	public Vec3d getOffsetBB(World world, BlockPos pos, Vec3d target, AxisAlignedBB bounds, EnumFacing side) {

		switch (side) {
			case DOWN:
				return new Vec3d(target.x, pos.getY() - (bounds.maxX - bounds.minY), target.z);
			case UP:
				return new Vec3d(target.x, pos.getY() + 1, target.z);
			case NORTH:
				return new Vec3d(target.x, target.y, pos.getZ() - (bounds.maxZ - bounds.minZ) / 2);
			case SOUTH:
				return new Vec3d(target.x, target.y, pos.getZ() + 1 + (bounds.maxZ - bounds.minZ) / 2);
			case WEST:
				return new Vec3d(pos.getX() - (bounds.maxX - bounds.minX) / 2, target.y, target.z);
			case EAST:
				return new Vec3d(pos.getX() + 1 + (bounds.maxX - bounds.minX) / 2, target.y, target.z);
			default:
				throw new IllegalArgumentException("Illegal Argument: " + side);
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onInput(MouseEvent event) {
		if (event.getButton() == 2) {
			buttonPressed = event.isButtonstate();
			if (!buttonPressed) {
				runData(event);
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent event) {
		if (buttonPressed) {
			runData(null);
		}
	}

	public void runData(@Nullable MouseEvent event) {
		Minecraft minecraft = Minecraft.getMinecraft();
		EntityPlayerSP player = minecraft.player;
		if (player == null || minecraft.currentScreen != null) return;
		if (minecraft.gameSettings.keyBindSprint.isKeyDown() || player.isSprinting()) {
			int abilityLevel = MonkManager.getAbilityLevel(player, this);
			if (abilityLevel == -1) return;

			Entity viewEntity = minecraft.player;
			if (viewEntity == null) return;
			if (event != null)
				event.setCanceled(true);

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


			if (buttonPressed) {
				if (resultBounds != null) {
					double r = 204 / 255F;
					double g = 0;
					double b = 250 / 255F;
					world.spawnParticle(EnumParticleTypes.REDSTONE,
							pointPos.x, pointPos.y, pointPos.z,
							r, g, b
					);

					Random rand = world.rand;

					double v = 0.2;
					for (int j = 0; j < 4; j++) {
						for (int i = 0; i <= 2; i++) {
							world.spawnParticle(EnumParticleTypes.REDSTONE,
									(resultBounds.minX + resultBounds.maxX) / 2 + rand.nextGaussian() * v,
									resultBounds.minY + i + rand.nextGaussian() * v,
									(resultBounds.minZ + resultBounds.maxZ) / 2 + rand.nextGaussian() * v,
									r, g, b
							);
						}
					}
					for (int i = 0; i < 10; i++) {

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
				if (event != null && resultBounds != null) {
					Vec3d vec3d3 = new Vec3d((resultBounds.minX + resultBounds.maxX) / 2, resultBounds.minY, (resultBounds.minZ + resultBounds.maxZ) / 2);
					MonkNetwork.net.sendToServer(new MessageBlink(vec3d3));
				}
			}
		}
	}
}
