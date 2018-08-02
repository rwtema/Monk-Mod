package com.rwtema.monkmod.advancements.criteria;

import com.rwtema.monkmod.data.MonkData;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.AxisAlignedBB;

import javax.annotation.Nonnull;

public class MonkRequirementMeditateEndermen extends MonkRequirementTick {

	public MonkRequirementMeditateEndermen(int time) {
		super("meditate_endermen", time);
	}

	@Override
	protected void doTick(@Nonnull EntityPlayerMP player, @Nonnull MonkData monkData) {
		AxisAlignedBB entityBoundingBox = player.getEntityBoundingBox();
		AxisAlignedBB grow = entityBoundingBox.grow(20);
		for (EntityEnderman enderman : player.world.getEntitiesWithinAABB(EntityEnderman.class, grow)) {
			if (enderman.isScreaming()) {
				monkData.resetProgress();
				return;
			} else {
				if (monkData.increase(1, requirementLimit)) {
					grantLevel(player);
				}
			}
		}
	}

}
