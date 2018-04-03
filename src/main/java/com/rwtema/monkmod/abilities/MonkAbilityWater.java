package com.rwtema.monkmod.abilities;

import net.minecraft.entity.player.EntityPlayerMP;

public class MonkAbilityWater extends MonkAbility {
	public MonkAbilityWater(String name, int maxlevel) {
		super(name, maxlevel);
	}

	@Override
	public void tickServer(EntityPlayerMP player, int level) {
		final int air = player.getAir();
		if (air < 300) {
			if (level == 1) {
				player.setAir(300);
			} else {
				int t = air % 30;
				int v = (int) ((player.world.getTotalWorldTime()) % 77);
				if (t == 15 && v != 0) {
					player.setAir(air + 2);
				}
			}
		}
	}
}
