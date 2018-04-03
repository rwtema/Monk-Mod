package com.rwtema.monkmod.abilities;

import com.rwtema.monkmod.MonkManager;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MonkAbilityFly extends MonkAbility {
	public MonkAbilityFly(String name) {
		super(name);
	}

	@Override
	public void tickServer(EntityPlayerMP player, int level) {
		PlayerCapabilities capabilities = player.capabilities;
		if (!capabilities.allowFlying) {
			capabilities.allowFlying = true;
			player.sendPlayerAbilities();
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void copyPlayer(PlayerEvent.Clone event) {
		int abilityLevel = MonkManager.getAbilityLevel(event.getEntityPlayer(), this);
		if (abilityLevel != -1) {
			if (!event.getEntityPlayer().capabilities.isCreativeMode) {
				event.getEntityPlayer().capabilities.allowFlying = false;
			}
		}
	}
}
