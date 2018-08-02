package com.rwtema.monkmod.abilities;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;

import javax.annotation.Nonnull;

public class MonkAbilityArmor extends MonkAbilityAttribute {
	public MonkAbilityArmor(double increase) {
		super("armor", SharedMonsterAttributes.ARMOR, increase, 0);
	}

	@Override
	public boolean canApply(@Nonnull EntityPlayer player) {
		return isUnarmored(player);
	}
}
