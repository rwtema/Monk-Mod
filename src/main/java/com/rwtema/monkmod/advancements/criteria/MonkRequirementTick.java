package com.rwtema.monkmod.advancements.criteria;

import com.rwtema.monkmod.MonkManager;
import com.rwtema.monkmod.advancements.MonkRequirement;
import com.rwtema.monkmod.data.MonkData;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public abstract class MonkRequirementTick extends MonkRequirement {
	public MonkRequirementTick(String name, int defaultRequirements) {
		super(name, defaultRequirements);
	}

	@SubscribeEvent
	public void walk(TickEvent.PlayerTickEvent event) {
		if (event.phase == TickEvent.Phase.START) {
			return;
		}

		if (event.player instanceof EntityPlayerMP) {
			MonkData monkData = MonkManager.get(event.player);
			if (monkData.getLevel() == (this.levelToGrant - 1)) {
				EntityPlayerMP player = (EntityPlayerMP) event.player;
				doTick(player, monkData);
			}
		}
	}

	protected abstract void doTick(EntityPlayerMP player, MonkData monkData);
}
