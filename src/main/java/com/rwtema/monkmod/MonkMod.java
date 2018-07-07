package com.rwtema.monkmod;

import com.rwtema.monkmod.advancements.MonkAdvancements;
import com.rwtema.monkmod.advancements.MonkCriterionTrigger;
import com.rwtema.monkmod.command.CommandMonkLevelManip;
import com.rwtema.monkmod.config.MonkConfiguration;
import com.rwtema.monkmod.data.MonkData;
import com.rwtema.monkmod.factory.Factory;
import com.rwtema.monkmod.item.ItemMonkBase;
import com.rwtema.monkmod.network.MonkNetwork;
import com.rwtema.monkmod.render.HUDProgress;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Set;

@Mod(modid = MonkMod.MODID, name = MonkMod.NAME, version = MonkMod.VERSION)
public class MonkMod {
	public static final String MODID = "monk";
	public static final String NAME = "Monk";
	public static final String VERSION = "1.0";
	public static final MonkCriterionTrigger TRIGGER = CriteriaTriggers.register(new MonkCriterionTrigger());
	public static final ItemMonkBase ITEM_MONK_BASE = new ItemMonkBase();
	public static int MAX_LEVEL;
	public static Logger logger;

	public static int config_version = 2;

	@Mod.Instance(value = MODID)
	public static MonkMod instance;

	public static boolean debug;
	public static Configuration config;

	static {
		debug = false;
		try {
			World.class.getDeclaredMethod("getBlockState", BlockPos.class);
			debug = true;
		} catch (NoSuchMethodException ignore) {

		}
	}



	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();

		config = new Configuration(event.getSuggestedConfigurationFile());

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
		}, () -> {
			throw new RuntimeException();
		});
		MinecraftForge.EVENT_BUS.register(MonkManager.class);
		MinecraftForge.EVENT_BUS.register(HUDProgress.INSTANCE);

		Factory.init();
		MonkConfiguration.load();

		ResourceLocation location = new ResourceLocation(MODID, "monk_level");
		ITEM_MONK_BASE.setRegistryName(location);
		ITEM_MONK_BASE.setUnlocalizedName(location.toString());
		ForgeRegistries.ITEMS.register(ITEM_MONK_BASE);

		(new ClientRunnable() {
			@Override
			@SideOnly(Side.CLIENT)
			public void run() {
				MonkTextures.init();
			}
		}).run();

//		CreateJSons.create();
		MonkNetwork.init();

		if (config.hasChanged()) {
			config.save();
		}
	}


	@EventHandler
	public void onServerAboutToStartEvent(FMLServerAboutToStartEvent event) {
		MonkAdvancements.registerAdvancements(event.getServer());
	}

	@EventHandler
	public void onServerStart(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandMonkLevelManip());

	}
}
