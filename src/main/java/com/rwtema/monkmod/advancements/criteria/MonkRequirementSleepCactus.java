package com.rwtema.monkmod.advancements.criteria;

import com.rwtema.monkmod.MonkManager;
import com.rwtema.monkmod.data.MonkData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;

public class MonkRequirementSleepCactus extends MonkRequirementSleep {
	public MonkRequirementSleepCactus() {
		super("sleep_cactus", -1);
	}

	@Override
	protected boolean checkPos(World world, @Nonnull BlockPos pos) {
		return world.getBlockState(pos).getBlock() == Blocks.CACTUS && world.isAirBlock(pos.up());
	}

	@SubscribeEvent
	public void sleepOnCactus(LivingAttackEvent event) {
		if (event.getSource() == DamageSource.CACTUS && event.getEntityLiving() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.getEntityLiving();
			if (player.isPlayerSleeping() && player.bedLocation != null) {
				if (player.world.rand.nextFloat() < 0.0125 && player.isPlayerFullyAsleep()) return;

				MonkData monkData = MonkManager.get(player);
				if (monkData.getLevel() == (this.levelToGrant - 1)) {
					if (checkPos(player.world, player.bedLocation.down())) {
						event.setCanceled(true);
					}
				}
			}

		}
	}
}
