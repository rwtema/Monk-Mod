package com.rwtema.monkmod.advancements.criteria;

import com.rwtema.monkmod.MonkManager;
import com.rwtema.monkmod.advancements.MonkRequirement;
import com.rwtema.monkmod.data.MonkData;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.event.entity.player.SleepingLocationCheckEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MonkRequirementBedrockSleep extends MonkRequirement {
	public MonkRequirementBedrockSleep() {
		super("bedrock_sleep", -1);
	}

	@SubscribeEvent
	public void rightClickBedrock(PlayerInteractEvent.RightClickBlock event) {
		World world = event.getWorld();
		BlockPos pos = event.getPos();
		if (world.provider.canRespawnHere() && world.getBiome(pos) != Biomes.HELL) {
			EntityPlayer player = event.getEntityPlayer();
			MonkData monkData = MonkManager.get(player);
			if (monkData.getLevel() == (this.levelToGrant - 1)) {
				if (!checkPos(world, pos)) return;

				event.setCancellationResult(EnumActionResult.SUCCESS);
				event.setCanceled(true);
				if (world.isRemote) {
					return;
				}

				player.trySleep(pos.up());
			}
		}
	}

	private boolean checkPos(World world, BlockPos pos) {
		IBlockState state = world.getBlockState(pos);
		if (state.getBlock() != Blocks.BEDROCK || !world.isAirBlock(pos.up())) {
			return false;
		}

		for (EnumFacing horizontal : EnumFacing.HORIZONTALS) {
			BlockPos offset = pos.offset(horizontal);
			if (world.getBlockState(offset).getBlock() == Blocks.BEDROCK && world.isAirBlock(offset.up())) {
				return true;
			}
		}
		return false;
	}

	@SubscribeEvent
	public void awaken(PlayerWakeUpEvent event) {
		EntityPlayer entityPlayer = event.getEntityPlayer();
		if (entityPlayer instanceof EntityPlayerMP) {
			MonkData monkData = MonkManager.get(entityPlayer);
			if (monkData.getLevel() == (this.levelToGrant - 1)) {
				if (checkPos(entityPlayer.world, entityPlayer.bedLocation.down())) {
					grantLevel((EntityPlayerMP) entityPlayer);
				}
			}
		}
	}

	@SubscribeEvent
	public void continueSleep(SleepingLocationCheckEvent event) {
		EntityPlayer entityPlayer = event.getEntityPlayer();
		MonkData monkData = MonkManager.get(entityPlayer);
		if (monkData.getLevel() == (this.levelToGrant - 1)) {
			if (checkPos(entityPlayer.world, event.getSleepingLocation().down())) {
				event.setResult(Event.Result.ALLOW);
			}
		}
	}

}
