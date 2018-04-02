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
	private static final NBTSerializer<MonkData> serializer =
			NBTSerializer.<MonkData>createSerializer()
					.addInteger("level", MonkData::getLevel, MonkData::setLevel)
					.addInteger("progress", MonkData::getProgress, MonkData::setProgress)
					.addInteger("ki", MonkData::getKi, MonkData::setKi);
	@CapabilityInject(MonkData.class)
	public static final Capability<MonkData> MONKLEVELDATA = null;
	private int level = -1;
	public int prevLevel = -112;
	private int progress;

	@CapabilityInject(MonkData.class)
	public static void test(Capability<MonkData> cap){
		MonkMod.logger.debug("Cap Registered");
	}

	public int getKi() {
		return ki;
	}

	public void setKi(int ki) {
		this.ki = ki;
	}

	private int ki;

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
		progress += k;
	}
}
