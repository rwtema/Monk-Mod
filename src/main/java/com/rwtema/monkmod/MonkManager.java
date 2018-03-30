package com.rwtema.monkmod;

import com.rwtema.monkmod.data.MonkData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MonkManager {
	@SubscribeEvent
	public static void registerCap(AttachCapabilitiesEvent<EntityPlayer> playerAttachCapabilitiesEvent) {
		playerAttachCapabilitiesEvent.addCapability(MonkData.LOCATION, new MonkData());
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
