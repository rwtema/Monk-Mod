package com.rwtema.monkmod.abilities;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.PlayerCapabilities;

public class MonkAbilityFly extends MonkAbility {
	public MonkAbilityFly(String name) {
		super(name);
	}

	@Override
	public void tickServer(EntityPlayerMP player, int level) {
		PlayerCapabilities capabilities = player.capabilities;
		if (!capabilities.allowFlying) {
			capabilities.allowFlying = true;
			player.sendPlayerAbilities();
		}
	}
}
