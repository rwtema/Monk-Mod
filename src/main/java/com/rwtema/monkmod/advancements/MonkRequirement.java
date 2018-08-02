package com.rwtema.monkmod.advancements;

import com.rwtema.monkmod.MonkManager;
import com.rwtema.monkmod.MonkMod;
import com.rwtema.monkmod.data.MonkData;
import com.rwtema.monkmod.factory.Factory;
import com.rwtema.monkmod.factory.IFactoryMade;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.Nonnull;

public class MonkRequirement implements IFactoryMade {
	public final String name;
	public final int requirementLimit;
	public int levelToGrant = -1;

	public MonkRequirement(String name, int defaultRequirements) {
		this.name = name;
		requirementLimit = defaultRequirements;
		if (Factory.shouldRegister)
			MinecraftForge.EVENT_BUS.register(this);
	}

	public void grantLevel(EntityPlayerMP player) {
		MonkData monkData = MonkManager.get(player);
		monkData.setLevel(levelToGrant);
		monkData.resetProgress();
		MonkMod.TRIGGER.trigger(player, levelToGrant);
		onGrant(player);
	}

	protected void onGrant(EntityPlayerMP player) {

	}

	@Override
	public String getKey() {
		return name;
	}

	protected boolean isWorkingToLevel(@Nonnull MonkData monkData) {
		return monkData.getLevel() == (this.levelToGrant - 1);
	}

	@Nonnull
	public ITextComponent getDescriptionComponent() {
		return new TextComponentTranslation("monk.advancements.requirement." + name, (Object[]) args());
	}

	@Nonnull
	protected Object[] args() {
		if (requirementLimit == -1) return new Object[0];
		return new Object[]{requirementLimit};
	}
}
