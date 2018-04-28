package com.rwtema.monkmod.abilities;

import com.rwtema.monkmod.MonkManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public abstract class MonkAbilityProtection extends MonkAbility {

	public MonkAbilityProtection(String name) {
		super(name);
	}

	@SubscribeEvent
	public void onHurt(LivingHurtEvent event) {
		if (!(event.getEntityLiving() instanceof EntityPlayer)) return;

		EntityPlayer player = (EntityPlayer) event.getEntityLiving();
		if (!MonkManager.getAbilityLevel(player, this)) return;
		DamageSource source = event.getSource();
		if (source.isDamageAbsolute()) return;

		if (!canHandle(player, source)) return;
		float t = getAbsorbtion(source, player);

		event.setAmount(event.getAmount() * t);


	}

	public abstract float getAbsorbtion(DamageSource source, EntityPlayer player);

	public abstract boolean canHandle(EntityPlayer player, DamageSource source);

}
