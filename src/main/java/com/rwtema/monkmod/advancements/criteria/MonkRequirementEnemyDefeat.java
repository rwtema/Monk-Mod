package com.rwtema.monkmod.advancements.criteria;

import com.rwtema.monkmod.MonkManager;
import com.rwtema.monkmod.abilities.MonkAbility;
import com.rwtema.monkmod.data.MonkData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.WeakHashMap;

public class MonkRequirementEnemyDefeat extends MonkRequirementTick {
	private final WeakHashMap<EntityLivingBase, WeakHashMap<EntityPlayerMP, Entry>> entryTracker = new WeakHashMap<>();
	private final ResourceLocation id;

	public MonkRequirementEnemyDefeat(String name, int defaultRequirements, ResourceLocation id) {
		super(name, defaultRequirements);
		this.id = id;
	}

	@Override
	protected void doTick(@Nonnull EntityPlayerMP player, MonkData monkData) {
		if (!entryTracker.isEmpty()) {
			if (!MonkAbility.isUnarmored(player)) {
				disqualifyPlayer(player);
			}
		}
	}

	private void disqualifyPlayer(@Nonnull EntityPlayerMP player) {
		if (entryTracker.values().removeIf(
				m -> {
					Entry remove = m.remove(player);
					if (remove != null) {
						remove.disqualified = true;
					}
					return m.isEmpty();
				}
		)) {
			player.sendStatusMessage(new TextComponentTranslation("monk.disqualify"), true);
		}
	}

	@SubscribeEvent
	public void onDeath(@Nonnull LivingDeathEvent event) {
		if (event.getEntity().world.isRemote || entryTracker.isEmpty() || !id.equals(EntityList.getKey(event.getEntity())))
			return;
		EntityLivingBase entityLiving = event.getEntityLiving();

		WeakHashMap<EntityPlayerMP, Entry> map = entryTracker.get(entityLiving);
		if (map == null) return;
		for (Map.Entry<EntityPlayerMP, Entry> playerMPEntryEntry : map.entrySet()) {
			EntityPlayerMP player = playerMPEntryEntry.getKey();
			MonkData monkData = MonkManager.get(player);
			if (isWorkingToLevel(monkData)) {
				if (!playerMPEntryEntry.getValue().disqualified && playerMPEntryEntry.getValue().damage > (event.getEntityLiving().getMaxHealth() / 4)) {
					if (monkData.increase(1, requirementLimit)) {
						grantLevel(player);
					}
				}
			}
		}

	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onDamage(@Nonnull LivingDamageEvent event) {
		if (!event.getEntity().world.isRemote && id.equals(EntityList.getKey(event.getEntity()))) {
			DamageSource source = event.getSource();
			Entity trueSource = source.getTrueSource();
			if (trueSource instanceof EntityPlayerMP) {
				EntityPlayerMP player = (EntityPlayerMP) trueSource;
				MonkData monkData = MonkManager.get(player);
				if (isWorkingToLevel(monkData)) {
					if (source.isProjectile()) {
						disqualifyPlayer(player);
					} else {
						Entry entry = entryTracker
								.computeIfAbsent(event.getEntityLiving(), t -> new WeakHashMap<>())
								.computeIfAbsent(player, p -> new Entry());
						entry.damage += event.getAmount();
					}
				}
			}
		}

	}

	@Nonnull
	@Override
	protected Object[] args() {
		String s1 = EntityList.getTranslationName(id);

		if (s1 == null) {
			s1 = "generic";
		}

		return new Object[]{requirementLimit, I18n.translateToLocal("entity." + s1 + ".name")};
	}

	private class Entry {
		boolean disqualified;
		float damage;
	}
}
