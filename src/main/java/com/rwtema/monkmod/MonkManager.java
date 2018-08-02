package com.rwtema.monkmod;

import com.rwtema.monkmod.abilities.MonkAbility;
import com.rwtema.monkmod.abilities.MonkAbilityAttribute;
import com.rwtema.monkmod.data.MonkData;
import com.rwtema.monkmod.levels.MonkLevelManager;
import com.rwtema.monkmod.network.MessageMonkLevelData;
import com.rwtema.monkmod.network.MessageProgress;
import com.rwtema.monkmod.network.MonkNetwork;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Set;
import java.util.UUID;

public class MonkManager {

	public static final int NUMBER_OF_TIMES_TO_UPDATE = 100;
	private static final ClientFunction<EntityPlayer, Boolean> isClient = new ClientFunction<EntityPlayer, Boolean>() {
		@Override
		@SideOnly(Side.CLIENT)
		public Boolean apply(EntityPlayer player) {
			return player.world.isRemote && player.equals(Minecraft.getMinecraft().player);
		}

		@Override
		public Boolean applyFallback(EntityPlayer player) {
			return false;
		}
	};
	public static MonkData clientData = new MonkData();

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
		newData.dirty = NUMBER_OF_TIMES_TO_UPDATE;
	}

	@SubscribeEvent
	public static void onChangeDim(PlayerEvent.PlayerChangedDimensionEvent event) {
		MonkData monkData = get(event.player);
		monkData.dirty = NUMBER_OF_TIMES_TO_UPDATE;
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
		player.sendPlayerAbilities();
	}

	@SubscribeEvent
	public static void tick(TickEvent.PlayerTickEvent event) {
		if (event.player.world.isRemote || event.phase == TickEvent.Phase.START) return;
		MonkData monkData = get(event.player);

		EntityPlayerMP playerMP = (EntityPlayerMP) event.player;

		if (monkData.progressDirty) {
			MonkNetwork.net.sendTo(new MessageProgress(monkData.getProgress(), monkData.getMaxProgress()), playerMP);
			monkData.progressDirty = false;
		}


		if (monkData.dirty > 0) {
			if(monkData.dirty == NUMBER_OF_TIMES_TO_UPDATE || (monkData.dirty % 6)  == 0)
				updatePlayer(playerMP, monkData);
			monkData.dirty--;
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
		if (isClient.apply(player)) {
			return clientData;
		}
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
