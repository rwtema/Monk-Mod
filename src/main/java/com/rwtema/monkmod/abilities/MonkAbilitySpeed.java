package com.rwtema.monkmod.abilities;

import com.rwtema.monkmod.MonkManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MonkAbilitySpeed extends MonkAbilityAttribute {
	public MonkAbilitySpeed(double increase) {
		super("swift", SharedMonsterAttributes.MOVEMENT_SPEED, increase, 1);
	}

	@Override
	public boolean canApply(EntityPlayer player) {
		return isUnarmored(player);
	}

	@Override
	public double getAmount(EntityPlayer player) {
		double amount = super.getAmount(player);
		return player.isSprinting() ? amount * 2 : amount;
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void overrideFOV(FOVUpdateEvent event) {
		EntityPlayerSP player = Minecraft.getMinecraft().player;
		if (player == null) return;
		if (!MonkManager.getAbilityLevel(player, this)) return;

		IAttributeInstance iattributeinstance = player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
		double oldMult = multiplier(player, iattributeinstance);
		AttributeModifier modifier = iattributeinstance.getModifier(uuid);
		if (modifier == null) return;
		iattributeinstance.removeModifier(modifier);
		double newMult = multiplier(player, iattributeinstance);
		iattributeinstance.applyModifier(modifier);
		event.setNewfov((float) ((event.getFov() / oldMult) * newMult));
	}

	@SideOnly(Side.CLIENT)
	private double multiplier(EntityPlayerSP player, IAttributeInstance iattributeinstance) {
		return (iattributeinstance.getAttributeValue() / (double) player.capabilities.getWalkSpeed() + 1.0D) / 2.0D;
	}
}
