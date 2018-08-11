package com.rwtema.monkmod.abilities;

import com.rwtema.monkmod.MonkManager;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MonkAbilityGlassTouch extends MonkAbility {
	public MonkAbilityGlassTouch() {
		super("glass_touch");
	}

	@SubscribeEvent
	public void harvestGlass(BlockEvent.HarvestDropsEvent event) {
		IBlockState state = event.getState();
		EntityPlayer harvester;

		if (state.getMaterial() != Material.GLASS
				|| state.getBlock().hasTileEntity(state)
				|| event.isSilkTouching()
				|| (harvester = event.getHarvester()) == null
				|| !MonkManager.getAbilityLevel(harvester, this)
				|| !isUnarmed(harvester))
			return;

		event.setDropChance(0);
		Block block = state.getBlock();
		ItemStack stack = new ItemStack(Items.STICK);
		stack.addEnchantment(Enchantments.SILK_TOUCH, 1);
		block.harvestBlock(event.getWorld(), harvester, event.getPos(), state, null, stack);
	}
}
