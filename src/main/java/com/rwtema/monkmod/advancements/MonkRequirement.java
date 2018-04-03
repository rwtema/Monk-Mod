package com.rwtema.monkmod.advancements;

import com.rwtema.monkmod.MonkManager;
import com.rwtema.monkmod.MonkMod;
import com.rwtema.monkmod.data.MonkData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;

public class MonkRequirement {
	public final int levelToGrant;

	public MonkRequirement(int level) {
		this.levelToGrant = level;
		MinecraftForge.EVENT_BUS.register(this);
	}

	public void grantLevel(EntityPlayerMP player) {
		MonkData monkData = MonkManager.get(player);
		monkData.setLevel(levelToGrant);
		monkData.setProgress(0);
		MonkMod.TRIGGER.trigger(player, levelToGrant);
		onGrant(player);
	}

	protected void onGrant(EntityPlayerMP player) {

	}

	public void displayText(Object object) {
		EntityPlayerSP player = Minecraft.getMinecraft().player;
		if (player != null)
			player.sendStatusMessage(new TextComponentString(object.toString()), true);
	}
}
