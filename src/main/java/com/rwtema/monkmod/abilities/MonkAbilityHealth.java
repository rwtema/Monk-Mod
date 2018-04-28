package com.rwtema.monkmod.abilities;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;

public class MonkAbilityHealth extends MonkAbilityAttribute {

	public MonkAbilityHealth(double multiplier) {
		super("health", SharedMonsterAttributes.MAX_HEALTH, multiplier, 1);
	}

	@Override
	public boolean canApply(EntityPlayer player) {
		return true;
	}
}
