package com.rwtema.monkmod.advancements.criteria;

import com.rwtema.monkmod.MonkManager;
import com.rwtema.monkmod.advancements.MonkRequirement;
import com.rwtema.monkmod.data.MonkData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public abstract class MonkRequirementKill extends MonkRequirement {

	private final int NUM_KILLS;

	public MonkRequirementKill(int level, int num_kills) {
		super(level);
		NUM_KILLS = num_kills;
	}

	@SubscribeEvent
	public void onEntityDeath(LivingDeathEvent event){
		if (!event.getEntity().world.isRemote && isValidEntity(event)) {
			Entity trueSource = event.getSource().getTrueSource();
			if(trueSource instanceof EntityPlayerMP){
				EntityPlayerMP player = (EntityPlayerMP) trueSource;
				MonkData monkData = MonkManager.get(player);
				if (monkData.getLevel() == (this.levelToGrant - 1)) {
					if (player.getHeldItemMainhand().isEmpty()) {

						if (monkData.increase(1, NUM_KILLS)) {
							grantLevel(player);
						}
					}
				}
			}
		}
	}

	protected abstract boolean isValidEntity(LivingDeathEvent event);
}
