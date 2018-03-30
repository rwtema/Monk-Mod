package com.rwtema.monkmod;

import com.rwtema.monkmod.abilities.MonkAbility;
import com.rwtema.monkmod.abilities.MonkAbilityAttribute;
import com.rwtema.monkmod.data.MonkData;
import com.rwtema.monkmod.levels.MonkLevelManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashSet;
import java.util.Map;

public class MonkManager {
	private final static HashSet<EntityPlayer> dirtyPlayers = new HashSet<EntityPlayer>();

	@SubscribeEvent
	public static void registerCap(AttachCapabilitiesEvent<Entity> playerAttachCapabilitiesEvent) {
		if (playerAttachCapabilitiesEvent.getObject() instanceof EntityPlayer)
			playerAttachCapabilitiesEvent.addCapability(MonkData.LOCATION, new MonkData());
	}

	public static void markDirty(EntityPlayerMP playerMP) {
		dirtyPlayers.add(playerMP);
	}

	public static void serverTick(TickEvent.ServerTickEvent event) {
		if (!dirtyPlayers.isEmpty()) {
			for (EntityPlayer dirtyPlayer : dirtyPlayers) {

			}
			dirtyPlayers.clear();
		}
	}

	@SubscribeEvent
	public static void tick(TickEvent.PlayerTickEvent event) {
		if (event.player.world.isRemote) return;
		MonkData monkData = get(event.player);

		Map<MonkAbility, Integer> abilities = MonkLevelManager.getAbilities(monkData.getLevel());
		abilities.forEach(
				(ability, level) -> ability.tickServer(event.player, level)
		);


		AbstractAttributeMap attributeMap = event.player.getAttributeMap();
		for (IAttributeInstance attributeInstance : attributeMap.getAllAttributes()) {
			MonkAbilityAttribute.uuids.forEach(
					(uuid, monkAbilityAttribute) -> {
						if (attributeInstance.getModifier(uuid) != null
								&& !abilities.containsKey(monkAbilityAttribute)) {
							attributeInstance.removeModifier(uuid);
						}
					}
			);
		}

	}

	public static MonkData get(EntityPlayer player) {
		return player.getCapability(MonkData.MONKLEVELDATA, null);
	}

	public static int getAbilityLevel(EntityPlayer player, MonkAbility ability) {
		return MonkLevelManager.getAbilities( get(player).getLevel()).getOrDefault(ability, -1);
	}

	public static boolean hasAbility(EntityPlayer player, MonkAbility ability) {
		return getAbilityLevel(player, ability) >= 0;
	}
}
