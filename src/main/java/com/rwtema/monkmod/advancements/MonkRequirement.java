package com.rwtema.monkmod.advancements;

import com.rwtema.monkmod.MonkManager;
import com.rwtema.monkmod.MonkMod;
import com.rwtema.monkmod.data.MonkData;
import net.minecraft.entity.player.EntityPlayerMP;
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
}
