package com.rwtema.monkmod;

import com.rwtema.monkmod.abilities.MonkAbility;
import com.rwtema.monkmod.abilities.MonkAbilityAttribute;
import com.rwtema.monkmod.data.MonkData;
import com.rwtema.monkmod.levels.MonkLevelManager;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class MonkManager {
	@SubscribeEvent
	public static void registerCap(AttachCapabilitiesEvent<EntityPlayer> playerAttachCapabilitiesEvent) {
		playerAttachCapabilitiesEvent.addCapability(MonkData.LOCATION, new MonkData());
	}

	private final static HashSet<EntityPlayer> dirtyPlayers = new HashSet<EntityPlayer>();

	public static void markDirty(EntityPlayerMP playerMP){
		dirtyPlayers.add(playerMP);
	}

	public static void serverTick(TickEvent.ServerTickEvent event){
		if(!dirtyPlayers.isEmpty()){
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

	public int getLevel(EntityPlayer player, MonkAbility ability) {
		return 0;
	}

	public boolean hasAbility(EntityPlayer player, MonkAbility ability) {
		return getLevel(player, ability) > 0;
	}
}
