package com.rwtema.monkmod.abilities;

import net.minecraft.entity.player.EntityPlayerMP;

import javax.annotation.Nonnull;

public class MonkAbilityWater extends MonkAbility {

	public MonkAbilityWater() {
		super("water_breathing_partial");
	}

	@Override
	public void tickServer(@Nonnull EntityPlayerMP player) {
		final int air = player.getAir();
		if (air < 300) {
			int t = air % 30;
			int v = (int) ((player.world.getTotalWorldTime()) % 77);
			if (t == 15 && v != 0) {
				player.setAir(air + 2);
			}
		}
	}
}
