package com.rwtema.monkmod.abilities;

import net.minecraft.entity.ai.attributes.IAttribute;

import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.UUID;

public class MonkAbilityAttribute extends MonkAbility {
	public final static HashSet<UUID> uuids = new HashSet<>();
	public final UUID uuid;
	public final IAttribute attribute;
	public final double modifierPerLevel;
	public final int operation;

	public MonkAbilityAttribute(String name, int maxlevel, IAttribute attribute, double modifierPerLevel, int operation) {
		super(name, maxlevel);
		this.uuid = generate(name);
		this.attribute = attribute;
		this.modifierPerLevel = modifierPerLevel;
		this.operation = operation;

		uuids.add(uuid);
	}


	public UUID generate(String name) {
		return UUID.nameUUIDFromBytes(name.getBytes(Charset.defaultCharset()));

	}

	public double getAmount(int level) {
		return modifierPerLevel * (1 + level);
	}
}
