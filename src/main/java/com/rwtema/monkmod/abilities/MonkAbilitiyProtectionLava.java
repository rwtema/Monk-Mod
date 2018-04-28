package com.rwtema.monkmod.abilities;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;

public class MonkAbilitiyProtectionLava extends MonkAbilityProtection {
	public MonkAbilitiyProtectionLava() {
		super("lava_protection");
	}

	@Override
	public float getAbsorbtion(DamageSource source, EntityPlayer player) {
		if (!player.isBurning()) {
			if (player.world.isMaterialInBB(player.getEntityBoundingBox(), Material.LAVA)) {
				player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 20 * 4, 2));
				player.extinguish();
				return 0;
			}
		}
		return 1;
	}

	@Override
	public boolean canHandle(EntityPlayer player, DamageSource source) {
		return source.isFireDamage();
	}
}
