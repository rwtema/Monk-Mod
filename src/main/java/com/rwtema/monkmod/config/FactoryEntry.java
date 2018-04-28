package com.rwtema.monkmod.config;

import com.rwtema.monkmod.MonkMod;
import com.rwtema.monkmod.abilities.MonkAbility;
import com.rwtema.monkmod.advancements.MonkRequirement;
import com.rwtema.monkmod.factory.Factory;
import net.minecraftforge.common.config.Property;

import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FactoryEntry<T> {
	final Factory<T> factory;
	final HashMap<Factory.Parameter, String> values = new HashMap<>();

	public FactoryEntry(Factory<T> factory) {
		this.factory = factory;
	}

	public static FactoryEntry<MonkAbility> ability(String name) {
		return new FactoryEntry<>(Factory.abilityFactories.get(name));
	}

	public static FactoryEntry<MonkRequirement> requirement(String name) {
		return new FactoryEntry<>(Factory.requirementFactories.get(name));
	}

	public void writeToConfig(String category) {
		String categoryName = category + "." + factory.name;
		MonkMod.config.getCategory(categoryName);
		for (Factory.Parameter parameter : factory.parameterList) {
			String s = values.getOrDefault(parameter, parameter._default);
			if (s == null) throw new IllegalArgumentException("Missing parameter: " + parameter.name);
			switch (parameter.type) {
				case INTEGER:
					MonkMod.config.get(categoryName, parameter.name, s, null, Property.Type.INTEGER).getInt();
					break;
				case FLOAT:
					MonkMod.config.get(categoryName, parameter.name, s, null, Property.Type.DOUBLE).getDouble();
					break;
				case STRING:
					MonkMod.config.get(categoryName, parameter.name, s, null, Property.Type.STRING).getString();
					break;
				case STRINGLIST:
					MonkMod.config.getStringList(parameter.name, categoryName, s.split(","), null);
					break;
			}
		}
	}

	public FactoryEntry<T> setString(String key, String value) {
		values.put(getParam(key, Factory.Type.STRING), value);
		return this;
	}

	public FactoryEntry<T> setInt(String key, Integer value) {
		values.put(getParam(key, Factory.Type.INTEGER), value.toString());
		return this;
	}

	public FactoryEntry<T> setFloat(String key, Float value) {
		values.put(getParam(key, Factory.Type.FLOAT), value.toString());
		return this;
	}

	public FactoryEntry<T> setStringList(String key, String... value) {
		values.put(getParam(key, Factory.Type.STRINGLIST), Stream.of(value).collect(Collectors.joining(",")));
		return this;
	}


	public Factory.Parameter getParam(String key, Factory.Type expectedType) {
		for (Factory.Parameter parameter : factory.parameterList) {
			if (parameter.name.equals(key)) {
				if (parameter.type != expectedType && !(parameter.type == Factory.Type.FLOAT && expectedType == Factory.Type.INTEGER) ) {
					throw new IllegalArgumentException(key + " does not match expected type " + expectedType);
				}
				return parameter;
			}
		}
		throw new IllegalArgumentException("Illegal key: " + key);
	}
}
