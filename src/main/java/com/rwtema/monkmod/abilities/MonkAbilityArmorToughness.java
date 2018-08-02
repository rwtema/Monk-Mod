package com.rwtema.monkmod.abilities;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;

import javax.annotation.Nonnull;

public class MonkAbilityArmorToughness extends MonkAbilityAttribute {
	public MonkAbilityArmorToughness(double increase) {
		super("armor_toughness", SharedMonsterAttributes.ARMOR_TOUGHNESS, increase, 0);
	}

	@Override
	public boolean canApply(@Nonnull EntityPlayer player) {
		return isUnarmored(player);
	}
}
