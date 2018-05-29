package com.rwtema.monkmod.abilities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;

import java.text.NumberFormat;
import java.util.Locale;

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


	@Override
	protected String[] args() {
		return new String[]{NumberFormat.getPercentInstance(Locale.UK).format(1 - aFloat)};
	}
}
