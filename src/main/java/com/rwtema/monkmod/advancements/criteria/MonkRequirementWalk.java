package com.rwtema.monkmod.advancements.criteria;

import com.rwtema.monkmod.abilities.MonkAbility;
import com.rwtema.monkmod.data.MonkData;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.stats.StatList;
import net.minecraft.stats.StatisticsManagerServer;

import java.util.WeakHashMap;

public abstract class MonkRequirementWalk extends MonkRequirementTick {
	private final static int STEP_PER_BLOCK = 100;

	WeakHashMap<EntityPlayerMP, MutableInt> trackers = new WeakHashMap<>();

	public MonkRequirementWalk(double numSteps, String name) {
		super(name, (int) (numSteps * STEP_PER_BLOCK));
	}

	@Override
	protected void doTick(EntityPlayerMP player, MonkData monkData) {
		if (satisfiesRequirements(player)) {
			MutableInt mutableInt = trackers.computeIfAbsent(player, playerMP -> new MutableInt(getRunDist(playerMP)));

			int k = mutableInt.getDiffAndStore(getRunDist(player));

			monkData.increaseProgress(k);
			if (monkData.getProgress() > requirementLimit) {
				grantLevel(player);
				trackers.remove(player);
			}
		} else {
			monkData.setProgress(0);
		}

	}

	public int getRunDist(EntityPlayerMP playerMP) {
		StatisticsManagerServer statFile = playerMP.getStatFile();
		return statFile.readStat(StatList.SPRINT_ONE_CM) +
				statFile.readStat(StatList.SNEAK_TIME) +
				statFile.readStat(StatList.WALK_ONE_CM)
				;
	}

	public boolean satisfiesRequirements(EntityPlayerMP player) {
		return MonkAbility.isUnarmored(player);
	}

	public class MutableInt {
		int value;

		public MutableInt(int value) {
			this.value = value;
		}

		public int getDiffAndStore(int newValue) {
			int diff = newValue - value;
			value = newValue;
			return diff;
		}
	}
}



