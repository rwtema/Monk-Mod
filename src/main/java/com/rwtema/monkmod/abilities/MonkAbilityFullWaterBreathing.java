package com.rwtema.monkmod.abilities;

import net.minecraft.entity.player.EntityPlayerMP;

public class MonkAbilityFullWaterBreathing extends MonkAbility {
	public MonkAbilityFullWaterBreathing() {
		super("water_breathing_full");
	}

	@Override
	public void tickServer(EntityPlayerMP player) {
		player.setAir(300);
	}
}
