package com.rwtema.monkmod.advancements.criteria;

import com.rwtema.monkmod.MonkManager;
import com.rwtema.monkmod.abilities.MonkAbility;
import com.rwtema.monkmod.advancements.MonkRequirement;
import com.rwtema.monkmod.data.MonkData;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class MonkRequirementWalk extends MonkRequirement {
	private final static int STEP_PER_BLOCK = 64;
	public final int numSteps;

	public MonkRequirementWalk(int level, double numSteps) {
		super(level);
		this.numSteps = (int) (numSteps * STEP_PER_BLOCK);
	}

	@SubscribeEvent
	public void walk(TickEvent.PlayerTickEvent event) {
		if (event.phase == TickEvent.Phase.START) {
			return;
		}

		if (event.player instanceof EntityPlayerMP) {
			MonkData monkData = MonkManager.get(event.player);
			if (monkData.getLevel() == (this.levelToGrant - 1)) {
				EntityPlayerMP player = (EntityPlayerMP) event.player;
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
	}

}
