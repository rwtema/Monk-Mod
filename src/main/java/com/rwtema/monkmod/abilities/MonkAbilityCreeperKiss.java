package com.rwtema.monkmod.abilities;

import com.google.common.collect.Iterables;
import com.rwtema.monkmod.MonkManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashSet;

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
			embarassCreeper(creeper);

			event.setCanceled(true);
			event.setCancellationResult(EnumActionResult.SUCCESS);
		}
	}

	public static void embarassCreeper(EntityCreeper creeper) {
		creeper.setCreeperState(-1);
		NBTTagCompound entityData = creeper.getEntityData();
		entityData.setBoolean("MonkTame", true);
		HashSet<EntityAIBase> toRemove = new HashSet<>();
		for (EntityAITasks.EntityAITaskEntry taskEntry : Iterables.concat(creeper.tasks.taskEntries, creeper.targetTasks.taskEntries )) {
			EntityAIBase action = taskEntry.action;
			if(action instanceof EntityAICreeperSwell || action instanceof EntityAINearestAttackableTarget || action instanceof EntityAIHurtByTarget){
				toRemove.add(action);
			}

		}
		toRemove.forEach(creeper.tasks::removeTask);
		toRemove.forEach(creeper.targetTasks::removeTask);
	}

	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent event){
		if (event.getEntity() instanceof EntityCreeper) {
			NBTTagCompound entityData = event.getEntity().getEntityData();
			if (entityData.getBoolean("MonkTame")) {
				embarassCreeper(((EntityCreeper) event.getEntity()));
			}
		}
	}
}
