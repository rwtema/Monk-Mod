package com.rwtema.monkmod.advancements.criteria;

import com.rwtema.monkmod.data.MonkData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;

public class MonkRequirementStare extends MonkRequirementTick {
	public MonkRequirementStare(int level) {
		super(level);
	}

	@Nullable
	public static EntityLiving getStareEntity(EntityPlayerMP player, Predicate<EntityLiving> additionalPredicate, int distance) {
		Vec3d look = player.getLook(1).normalize();
		Vec3d startPos = new Vec3d(player.posX, player.posY + (double) player.getEyeHeight(), player.posZ);
		Vec3d endPos = startPos.add(look.scale(distance));
		World world = player.world;
		RayTraceResult rayTraceResult = world.rayTraceBlocks(startPos, endPos, false, true, false);
		if (rayTraceResult != null && rayTraceResult.hitVec != null) {
			endPos = rayTraceResult.hitVec;
		}

		EntityLiving resultEntity = null;
		List<Entity> list = world.getEntitiesInAABBexcluding(
				player,
				new AxisAlignedBB(startPos, endPos)
				, entity -> entity instanceof EntityLiving && entity.canBeCollidedWith() && additionalPredicate.test((EntityLiving) entity)
		);
		double d6 = 0.0D;

		for (Entity entity : list) {
			if (entity.canBeCollidedWith() && entity instanceof EntityLiving) {
				Vec3d vec3d1 = new Vec3d(entity.posX - player.posX, entity.getEntityBoundingBox().minY + (double) entity.getEyeHeight() - (player.posY + (double) player.getEyeHeight()), entity.posZ - player.posZ);
				double d0 = vec3d1.lengthVector();
				vec3d1 = vec3d1.normalize();
				double d1 = look.dotProduct(vec3d1);

				if (d1 <= 1.0D - 0.025D / d0 || !player.canEntityBeSeen(entity)) {
					continue;
				}


				AxisAlignedBB axisalignedbb = entity.getEntityBoundingBox().grow(0.3);
				RayTraceResult raytraceresult1 = axisalignedbb.calculateIntercept(startPos, endPos);

				if (raytraceresult1 != null) {
					double d7 = startPos.squareDistanceTo(raytraceresult1.hitVec);

					if (d7 < d6 || d6 == 0.0D) {
						resultEntity = (EntityLiving) entity;
						d6 = d7;
					}
				}
			}
		}
		return resultEntity;
	}

	@Override
	protected void doTick(EntityPlayerMP player, MonkData monkData) {
		World world = player.world;
		if (!(world instanceof WorldServer)) return;
		int progress = monkData.getProgress();
		monkData.setProgress(Math.max(0, progress - 4));

		if (((WorldServer) world).getChunkProvider().isInsideStructure(world, "Fortress", new BlockPos(player))) {
			EntityLiving resultEntity = getStareEntity(player,
					e -> e instanceof EntityWitherSkeleton && ((EntityWitherSkeleton) e).getAttackTarget() == player, 20);

			if (resultEntity != null) {
				progress++;
				monkData.setProgress(progress);
				if (progress > 200) {
					resultEntity.setAttackTarget(null);
					resultEntity.getNavigator().clearPath();
					grantLevel(player);
				}
			}
		}
		displayText(progress+"");
	}
}
