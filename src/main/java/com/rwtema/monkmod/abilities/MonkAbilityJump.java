package com.rwtema.monkmod.abilities;

import com.rwtema.monkmod.MonkManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.text.NumberFormat;
import java.util.Locale;

public class MonkAbilityJump extends MonkAbility {

	private final double jumpLevel;

	public MonkAbilityJump(double jumpLevel) {
		super("jump");
		this.jumpLevel = jumpLevel;
	}

	@SubscribeEvent
	public void onFall(LivingFallEvent event) {
		if (!(event.getEntityLiving() instanceof EntityPlayer)) return;

		EntityPlayer player = (EntityPlayer) event.getEntityLiving();
		if (!MonkManager.getAbilityLevel(player, this)) return;

		event.setDistance(event.getDistance() - 3);
	}


	@SubscribeEvent
	public void onJump(LivingEvent.LivingJumpEvent event) {
		if (!(event.getEntityLiving() instanceof EntityPlayer)) return;

		EntityPlayer player = (EntityPlayer) event.getEntityLiving();
		if (!MonkManager.getAbilityLevel(player, this)) return;


		player.motionY *= jumpLevel;
	}

	@Override
	protected String[] args() {
		return new String[]{NumberFormat.getPercentInstance(Locale.UK).format(jumpLevel - 1)};
	}
}
