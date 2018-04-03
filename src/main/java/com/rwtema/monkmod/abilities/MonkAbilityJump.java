package com.rwtema.monkmod.abilities;

import com.rwtema.monkmod.MonkManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MonkAbilityJump extends MonkAbility {
	public MonkAbilityJump(String name) {
		super(name, 2);
	}

	public MonkAbilityJump(String name, int maxlevel) {
		super(name, maxlevel);
	}

	@SubscribeEvent
	public void onJump (LivingEvent.LivingJumpEvent event){
		if (!(event.getEntityLiving() instanceof EntityPlayer)) return;

		EntityPlayer player = (EntityPlayer) event.getEntityLiving();
		int abilityLevel = MonkManager.getAbilityLevel(player, this);
		if (abilityLevel == -1) return;


		if(abilityLevel == 1){
			player.motionY *= 1.5;
		}else{
			player.motionY *= 1.25;
		}
	}
}
