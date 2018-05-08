package com.rwtema.monkmod.abilities;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.PotionEffect;

public class MonkAbilityHealPotionEffects extends MonkAbility {
	public MonkAbilityHealPotionEffects() {
		super("potion_immunity");
	}

	@Override
	public void tickServer(EntityPlayerMP player) {
		for (PotionEffect potionEffect : player.getActivePotionEffects()) {
			if (potionEffect.getPotion().isBadEffect()) {
				// Remove the first bad effect. If there are more, they will be removed next tick.
				player.removePotionEffect(potionEffect.getPotion());
				return;
			}
		}
	}
}
