package com.rwtema.monkmod.advancements;

import com.google.common.collect.HashMultimap;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.rwtema.monkmod.MonkMod;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.advancements.critereon.AbstractCriterionInstance;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class MonkCriterionTrigger implements ICriterionTrigger<MonkCriterionTrigger.Instance> {
	private static final ResourceLocation ID = new ResourceLocation(MonkMod.MODID, "levelup");
	HashMultimap<PlayerAdvancements, Listener<Instance>> instances = HashMultimap.create();

	@Override
	public ResourceLocation getId() {
		return ID;
	}

	@Override
	public void addListener(@Nonnull PlayerAdvancements playerAdvancementsIn, @Nonnull Listener<Instance> listener) {
		instances.put(playerAdvancementsIn, listener);
	}

	@Override
	public void removeListener(@Nonnull PlayerAdvancements playerAdvancementsIn, @Nonnull Listener<Instance> listener) {
		instances.remove(playerAdvancementsIn, listener);
	}

	@Override
	public void removeAllListeners(@Nonnull PlayerAdvancements playerAdvancementsIn) {
		instances.removeAll(playerAdvancementsIn);
	}

	@Nonnull
	@Override
	public Instance deserializeInstance(@Nonnull JsonObject json, @Nonnull JsonDeserializationContext context) {
		int level = JsonUtils.getInt(json, "level");
		return new Instance(level);
	}

	public void trigger(EntityPlayerMP player, int level) {
		for (Listener<Instance> listener : instances.get(player.getAdvancements())) {
			if (listener.getCriterionInstance().level == level) {
				listener.grantCriterion(player.getAdvancements());
			}
		}
	}

	public static class Instance extends AbstractCriterionInstance {
		private int level;

		public Instance(int level) {
			super(ID);
			this.level = level;
		}
	}
}
