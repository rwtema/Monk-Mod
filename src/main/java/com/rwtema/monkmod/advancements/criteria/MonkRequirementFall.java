package com.rwtema.monkmod.advancements.criteria;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

public class MonkRequirementFall extends MonkRequirementDeath {
	public MonkRequirementFall(int level) {
		super(level);
	}

	@Override
	protected void onDeathAvoid(EntityPlayerMP player) {
		super.onDeathAvoid(player);
	}

	@Override
	protected boolean isValidSourceOfDeath(LivingDeathEvent event) {
		if (event.getSource() == DamageSource.OUT_OF_WORLD) {
			return event.getEntity().posY < 0;
		}
		return false;
	}
}
