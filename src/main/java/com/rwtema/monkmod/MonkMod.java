package com.rwtema.monkmod;

import com.rwtema.monkmod.data.MonkData;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

@Mod(modid = MonkMod.MODID, name = MonkMod.NAME, version = MonkMod.VERSION)
public class MonkMod {
	public static final String MODID = "monk";
	public static final String NAME = "Monk";
	public static final String VERSION = "1.0";

	private static Logger logger;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		CapabilityManager.INSTANCE.register(MonkData.class, new Capability.IStorage<MonkData>() {
			@Nullable
			@Override
			public NBTBase writeNBT(Capability<MonkData> capability, MonkData instance, EnumFacing side) {
				return instance.serializeNBT();
			}

			@Override
			public void readNBT(Capability<MonkData> capability, MonkData instance, EnumFacing side, NBTBase nbt) {
				instance.deserializeNBT((NBTTagCompound) nbt);
			}
		}, MonkData::new);
		MinecraftForge.EVENT_BUS.register(MonkManager.class);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {

	}
}
