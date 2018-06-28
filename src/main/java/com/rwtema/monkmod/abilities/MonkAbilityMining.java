package com.rwtema.monkmod.abilities;

import com.rwtema.monkmod.MonkManager;
import net.minecraft.block.state.IBlockState;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Set;

public class MonkAbilityMining extends MonkAbility {
	private final float speed;
	private final Set<String> validTools;
	private final int harvestLevel;


	public MonkAbilityMining(int harvestLevel, float speeds, Set<String> validTools) {
		super("mining");
		this.speed = speeds;
		this.validTools = validTools;
		this.harvestLevel = harvestLevel;
	}

	@SubscribeEvent
	public void onMine(PlayerEvent.BreakSpeed event) {
		if (!MonkManager.getAbilityLevel(event.getEntityPlayer(), this)) {
			return;
		}

		float oldSpeed = Math.min(event.getOriginalSpeed(), 1);

		event.setNewSpeed(Math.max(event.getNewSpeed(), (1 + speed) * oldSpeed));
	}

	@SubscribeEvent
	public void onHarvest(PlayerEvent.HarvestCheck event) {
		if (!MonkManager.getAbilityLevel(event.getEntityPlayer(), this)) {
			return;
		}

		IBlockState targetBlock = event.getTargetBlock();
		String harvestTool = targetBlock.getBlock().getHarvestTool(targetBlock);

		if (harvestTool != null && (harvestLevel == 0 && "pickaxe".equals(harvestTool) || !validTools.contains(harvestTool)))
			return;

		if (targetBlock.getBlock().getHarvestLevel(targetBlock) <= harvestLevel) {
			event.setCanHarvest(true);
		}
	}

	@Override
	public String getUnlocalized() {
		return super.getUnlocalized() + "." + harvestLevel;
	}

	@Override
	protected String[] args() {
		return new String[]{};
	}
}
