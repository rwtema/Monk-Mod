package com.rwtema.monkmod.abilities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;

import javax.annotation.Nonnull;
import java.text.NumberFormat;
import java.util.Locale;

public class MonkAbilityExplosionProof extends MonkAbilityProtection {

	private final float aFloat;

	public MonkAbilityExplosionProof(float protection) {
		super("explosion_resistance");
		aFloat = protection;
	}

	@Override
	public float getAbsorbtion(DamageSource source, EntityPlayer player) {
		return aFloat;
	}

	@Override
	public boolean canHandle(EntityPlayer player, @Nonnull DamageSource source) {
		return source.isExplosion();
	}


	@Nonnull
	@Override
	protected String[] args() {
		return new String[]{NumberFormat.getPercentInstance(Locale.UK).format(aFloat)};
	}
}
