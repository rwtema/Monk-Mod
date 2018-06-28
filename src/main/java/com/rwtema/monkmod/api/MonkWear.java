package com.rwtema.monkmod.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

// Add to ItemStack capabilities to allow players to wear armor and items
public interface MonkWear {
	@CapabilityInject(MonkWear.class)
	Capability<MonkWear> MONK_SAFE_CAPABILITY = null;

	@CapabilityInject(IItemHandler.class)
	static void init(Capability<IItemHandler> capability) {
		CapabilityManager.INSTANCE.register(MonkWear.class, new Capability.IStorage<MonkWear>() {
					@Nullable
					@Override
					public NBTBase writeNBT(Capability<MonkWear> capability, MonkWear instance, EnumFacing side) {
						return null;
					}

					@Override
					public void readNBT(Capability<MonkWear> capability, MonkWear instance, EnumFacing side, NBTBase nbt) {

					}
				},
				() -> player -> true);


	}

	boolean canMonkWear(@Nullable EntityPlayer player);
}
