package com.rwtema.monkmod.advancements;

import com.rwtema.monkmod.advancements.criteria.MonkRequirementTick;
import com.rwtema.monkmod.data.MonkData;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;

public class MonkRequirementAdvancement extends MonkRequirementTick {
	private final ResourceLocation location;
	private final ITextComponent description;
	public MonkRequirementAdvancement(ResourceLocation location, ITextComponent description) {
		super("advancement", -1);
		this.location = location;
		this.description = description;
	}

	@Override
	protected void doTick(EntityPlayerMP player, MonkData monkData) {
		AdvancementManager manager = player.getServerWorld().getAdvancementManager();
		Advancement advancement = manager.getAdvancement(location);
		if(advancement == null) {
			grantLevel(player);
			return;
		}

		AdvancementProgress progress = player.getAdvancements().getProgress(advancement);
		if(progress.isDone()){
			grantLevel(player);
		}
	}

	@Nonnull
	@Override
	protected Object[] args() {
		return new Object[]{description};
	}
}
