package com.rwtema.monkmod.abilities;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;

public class MonkAbilityStrength extends MonkAbilityAttribute {

	public MonkAbilityStrength(double strengthToAdd) {
		super("strength", SharedMonsterAttributes.ATTACK_DAMAGE, strengthToAdd, 0);
	}

	@Override
	public boolean canApply(EntityPlayer player) {
		return isUnarmed(player);
	}
}
