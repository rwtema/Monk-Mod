package com.rwtema.monkmod.data;

import com.rwtema.monkmod.MonkMod;
import com.rwtema.monkmod.helper.NBTSerializer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MonkData implements INBTSerializable<NBTTagCompound>, ICapabilityProvider {

	public static final ResourceLocation LOCATION = new ResourceLocation(MonkMod.MODID, "monk_level_data");
	@CapabilityInject(MonkData.class)
	public static final Capability<MonkData> MONKLEVELDATA = null;
	private static final NBTSerializer<MonkData> serializer =
			NBTSerializer.<MonkData>createSerializer()
					.addInteger("level", MonkData::getLevel, MonkData::setLevel)
					.addInteger("progress", MonkData::getProgress, MonkData::setProgress);
	public int prevLevel = -112;
	public boolean progressDirty = false;
	private int progress, level = -1;

	@CapabilityInject(MonkData.class)
	public static void test(Capability<MonkData> cap) {
		MonkMod.logger.debug("Cap Registered");
	}


	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		if (progress != this.progress) progressDirty = true;
		this.progress = progress;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		return serializer.serialize(this, new NBTTagCompound());
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		serializer.deserialize(this, nbt);
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		assert MONKLEVELDATA != null;
		return capability == MONKLEVELDATA;
	}

	@Nullable
	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
		assert MONKLEVELDATA != null;
		return capability == MONKLEVELDATA ? MONKLEVELDATA.cast(this) : null;
	}

	public void increaseProgress(int k) {
		if (k != 0) progressDirty = true;
		progress += k;
	}

	public boolean increase(int k, int threshold) {
		if (k != 0) progressDirty = true;
		progress += k;
		return progress >= threshold;
	}
}
