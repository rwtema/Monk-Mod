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

import javax.annotation.Nonnull;

public abstract class MonkRequirementSleep extends MonkRequirement {
	public MonkRequirementSleep(String name, int defaultRequirements) {
		super(name, defaultRequirements);
	}


	@SubscribeEvent
	public void rightClickBedrock(@Nonnull PlayerInteractEvent.RightClickBlock event) {
		World world = event.getWorld();
		BlockPos pos = event.getPos();
		if (!world.provider.canRespawnHere() || world.getBiome(pos) == Biomes.HELL) return;

		EntityPlayer player = event.getEntityPlayer();
		MonkData monkData = MonkManager.get(player);
		if (monkData.getLevel() != (this.levelToGrant - 1)) return;
		if (!checkPos(world, pos)) return;

		event.setCancellationResult(EnumActionResult.SUCCESS);
		event.setCanceled(true);
		if (world.isRemote) return;

		player.trySleep(pos.up());
	}

	protected abstract boolean checkPos(World world, @Nonnull BlockPos pos) ;

	@SubscribeEvent
	public void awaken(@Nonnull PlayerWakeUpEvent event) {
		EntityPlayer entityPlayer = event.getEntityPlayer();

		if (entityPlayer.isPlayerFullyAsleep() && entityPlayer instanceof EntityPlayerMP ) {
			MonkData monkData = MonkManager.get(entityPlayer);
			if (monkData.getLevel() == (this.levelToGrant - 1)) {
				if (checkPos(entityPlayer.world, entityPlayer.bedLocation.down())) {
					grantLevel((EntityPlayerMP) entityPlayer);
				}
			}
		}
	}

	@SubscribeEvent
	public void continueSleep(@Nonnull SleepingLocationCheckEvent event) {
		EntityPlayer entityPlayer = event.getEntityPlayer();
		MonkData monkData = MonkManager.get(entityPlayer);
		if (monkData.getLevel() == (this.levelToGrant - 1)) {
			if (checkPos(entityPlayer.world, event.getSleepingLocation().down())) {
				event.setResult(Event.Result.ALLOW);
			}
		}
	}
}
