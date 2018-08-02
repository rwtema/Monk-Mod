package com.rwtema.monkmod.debug;

import com.google.common.collect.Maps;
import com.rwtema.monkmod.MonkMod;
import com.rwtema.monkmod.advancements.JSonObjBuilder;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

public class CreateJSons {
	public static void create() {
		createAdvancements();
		createItemModels();

		ArrayList<String> strings = new ArrayList<>();
		for (int i = 0; i <= 20; i++) {
			strings.add("monk.advancements.level." + i + "=Monk Level " + i);
			strings.add("monk.advancements.level." + i + ".desc=");
			strings.add("monk.advancements.reward." + i + "=Awakened Skills: " + i);
			strings.add("monk.advancements.reward." + i + ".desc=");
		}
		MonkMod.logger.info("\n" + strings.stream().collect(Collectors.joining("\n")));

	}

	private static void createItemModels() {
		File dir = new File("C:\\extrautils\\modjam2018\\src\\main\\resources\\assets\\monk\\models\\item");

		for (int level = 0; level <= 21; level++) {
			JSonObjBuilder.writeJSon(new File(dir, Validate.notNull(MonkMod.ITEM_MONK_BASE.getRegistryName()).getResourcePath() + "_" + level + ".json"), JSonObjBuilder.json()
					.add("parent", "item/generated")
					.add("textures", JSonObjBuilder.json()
							.add("layer0", MonkMod.MODID + ":monk_level_icon_" + level)
					)
					.build()
			);
		}
	}

	private static void createAdvancements() {
		File dir = new File("C:\\extrautils\\modjam2018\\src\\main\\resources\\assets\\monk\\advancements\\monk");

		JSonObjBuilder.writeJSon(new File(dir, "root.json"), JSonObjBuilder.json()
				.add("display", JSonObjBuilder.json()
						.add("icon", JSonObjBuilder.json()
								.add("item", Validate.notNull(MonkMod.ITEM_MONK_BASE.getRegistryName()).toString())
								.add("data", 0))
						.add("title", JSonObjBuilder.json()
								.add("translate", "monk.advancements.level.0"))
						.add("description", JSonObjBuilder.json()
								.add("translate", "monk.advancements.level.0.desc"))
						.add("background", "monk:textures/advancements/advancement_background.png")
						.add("show_toast", true)
						.add("announce_to_chat", false)
						.add("hidden", false)
				)
				.add("criteria", JSonObjBuilder.json()
						.add("level", JSonObjBuilder.json()
								.add("trigger", "monk:levelup")
								.add("conditions", JSonObjBuilder.json()
										.add("level", 0))
						)
				).build());

		for (int level = 1; level <= 20; level++) {
			JSonObjBuilder.writeJSon(new File(dir, "level_" + level + ".json"), JSonObjBuilder.json()
					.add("display", JSonObjBuilder.json()
							.add("icon", JSonObjBuilder.json()
									.add("item", Validate.notNull(MonkMod.ITEM_MONK_BASE.getRegistryName()).toString())
									.add("data", level))
							.add("title", JSonObjBuilder.json()
									.add("translate", "monk.advancements.level." + level))
							.add("description", JSonObjBuilder.json()
									.add("translate", "monk.advancements.level." + level + ".desc"))

							.add("show_toast", true)
							.add("announce_to_chat", true)
							.add("hidden", false)
					)
					.add("parent", level == 1 ? "monk:monk/root" : "monk:monk/level_" + (level - 1))
					.add("criteria", JSonObjBuilder.json()
							.add("level", JSonObjBuilder.json()
									.add("trigger", "monk:levelup")
									.add("conditions", JSonObjBuilder.json()
											.add("level", level))
							)
					).build());

			JSonObjBuilder.writeJSon(new File(dir, "level_reward_" + level + ".json"), JSonObjBuilder.json()
					.add("display", JSonObjBuilder.json()
							.add("icon", JSonObjBuilder.json()
									.add("item", Validate.notNull(MonkMod.ITEM_MONK_BASE.getRegistryName()).toString())
									.add("data", 21))
							.add("title", JSonObjBuilder.json()
									.add("translate", "monk.advancements.reward." + level))
							.add("description", JSonObjBuilder.json()
									.add("translate", "monk.advancements.reward." + level + ".desc"))

							.add("show_toast", false)
							.add("announce_to_chat", false)
							.add("hidden", true)

					)
					.add("parent", level == 1 ? "monk:monk/root" : "monk:monk/level_" + (level - 1))

					.add("criteria", JSonObjBuilder.json()
							.add("level", JSonObjBuilder.json()
									.add("trigger", "monk:levelup")
									.add("conditions", JSonObjBuilder.json()
											.add("level", level))
							)
					).build());
		}
	}


	public class AdvDummy {
		final DisplayDummy display;
		@Nonnull
		HashMap<String, CriteriaDummy> criteria;

		public AdvDummy(int level) {
			this.display = new DisplayDummy();
			display.icon.data = level;
			display.title.translate = "monk.advancements.level." + level;
			display.description.translate = "monk.advancements.level." + level + ".desc";
			display.parent = level == 1 ? "monk:monk/root" : "monk:monk/level_" + level;
			CriteriaDummy criteriaDummy = new CriteriaDummy();
			criteriaDummy.level = level;
			this.criteria = Maps.newHashMap();
			criteria.put("level", criteriaDummy);
		}

		public class DisplayDummy {
			@Nonnull
			final
			ItemDummy icon = new ItemDummy();
			@Nonnull
			final
			StringDummy title = new StringDummy();
			@Nonnull
			final
			StringDummy description = new StringDummy();
			String parent;
			boolean show_toast = true;
			boolean announce_to_chat = true;

			public class ItemDummy {
				@Nonnull
				String icon = Validate.notNull(MonkMod.ITEM_MONK_BASE.getRegistryName()).toString();
				int data;
			}

			public class StringDummy {
				String translate;
			}
		}

		public class CriteriaDummy {
			@Nonnull
			String trigger = "monk:levelup";
			int level;
		}
	}


}
