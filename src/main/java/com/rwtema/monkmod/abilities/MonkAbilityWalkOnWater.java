package com.rwtema.monkmod.abilities;

import com.rwtema.monkmod.MonkManager;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.world.GetCollisionBoxesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MonkAbilityWalkOnWater extends MonkAbility {

	public MonkAbilityWalkOnWater() {
		super("water_walking");
	}

	@SubscribeEvent
	public void getCollisions(GetCollisionBoxesEvent event) {
		Entity entity = event.getEntity();
		if (entity == null || entity.isInWater() || entity.isSneaking()) return;

		if (entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			if (!MonkManager.getAbilityLevel(player, this)) return;

			if (player.fallDistance > 3) {
				return;
			}
			AxisAlignedBB entityBoundingBox = player.getEntityBoundingBox();

			World world = event.getWorld();
			for (BlockPos.MutableBlockPos mutableBlockPos : BlockPos.getAllInBoxMutable(
					MathHelper.floor(entityBoundingBox.minX),
					MathHelper.floor(entityBoundingBox.minY - 1),
					MathHelper.floor(entityBoundingBox.minZ),
					MathHelper.ceil(entityBoundingBox.minX),
					MathHelper.floor(entityBoundingBox.minY),
					MathHelper.ceil(entityBoundingBox.minZ)
			)) {
				IBlockState state = world.getBlockState(mutableBlockPos);
				if (state.getBlock() == Blocks.WATER || state.getBlock() == Blocks.FLOWING_WATER) {
					if (state.getValue(BlockLiquid.LEVEL) == 0) {
						AxisAlignedBB bb = new AxisAlignedBB(mutableBlockPos);
						if (event.getAabb().intersects(bb)) {
							event.getCollisionBoxesList().add(bb);
						}
					}
				}
			}


		}
	}
}
