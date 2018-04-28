package com.rwtema.monkmod.abilities;

import com.google.common.collect.HashMultimap;
import com.rwtema.monkmod.factory.Factory;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import java.nio.charset.Charset;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.UUID;

public abstract class MonkAbilityAttribute extends MonkAbility {
	public final static HashMultimap<UUID, MonkAbilityAttribute> uuids = HashMultimap.create();
	public final UUID uuid;
	public final IAttribute attribute;
	public final int operation;
	private double multiplier;

	public MonkAbilityAttribute(String name, IAttribute attribute, double multiplier, int operation) {
		super(name);
		this.uuid = generate(name);
		this.attribute = attribute;
		this.multiplier = multiplier;
		this.operation = operation;
		if (Factory.shouldRegister) {
			uuids.put(uuid, this);
		}
	}

	@Override
	public void tickServer(EntityPlayerMP player) {
		IAttributeInstance entityAttribute = player.getEntityAttribute(attribute);
		AttributeModifier modifier = entityAttribute.getModifier(uuid);
		if (canApply(player)) {
			double amount = getAmount(player);
			if (modifier == null || modifier.getAmount() != amount) {
				if (modifier != null) {
					entityAttribute.removeModifier(uuid);
				}
				AttributeModifier attributeModifier = new AttributeModifier(uuid, name, amount, operation);
				entityAttribute.applyModifier(attributeModifier);
			}
		} else if (modifier != null) {
			entityAttribute.removeModifier(modifier);
		}
		super.tickServer(player);
	}

	@Override
	protected String[] args() {
		return new String[]{NumberFormat.getPercentInstance(Locale.UK).format(multiplier)};
	}

	public UUID generate(String name) {
		return UUID.nameUUIDFromBytes(name.getBytes(Charset.defaultCharset()));

	}

	public double getAmount(EntityPlayer player) {
		return multiplier;
	}

	public abstract boolean canApply(EntityPlayer player);
}
