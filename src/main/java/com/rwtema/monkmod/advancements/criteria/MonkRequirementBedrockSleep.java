package com.rwtema.monkmod.advancements.criteria;

import com.rwtema.monkmod.MonkManager;
import com.rwtema.monkmod.advancements.MonkRequirement;
import com.rwtema.monkmod.data.MonkData;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.event.entity.player.SleepingLocationCheckEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;

public class MonkRequirementBedrockSleep extends MonkRequirementSleep {
	public MonkRequirementBedrockSleep() {
		super("bedrock_sleep", -1);
	}


	protected boolean checkPos(World world, @Nonnull BlockPos pos) {
		IBlockState state = world.getBlockState(pos);
		if (state.getBlock() != Blocks.BEDROCK || !world.isAirBlock(pos.up())) {
			return false;
		}

		for (EnumFacing horizontal : EnumFacing.HORIZONTALS) {
			BlockPos offset = pos.offset(horizontal);
			if (world.getBlockState(offset).getBlock() == Blocks.BEDROCK && world.isAirBlock(offset.up())) {
				return true;
			}
		}
		return false;
	}


}
