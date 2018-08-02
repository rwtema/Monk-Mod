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

import static com.rwtema.monkmod.MonkManager.NUMBER_OF_TIMES_TO_UPDATE;

public class MonkData implements INBTSerializable<NBTTagCompound>, ICapabilityProvider {

	public static final ResourceLocation LOCATION = new ResourceLocation(MonkMod.MODID, "monk_level_data");
	@Nullable
	@CapabilityInject(MonkData.class)
	public static final Capability<MonkData> MONKLEVELDATA = null;
	private static final NBTSerializer<MonkData> serializer =
			NBTSerializer.<MonkData>createSerializer()
					.addInteger("level", MonkData::getLevel, MonkData::setLevel)
					.addInteger("progress", MonkData::getProgress, MonkData::setProgress);
	public int dirty = NUMBER_OF_TIMES_TO_UPDATE;
	public boolean progressDirty = true;
	private int progress = -1;
	private int max_progress = -1;
	private int level = -1;

	@CapabilityInject(MonkData.class)
	public static void test(Capability<MonkData> cap) {
		MonkMod.logger.debug("Cap Registered");
	}

	public int getMaxProgress() {
		return max_progress;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
		progress = 0;
		max_progress = -1;
		progressDirty = true;
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

	@SuppressWarnings("ConstantConditions")
	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == MONKLEVELDATA;
	}

	@Nullable
	@Override
	@SuppressWarnings("ConstantConditions")
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
		return capability == MONKLEVELDATA ? MONKLEVELDATA.cast(this) : null;
	}

	public boolean increase(int k, int threshold) {
		if (k != 0 || threshold != max_progress) {
			progressDirty = true;
		}
		progress += k;
		max_progress = threshold;
		return progress >= threshold;
	}

	public void resetProgress() {
		if (progress != 0)
			progressDirty = true;
		progress = 0;
	}
}
