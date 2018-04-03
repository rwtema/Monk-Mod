package com.rwtema.monkmod.abilities;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;

class MonkProtectionFire extends MonkProtection {
	public MonkProtectionFire() {
		super("fire", 2);
	}

	@Override
	public float getAbsorbtion(DamageSource source, EntityPlayer player, int abilityLevel) {
		if (!player.isBurning()) {
			if (player.world.isMaterialInBB(player.getEntityBoundingBox(), Material.LAVA)) {
				player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 20 * 4, 2));
				player.extinguish();
				return 0;
			}
		}

		switch (abilityLevel) {
			case 0:
				return 0.5F;
			default:
			case 1:
				return 0;
		}
	}

	@Override
	public boolean canHandle(EntityPlayer player, DamageSource source) {
		return source.isFireDamage();
	}
}
