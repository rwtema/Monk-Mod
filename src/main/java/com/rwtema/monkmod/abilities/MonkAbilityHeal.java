package com.rwtema.monkmod.abilities;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.PotionEffect;

public class MonkAbilityHeal extends MonkAbility {
	public MonkAbilityHeal(String name) {
		super(name);
	}

	@Override
	public void tickServer(EntityPlayerMP player, int level) {
		for (PotionEffect potionEffect : player.getActivePotionEffects()) {
			if (potionEffect.getPotion().isBadEffect()) {
				player.removePotionEffect(potionEffect.getPotion());
			}
		}
	}
}
