package com.rwtema.monkmod.abilities;

import net.minecraft.entity.player.EntityPlayer;

import java.util.Random;

public class MonkAbilityWater extends MonkAbility {
	public MonkAbilityWater(String name, int maxlevel) {
		super(name, maxlevel);
	}

	@Override
	public void tickServer(EntityPlayer player, int level) {
		final int air = player.getAir();
		Random rand = player.world.rand;
		if (air < 300) {
			if (level == 1) {
				player.setAir(300);
			} else {
				if (air > rand.nextInt(300)) {
					player.setAir(air + 1);
				}
			}
		}
	}
}
