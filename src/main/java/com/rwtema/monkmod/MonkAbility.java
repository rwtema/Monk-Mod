package com.rwtema.monkmod;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;

public class MonkAbility {
	public final String name;
	public final int maxlevel;

	public MonkAbility(String name, int maxlevel) {
		this.name = name;
		this.maxlevel = maxlevel;
		MinecraftForge.EVENT_BUS.register(this);
	}

	public void tickServer(EntityPlayer player) {

	}

	public boolean isUnarmored(EntityPlayer player){
		return false;
	}

}
