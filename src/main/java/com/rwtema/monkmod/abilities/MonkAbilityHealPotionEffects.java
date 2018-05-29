package com.rwtema.monkmod.abilities;

import com.google.common.collect.Lists;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.PotionEffect;

import java.util.Collection;

public class MonkAbilityHealPotionEffects extends MonkAbility {
	public MonkAbilityHealPotionEffects() {
		super("potion_immunity");
	}

	@Override
	public void tickServer(EntityPlayerMP player) {
		Collection<PotionEffect> activePotionEffects = Lists.newArrayList(player.getActivePotionEffects());
		for (PotionEffect potionEffect : activePotionEffects) {
			if (potionEffect.getPotion().isBadEffect()) {
				player.removePotionEffect(potionEffect.getPotion());
			}
		}
	}
}
