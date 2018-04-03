package com.rwtema.monkmod.abilities;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;

public class MonkAbilityBlindness extends MonkAbility {
	public MonkAbilityBlindness(String name, int maxlevel) {
		super(name, maxlevel);
	}

	@Override
	public void tickServer(EntityPlayerMP player, int level) {
		if(level == 0){
			player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 20 * 5));
		}
		super.tickServer(player, level);
	}
}
