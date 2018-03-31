package com.rwtema.monkmod.abilities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.Method;

public class MonkCatchArrows extends MonkProtection {
	Method getArrowStackMethod = ReflectionHelper.findMethod(EntityArrow.class, "getArrowStack", "func_184550_j");

	public MonkCatchArrows(String name) {
		super(name);
	}

	@Override
	public float getAbsorbtion(DamageSource source, EntityPlayer player, int abilityLevel) {

		return 0;
	}

	@Override
	public boolean canHandle(EntityPlayer player, DamageSource source) {

		if (!isUnarmed(player)) {
			return false;
		}
		if (!source.isProjectile()) {
			return false;
		}
		if (!(source.getImmediateSource() instanceof EntityArrow)) {
			return false;
		}

		EntityArrow arrow = (EntityArrow) source.getImmediateSource();

		Vec3d vec3d = source.getDamageLocation();

		if (vec3d != null)
		{
			Vec3d vec3d1 = player.getLook(1.0F);
			Vec3d vec3d2 = vec3d.subtractReverse(new Vec3d(player.posX, player.posY, player.posZ)).normalize();
			vec3d2 = new Vec3d(vec3d2.x, 0.0D, vec3d2.z);

			if (vec3d2.dotProduct(vec3d1) < 0.0D)
			{
				return true;
			}
		}

		if (arrow.pickupStatus == EntityArrow.PickupStatus.ALLOWED) {
			ItemStack dropStack;
			try {
				dropStack = (ItemStack) getArrowStackMethod.invoke(arrow);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			if (!player.inventory.addItemStackToInventory(dropStack)) {
				arrow.entityDropItem(dropStack, 0.1F);
			}
		}
		arrow.setDead();
		return true;

	}
}
