package com.rwtema.monkmod;

import com.rwtema.monkmod.helper.NBTSerializer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MonkLevelData implements INBTSerializable<NBTTagCompound>, ICapabilityProvider {

	@CapabilityInject(MonkLevelData.class)
	public static Capability<MonkLevelData> MONKLEVELDATA = null;

	private static final NBTSerializer<MonkLevelData> serializer =
			NBTSerializer.<MonkLevelData>createSerializer()
					.addInteger("level", MonkLevelData::getLevel, MonkLevelData::setLevel)
					.addInteger("experience", MonkLevelData::getExperience, MonkLevelData::setExperience)
			;
	private int level;

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getExperience() {
		return experience;
	}

	public void setExperience(int experience) {
		this.experience = experience;
	}

	private int experience;

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
		return capability == MONKLEVELDATA;
	}

	@Nullable
	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
		return capability == MONKLEVELDATA ? MONKLEVELDATA.cast(this) : null;
	}
}
