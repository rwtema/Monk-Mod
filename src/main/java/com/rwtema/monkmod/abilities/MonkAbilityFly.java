package com.rwtema.monkmod.abilities;

import com.rwtema.monkmod.MonkManager;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MonkAbilityFly extends MonkAbility {
	public MonkAbilityFly() {
		super("fly");
	}

	@Override
	public void tickServer(EntityPlayerMP player) {
		PlayerCapabilities capabilities = player.capabilities;
		if (!capabilities.allowFlying) {
			capabilities.allowFlying = true;
			player.sendPlayerAbilities();
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void copyPlayer(PlayerEvent.Clone event) {
		if (!MonkManager.getAbilityLevel(event.getEntityPlayer(), this)) return;

		if (!event.getEntityPlayer().capabilities.isCreativeMode) {
			event.getEntityPlayer().capabilities.allowFlying = false;
		}

	}
}
