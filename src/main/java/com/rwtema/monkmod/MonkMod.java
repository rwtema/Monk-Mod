package com.rwtema.monkmod;

import com.rwtema.monkmod.advancements.MonkCriterionTrigger;
import com.rwtema.monkmod.command.CommandMonkLevelManip;
import com.rwtema.monkmod.data.MonkData;
import com.rwtema.monkmod.item.ItemMonkBase;
import com.rwtema.monkmod.levels.MonkLevels;
import com.rwtema.monkmod.network.MonkNetwork;
import gnu.trove.map.hash.TIntObjectHashMap;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

@Mod(modid = MonkMod.MODID, name = MonkMod.NAME, version = MonkMod.VERSION)
public class MonkMod {
	public static final String MODID = "monk";
	public static final String NAME = "Monk";
	public static final String VERSION = "1.0";
	public static final MonkCriterionTrigger TRIGGER = CriteriaTriggers.register(new MonkCriterionTrigger());
	public static final ItemMonkBase ITEM_MONK_BASE = new ItemMonkBase();
	public final static int MAX_LEVEL = 20;
	public static Logger logger;

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
		}, () -> {
			throw new RuntimeException();
		});
		MinecraftForge.EVENT_BUS.register(MonkManager.class);
		MonkLevels.init();

		ResourceLocation location = new ResourceLocation(MODID, "monk_level");
		ITEM_MONK_BASE.setRegistryName(location);
		ITEM_MONK_BASE.setUnlocalizedName(location.toString());
		ForgeRegistries.ITEMS.register(ITEM_MONK_BASE);

		(new ClientRunnable() {
			@Override
			@SideOnly(Side.CLIENT)
			public void run() {
				TIntObjectHashMap<ModelResourceLocation> map = new TIntObjectHashMap<>();

				for (int i = 0; i <= 21; i++) {
					ModelResourceLocation modelResourceLocation = new ModelResourceLocation(location.toString() + "_" + i, "inventory");
					map.put(i, modelResourceLocation);
					ModelLoader.setCustomModelResourceLocation(ITEM_MONK_BASE, i, modelResourceLocation);
					ModelBakery.registerItemVariants(ITEM_MONK_BASE, modelResourceLocation);
				}
				ModelLoader.setCustomMeshDefinition(ITEM_MONK_BASE, stack -> map.containsKey(stack.getMetadata()) ? map.get(stack.getMetadata()) : map.get(0));
			}
		}).run();

//		CreateJSons.create();
		MonkNetwork.init();
	}

	@EventHandler
	public void onServerStart(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandMonkLevelManip());
	}
}
