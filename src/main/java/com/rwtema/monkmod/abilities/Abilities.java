package com.rwtema.monkmod.abilities;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;

public class Abilities {
	public static final MonkAbilityAttribute STRENGTH = new MonkAbilityAttribute("punch", SharedMonsterAttributes.ATTACK_DAMAGE, new double[]{
			1, 2, 4, 8, 16, 32
	}, 0) {

		@Override
		public boolean canApply(EntityPlayer player) {
			return isUnarmed(player);
		}
	};

	public static final MonkAbilityAttribute HEALTH = new MonkAbilityAttribute("health", SharedMonsterAttributes.MOVEMENT_SPEED, new double[]{1.1, 1.2, 1.5, 2}, 1) {

		@Override
		public boolean canApply(EntityPlayer player) {
			return true;
		}
	};

	public static final MonkAbilityAttribute SPEED = new MonkAbilityAttribute("swift", SharedMonsterAttributes.MOVEMENT_SPEED, new double[]{1.1, 1.2, 1.4, 1.6, 2, 2.25}, 1) {
		@Override
		public boolean canApply(EntityPlayer player) {
			return isUnarmored(player);
		}
	};

	public static final MonkAbilityAttribute ARMOR = new MonkAbilityAttribute("hardskin", SharedMonsterAttributes.ARMOR, new double[]{
			4, 7, 11, 15, 20
	}, 0) {
		@Override
		public boolean canApply(EntityPlayer player) {
			return isUnarmored(player);
		}
	};

	public static final MonkAbilityAttribute ARMOR_TOUGH = new MonkAbilityAttribute("strong_skin", SharedMonsterAttributes.ARMOR, new double[]{
			2, 4, 8
	}, 0) {
		@Override
		public boolean canApply(EntityPlayer player) {
			return isUnarmored(player);
		}
	};
}
