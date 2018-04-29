package com.rwtema.monkmod;

import com.rwtema.monkmod.abilities.MonkAbility;
import com.rwtema.monkmod.abilities.MonkAbilityAttribute;
import com.rwtema.monkmod.data.MonkData;
import com.rwtema.monkmod.levels.MonkLevelManager;
import com.rwtema.monkmod.network.MessageMonkLevelData;
import com.rwtema.monkmod.network.MonkNetwork;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Set;
import java.util.UUID;

public class MonkManager {

	@SubscribeEvent
	public static void registerCap(AttachCapabilitiesEvent<Entity> playerAttachCapabilitiesEvent) {
		if (playerAttachCapabilitiesEvent.getObject() instanceof EntityPlayer)
			playerAttachCapabilitiesEvent.addCapability(MonkData.LOCATION, new MonkData());
	}

	@SubscribeEvent
	public static void onClone(net.minecraftforge.event.entity.player.PlayerEvent.Clone event) {
		MonkData oldData = get(event.getOriginal());
		MonkData newData = get(event.getEntityPlayer());
		newData.deserializeNBT(oldData.serializeNBT());
	}

	@SubscribeEvent
	public static void onWatch(PlayerEvent.PlayerLoggedInEvent event) {
		EntityPlayerMP player = (EntityPlayerMP) event.player;
		updatePlayer(player, get(player));
	}

	private static void updatePlayer(EntityPlayerMP player, MonkData monkData) {
		int level = monkData.getLevel();
		for (int i = 0; i < level; i++) {
			MonkMod.TRIGGER.trigger(player, level);
		}

		MonkNetwork.net.sendTo(new MessageMonkLevelData(monkData), player);
	}

	@SubscribeEvent
	public static void tick(TickEvent.PlayerTickEvent event) {
		if (event.player.world.isRemote || event.phase == TickEvent.Phase.START) return;
		MonkData monkData = get(event.player);

		EntityPlayerMP playerMP = (EntityPlayerMP) event.player;
		if (monkData.getLevel() != monkData.prevLevel) {
			updatePlayer(playerMP, monkData);
			monkData.prevLevel = monkData.getLevel();
		}

		Set<MonkAbility> abilities = MonkLevelManager.getAbilities(monkData.getLevel());
		abilities.forEach(
				ability -> ability.tickServer(playerMP)
		);


		AbstractAttributeMap attributeMap = event.player.getAttributeMap();
		for (IAttributeInstance attributeInstance : attributeMap.getAllAttributes()) {
			for (UUID uuid : MonkAbilityAttribute.uuids.keySet()) {
				if (attributeInstance.getModifier(uuid) != null) {
					Set<MonkAbilityAttribute> monkAbilityAttributes = MonkAbilityAttribute.uuids.get(uuid);
					if (monkAbilityAttributes.stream().noneMatch(abilities::contains)) {
						attributeInstance.removeModifier(uuid);
					}
				}
			}
		}

	}

	public static MonkData get(EntityPlayer player) {
		//noinspection ConstantConditions
		return player.getCapability(MonkData.MONKLEVELDATA, null);
	}

	public static boolean getAbilityLevel(EntityPlayer player, MonkAbility ability) {
		return MonkLevelManager.getAbilities(get(player).getLevel()).contains(ability);
	}

	public static boolean getAbilityLevel(EntityPlayer player, String key) {
		return MonkLevelManager.getAbilities(get(player).getLevel()).stream().anyMatch(a -> a.name.equals(key));
	}
}
