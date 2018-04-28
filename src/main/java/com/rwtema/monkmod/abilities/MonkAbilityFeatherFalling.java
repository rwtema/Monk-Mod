package com.rwtema.monkmod.abilities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;

public class MonkAbilityFeatherFalling extends MonkAbilityProtection {

	private final float aFloat;

	public MonkAbilityFeatherFalling(float protection) {
		super("feather_fall");
		aFloat = protection;
	}

	@Override
	public float getAbsorbtion(DamageSource source, EntityPlayer player) {
		return aFloat;
	}

	@Override
	public boolean canHandle(EntityPlayer player, DamageSource source) {
		return source == DamageSource.FALL;
	}
}
