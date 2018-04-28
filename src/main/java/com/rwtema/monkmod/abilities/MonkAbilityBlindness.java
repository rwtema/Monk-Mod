package com.rwtema.monkmod.abilities;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;

public class MonkAbilityBlindness extends MonkAbility {
	public MonkAbilityBlindness() {
		super("blindness");
	}

	@Override
	public void tickServer(EntityPlayerMP player) {
		player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 20 * 5));
		super.tickServer(player);
	}
}
