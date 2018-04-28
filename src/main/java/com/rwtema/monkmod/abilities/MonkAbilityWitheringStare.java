package com.rwtema.monkmod.abilities;

import com.rwtema.monkmod.advancements.criteria.MonkRequirementStare;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;

public class MonkAbilityWitheringStare extends MonkAbility {
	public MonkAbilityWitheringStare() {
		super("withering_stare");
	}

	@Override
	public void tickServer(EntityPlayerMP player) {
		EntityLiving stareEntity = MonkRequirementStare.getStareEntity(player, EntityLivingBase::isEntityUndead, 20);
		if (stareEntity != null) {
			stareEntity.addPotionEffect(new PotionEffect(new PotionEffect(MobEffects.WITHER, 200)));
			if (stareEntity instanceof EntityWither) {
				if (!((EntityWither) stareEntity).isArmored()) {
					stareEntity.attackEntityFrom(DamageSource.WITHER, 20);
				}
			}
		}
	}
}
