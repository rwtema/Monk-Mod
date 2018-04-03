package com.rwtema.monkmod.helper;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagString;

import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class NBTSerializer<T> {
	private final HashMap<String, DataTag<T, ?>> map = new HashMap<>();

	private NBTSerializer() {
	}

	public static <T> NBTSerializer<T> createSerializer() {
		return new NBTSerializer<T>();
	}

	public NBTTagCompound serialize(T object, NBTTagCompound tag) {
		map.forEach((key, tDataTag) -> tag.setTag(key, tDataTag.write(object)));
		return tag;
	}

	public void deserialize(T object, NBTTagCompound tag) {
		map.forEach((key, datatag) -> {
			if (tag.hasKey(key)) {
				NBTBase base = tag.getTag(key);
				((DataTag) datatag).read(object, base);
			}
		});
	}

	public <V, K extends NBTBase> NBTSerializer<T> add(String key, Function<T, V> getter, BiConsumer<T, V> setter, Function<V, K> toTag, Function<K, V> fromTag) {
		DataTagGetterSetter<T, V, K> tvkDataTagGetterSetter = new DataTagGetterSetter<>(toTag, fromTag, getter, setter);
		map.put(key, tvkDataTagGetterSetter);
		return this;
	}

	public NBTSerializer<T> addInteger(String key, Function<T, Integer> getter, BiConsumer<T, Integer> setter) {
		return this.add(key, getter, setter, NBTTagInt::new, NBTTagInt::getInt);
	}

	public NBTSerializer<T> addString(String key, Function<T, String> getter, BiConsumer<T, String> setter) {
		return this.add(key, getter, setter, NBTTagString::new, NBTTagString::getString);
	}

	public abstract static class DataTag<T, K extends NBTBase> {
		public abstract void read(T object, K tag);

		public abstract K write(T object);
	}

	public static class DataTagGetterSetter<T, V, K extends NBTBase> extends DataTag<T, K> {
		public final Function<V, K> toTag;
		public final Function<K, V> fromTag;
		public final Function<T, V> getter;
		public final BiConsumer<T, V> setter;

		public DataTagGetterSetter(Function<V, K> toTag, Function<K, V> fromTag, Function<T, V> getter, BiConsumer<T, V> setter) {
			this.toTag = toTag;
			this.fromTag = fromTag;
			this.getter = getter;
			this.setter = setter;
		}

		@Override
		public void read(T object, K tag) {
			V apply = fromTag.apply(tag);
			setter.accept(object, apply);
		}

		@Override
		public K write(T object) {
			V apply = getter.apply(object);
			return toTag.apply(apply);
		}


	}
}
