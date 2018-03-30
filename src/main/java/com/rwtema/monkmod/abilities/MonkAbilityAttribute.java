package com.rwtema.monkmod.abilities;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import org.apache.commons.lang3.Validate;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.UUID;

public abstract class MonkAbilityAttribute extends MonkAbility {
	public final static HashMap<UUID, MonkAbilityAttribute> uuids = new HashMap<>();
	public final UUID uuid;
	public final IAttribute attribute;
	public final int operation;
	public final double[] levels;

	public MonkAbilityAttribute(String name, IAttribute attribute, double[] levels, int operation) {
		super(name, levels.length);
		this.uuid = generate(name);
		this.attribute = attribute;
		this.levels = levels;
		this.operation = operation;
		Validate.isTrue(uuids.put(uuid, this) == null);
	}

	@Override
	public void tickServer(EntityPlayer player, int level) {
		IAttributeInstance entityAttribute = player.getEntityAttribute(attribute);
		AttributeModifier modifier = entityAttribute.getModifier(uuid);
		if (canApply(player)) {
			double amount = getAmount(level);
			if (modifier == null || modifier.getAmount() != amount) {
				AttributeModifier attributeModifier = new AttributeModifier(uuid, name, amount, operation);
				entityAttribute.applyModifier(attributeModifier);
			}
		} else if (modifier != null) {
			entityAttribute.removeModifier(modifier);
		}
		super.tickServer(player, level);
	}

	public UUID generate(String name) {
		return UUID.nameUUIDFromBytes(name.getBytes(Charset.defaultCharset()));

	}

	public double getAmount(int level) {
		return levels[level];
	}

	public abstract boolean canApply(EntityPlayer player);
}
