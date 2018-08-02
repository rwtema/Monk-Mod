package com.rwtema.monkmod.advancements.criteria;

import com.rwtema.monkmod.MonkManager;
import com.rwtema.monkmod.advancements.MonkRequirement;
import com.rwtema.monkmod.data.MonkData;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;

public abstract class MonkRequirementDeath extends MonkRequirement {
	public MonkRequirementDeath(String name) {
		super(name, -1);
	}

	@SubscribeEvent
	public void onDeathAvoid(@Nonnull LivingDeathEvent event) {
		if (event.getEntityLiving() instanceof EntityPlayerMP) {
			EntityPlayerMP player = (EntityPlayerMP) event.getEntityLiving();
			MonkData monkData = MonkManager.get(player);
			if (monkData.getLevel() == (this.levelToGrant - 1)) {
				if (isValidSourceOfDeath(event)) {
					event.setCanceled(true);
					player.setHealth(1);
					onDeathAvoid(player, monkData);
				}
			}
		}

	}

	protected void onDeathAvoid(EntityPlayerMP player, MonkData monkData) {
		grantLevel(player);
	}

	protected abstract boolean isValidSourceOfDeath(LivingDeathEvent event);
}
