package com.rwtema.monkmod.abilities;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;

public class MonkAbilityProtectionFire extends MonkAbilityProtection {

	private final float amount;

	public MonkAbilityProtectionFire(float amount) {
		super("fire_resistance");
		this.amount = amount;
	}

	@Override
	public float getAbsorbtion(DamageSource source, EntityPlayer player) {


		return amount;
	}

	@Override
	public boolean canHandle(EntityPlayer player, DamageSource source) {
		return source.isFireDamage();
	}
}
