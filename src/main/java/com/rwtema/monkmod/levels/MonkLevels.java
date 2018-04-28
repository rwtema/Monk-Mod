package com.rwtema.monkmod.levels;

@SuppressWarnings("unused")
public class MonkLevels {
	public void loadAbilities() {

//		registerRequirement(new MonkRequirementBreakBlockBareHanded(0, (world, pos, state) -> state.getBlock().isWood(world, pos), 1, "wood_break"));
//
//		// Sapling
//		// Punch 50 tree logs with bare hands.
//		register(1, MINING, STRENGTH);
//		registerRequirement(new MonkRequirementBreakBlockBareHanded(1, (world, pos, state) -> state.getBlock().isWood(world, pos), 10, "wood_break"));
//
//		// Chicken
//		// Meditate while watching a sunset or sunrise.
//		register(2, HUNGER);
//		register(2, ARMOR);
//		registerRequirement(new MonkRequirementSunriseSunset(2, 10 * 20));
//
//		// Squid
//		// Swim to the bottom of an ocean.
//		register(3, WATER_BREATHING);
//		register(3, ARMOR);
//		registerRequirement(new MonkRequirementOcean(3, 20));
//
//		// Silverfish
//		// Break 5 stone blocks with your bare hands
//		registerRequirement(new MonkRequirementBreakBlockBareHanded(4, (world, pos, state) -> state.getBlock() == Blocks.STONE, 5, name));
//		register(4, MINING);
//		register(4, ARMOR, STRENGTH);
//
//
//		// Animal Friend
//		// Pet one of each vanilla animal (pet animals by right clicking on them with an empty hand)
//		registerRequirement(new MonkRequirementPet(5, 20));
//		register(5, PET);
//
//		// Ocelot
//		// From sunrise to sunset, run a mile in one day.
//		registerRequirement(new MyMonkRequirementSprint(name));
//		register(6, SPEED, JUMP);
////		register(5);
//
//		// Wolf
//		// Kill 10 hostile mobs with your bare fists
//		registerRequirement(new MonkRequirementKill(7, 5, name) {
//
//			@Override
//			protected boolean isValidEntity(LivingDeathEvent event) {
//				return event.getEntityLiving().isEntityUndead();
//			}
//		});
//		register(7, STRENGTH);
//		register(7, ARMOR);
//
//		// Golem
//		// Break an iron storage block with your bare fists
//		registerRequirement(new MonkRequirementBreakBlockBareHanded(8, (world, pos, state) -> state.getBlock() == Blocks.IRON_BLOCK, 1, name));
//		register(8, MINING, STRENGTH);
//
//		// Blaze
//		// Walk through fire.
//		registerRequirement(new MonkRequirementWalk(9, 10, name) {
//			@Override
//			protected void onGrant(EntityPlayerMP player) {
//				player.extinguish();
//				player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 20 * 10, 2));
//			}
//
//			@Override
//			public boolean satisfiesRequirements(EntityPlayerMP player) {
//				return ((player.isImmuneToFire() || player.isBurning()) && player.world.isFlammableWithin(player.getEntityBoundingBox()))
//						&& super.satisfiesRequirements(player);
//			}
//		});
//		register(9, FIRE);
//		register(9, ARMOR, SPEED);
//
//
//		// Ghast
//		// Break obsidian with bare hands
//		registerRequirement(new MonkRequirementBreakBlockBareHanded(10, (world, pos, state) -> state.getBlock() == Blocks.OBSIDIAN, 1, name));
//		register(10, MINING);
//		register(10, ARMOR, STRENGTH);
//
//
//		// Leap of Faith
//		registerRequirement(new MonkRequirementFall(11, 40));
//		register(11, SPEED, HEALTH, FEATHER_FALLING);
//
//		// Creeper
//		// Kiss a creeper
//		registerRequirement(new MonkRequirementKissCreeper(12));
//		register(12, EXPLOSION, KISS, HEALTH);
//
//		// Dodge Arrows
//		registerRequirement(new MonkRequirementArrow(13, 5));
//		register(13, ARROW_CATCH, HEALTH);
//
//		// Ocean
//		registerRequirement(new MonkRequirementWaterMeditation(14, 10 * 20));
//		register(14, WALK_ON_WATER);
//
//		// Wither Skeleton
//		registerRequirement(new MonkRequirementStare(15, 200));
//		register(15, WITHERING_STARE, HEALTH);
//
//
//		// Enderman
//		registerRequirement(new MonkRequirementMeditateEndermen(16, 3 * 60 * 20));
//		register(16, BLINK);
//
//		registerRequirement(new MonkRequirementBedrockSleep(17));
//		register(17, BLIND, MINING, STRENGTH);
//
//		// Kill entities while blind
//		registerRequirement(new MonkRequirementKill(18, 5, "kill_blind") {
//			@Override
//			protected boolean isValidEntity(LivingDeathEvent event) {
//				return event.getEntity() instanceof IMob;
//			}
//
//			@Override
//			protected void onGrant(EntityPlayerMP player) {
//				super.onGrant(player);
//				player.removePotionEffect(MobEffects.BLINDNESS);
//			}
//		});
//		register(18, BLIND, HUNGER, ARMOR, HEALTH);
//
//		registerRequirement(new MonkRequirementEnemyDefeat<>(19, EntityWither.class, "kill_wither", 1));
//		register(19, POTION_IMMUNITY);
//
//		registerRequirement(new MonkRequirementDescentAscent(20));
//		register(20, FLY);

	}


}
