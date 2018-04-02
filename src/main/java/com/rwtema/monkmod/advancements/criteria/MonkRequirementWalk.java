package com.rwtema.monkmod.advancements.criteria;

import com.rwtema.monkmod.abilities.MonkAbility;
import com.rwtema.monkmod.data.MonkData;
import net.minecraft.entity.player.EntityPlayerMP;

public class MonkRequirementWalk extends MonkRequirementTick {
	private final static int STEP_PER_BLOCK = 64;
	public final int numSteps;

	public MonkRequirementWalk(int level, double numSteps) {
		super(level);
		this.numSteps = (int) (numSteps * STEP_PER_BLOCK);
	}

	@Override
	protected void doTick(EntityPlayerMP player, MonkData monkData) {
		if(!player.world.isDaytime()){
			monkData.setProgress(0);
		}
		if (MonkAbility.isUnarmored(player)) {
			double v = Math.sqrt((player.posX - player.prevPosX) * (player.posX - player.prevPosX) +
					(player.posZ - player.prevPosZ) * (player.posZ - player.prevPosZ));

			int k = (int) (v * STEP_PER_BLOCK);

			monkData.increaseProgress(k);
			if (monkData.getProgress() > numSteps) {
				grantLevel(player);
			}
		} else {
			monkData.setProgress(0);
		}
	}
}



