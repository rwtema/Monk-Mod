package com.rwtema.monkmod.abilities;

import com.rwtema.monkmod.MonkManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MonkAbilityCreeperKiss extends MonkAbility {
	public MonkAbilityCreeperKiss(String name) {
		super(name);
	}


	@SubscribeEvent
	public void onRightClickAnimal(PlayerInteractEvent.EntityInteract event) {
		Entity entity = event.getTarget();
		if (!(entity instanceof EntityCreeper)) return;
		EntityCreeper creeper = (EntityCreeper) entity;
		if (creeper.isAIDisabled()) {
			return;
		}

		EntityPlayer entityPlayer = event.getEntityPlayer();

		if (!entityPlayer.getHeldItem(event.getHand()).isEmpty()) return;

		int abilityLevel = MonkManager.getAbilityLevel(entityPlayer, this);
		if (abilityLevel == -1) return;


		if (event.getWorld().isRemote) {
			event.setCanceled(true);
			event.setCancellationResult(EnumActionResult.SUCCESS);
		} else {

			entityPlayer.sendMessage(new TextComponentTranslation("chat.type.text", entity.getDisplayName(), new TextComponentTranslation("monk.blush")));
			creeper.setNoAI(true);

			event.setCanceled(true);
			event.setCancellationResult(EnumActionResult.SUCCESS);
		}
	}
}
