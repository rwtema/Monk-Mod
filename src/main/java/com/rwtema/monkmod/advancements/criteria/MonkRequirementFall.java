package com.rwtema.monkmod.advancements.criteria;

import com.rwtema.monkmod.MonkManager;
import com.rwtema.monkmod.advancements.MonkRequirement;
import com.rwtema.monkmod.data.MonkData;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;

public class MonkRequirementFall extends MonkRequirement {
	public MonkRequirementFall(int defaultRequirements) {
		super("fall", defaultRequirements);
	}

	@SubscribeEvent
	public void onFall(@Nonnull LivingFallEvent event) {
		if (event.getDistance() < requirementLimit) return;

		if (event.getEntityLiving() instanceof EntityPlayerMP) {
			EntityPlayerMP player = (EntityPlayerMP) event.getEntityLiving();
			MonkData monkData = MonkManager.get(player);
			if (monkData.getLevel() == (this.levelToGrant - 1)) {

				EntityLivingBase entityLiving = event.getEntityLiving();
				int j6 = MathHelper.floor(entityLiving.posX);
				int i1 = MathHelper.floor(entityLiving.posY - 0.20000000298023224D);
				int k6 = MathHelper.floor(entityLiving.posZ);
				BlockPos blockpos = new BlockPos(j6, i1, k6);
				IBlockState iblockstate = entityLiving.world.getBlockState(blockpos);

				if (iblockstate.getBlock() == Blocks.HAY_BLOCK) {
					event.setDamageMultiplier(0);
					grantLevel(player);
				}
			}
		}

	}
}
