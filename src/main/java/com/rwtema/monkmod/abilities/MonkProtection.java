package com.rwtema.monkmod.abilities;

import com.rwtema.monkmod.MonkManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public abstract class MonkProtection extends MonkAbility {

	public MonkProtection(String name) {
		super(name);
	}

	public MonkProtection(String name, int maxlevel) {
		super(name, maxlevel);
	}

	@SubscribeEvent
	public void onHurt(LivingHurtEvent event) {
		if (!(event.getEntityLiving() instanceof EntityPlayer)) return;

		EntityPlayer player = (EntityPlayer) event.getEntityLiving();
		int abilityLevel = MonkManager.getAbilityLevel(player, this);
		if (abilityLevel == -1) return;
		DamageSource source = event.getSource();
		if (source.isDamageAbsolute()) return;

		if (!canHandle(player, source)) return;
		float t = getAbsorbtion(source, player, abilityLevel);

		event.setAmount(event.getAmount() * t);


	}

	public abstract float getAbsorbtion(DamageSource source, EntityPlayer player, int abilityLevel);

	public abstract boolean canHandle(EntityPlayer player, DamageSource source);

}
