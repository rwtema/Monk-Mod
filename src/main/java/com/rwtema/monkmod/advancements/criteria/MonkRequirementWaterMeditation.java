package com.rwtema.monkmod.advancements.criteria;

import com.rwtema.monkmod.data.MonkData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.Vec3d;

public class MonkRequirementWaterMeditation extends MonkRequirementTick {

	private final int stareTime;

	public MonkRequirementWaterMeditation(int level, int stareTime) {
		super(level);
		this.stareTime = stareTime;
	}

	@Override
	protected void doTick(EntityPlayerMP player, MonkData monkData) {
		Entity ridingEntity = player.getRidingEntity();
		if (!(ridingEntity instanceof EntityBoat)) {
			monkData.setProgress(0);
			return;
		}

		double celestialAngle = player.world.getCelestialAngle(0) * Math.PI * 2;
		double sunDist = Math.sin(celestialAngle);
		double sunHeight = Math.cos(celestialAngle);
		if (sunHeight < -0.2) {
			Vec3d vec3d1 = player.getLook(1.0F);
			Vec3d sunDir = new Vec3d(-sunDist, sunHeight, 0);

			if (sunDir.dotProduct(vec3d1) < -0.99) {
				monkData.increaseProgress(1);
				if (monkData.getProgress() > stareTime) {
					grantLevel(player);
				}
			}
		} else {
			monkData.setProgress(0);
		}
	}
}
