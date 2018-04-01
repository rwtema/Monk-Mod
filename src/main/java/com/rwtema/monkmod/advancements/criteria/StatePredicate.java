package com.rwtema.monkmod.advancements.criteria;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface StatePredicate {
	boolean test(World world, BlockPos pos, IBlockState state);
}
