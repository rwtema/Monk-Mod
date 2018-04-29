package com.rwtema.monkmod.abilities;

import com.rwtema.monkmod.MonkManager;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.WeakHashMap;

public class MonkAbilityFly extends MonkAbility {
	WeakHashMap<EntityPlayerMP, Boolean> wasArmored = new WeakHashMap<>();

	public MonkAbilityFly() {
		super("fly");
	}

	@Override
	public void tickServer(EntityPlayerMP player) {

		PlayerCapabilities capabilities = player.capabilities;
		boolean wasArmored = this.wasArmored.get(player) == Boolean.TRUE;

		boolean armored = !isUnarmored(player);
		this.wasArmored.put(player, armored);
		if (capabilities.isCreativeMode) return;

		if (armored) {
			if (!wasArmored) {
				capabilities.allowFlying = false;
				capabilities.isFlying = false;
				player.sendPlayerAbilities();
			}
		} else {

			if (!capabilities.allowFlying) {
				capabilities.allowFlying = true;
				player.sendPlayerAbilities();
			}
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
