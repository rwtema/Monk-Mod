package com.rwtema.monkmod.advancements;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.rwtema.monkmod.MonkMod;
import com.rwtema.monkmod.abilities.MonkAbility;
import com.rwtema.monkmod.config.MonkConfiguration;
import com.rwtema.monkmod.helper.TranslateHelper;
import com.rwtema.monkmod.levels.MonkLevelManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.storage.SaveFormatOld;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.stream.Collectors;

import static com.rwtema.monkmod.advancements.JSonObjBuilder.json;

public class MonkAdvancements {


	private static final HashSet<String> keys = new HashSet<>();

	public static void registerAdvancements(@Nonnull MinecraftServer server) {
		if (MonkMod.debug)
			keys.clear();
		try {
			loadData(server);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		if (MonkMod.debug) {
			//noinspection deprecation
			MonkMod.logger.info(keys.stream().sorted().map(s -> s + "=" + (TranslateHelper.canTranslate(s) ? TranslateHelper.translateKey(s) : "")).collect(Collectors.joining("\n", "Keys::\n", "\n")));
		}
	}

	private static void loadData(MinecraftServer server) throws IOException {
		SaveFormatOld activeAnvilConverter = (SaveFormatOld) server.getActiveAnvilConverter();
		File savesDirectory = activeAnvilConverter.savesDirectory;
		File worldFile = new File(savesDirectory, server.getFolderName());

		File directory = new File(new File(new File(worldFile, "data"), "advancements"), "monk");
		if (directory.exists()) {
			FileUtils.cleanDirectory(directory);
		} else if (!directory.mkdirs()) {
			throw new IOException("Unable to generate advancements");
		}


		JSonObjBuilder.writeJSon(new File(directory, "root.json"), json()
				.add("display", json()
						.add("icon", json()
								.add("item", Validate.notNull(MonkMod.ITEM_MONK_BASE.getRegistryName()).toString())
								.add("nbt", getAdd(MonkConfiguration.data[0].texture)))
						.add("title", serializeTextComponent(new TextComponentTranslation("monk.advancements.level.start")))
						.add("description", serializeTextComponent(new TextComponentTranslation("monk.advancements.level.start.desc")))
						.add("background", "monk:textures/advancements/advancement_background.png")
						.add("show_toast", true)
						.add("announce_to_chat", false)
						.add("hidden", false)
				)
				.add("criteria", json()
						.add("level", json()
								.add("trigger", "monk:levelup")
								.add("conditions", json()
										.add("level", 0))
						)
				).build());

		for (int level = 1; level <= MonkMod.MAX_LEVEL; level++) {
			JSonObjBuilder.writeJSon(new File(directory, "level_" + level + ".json"), json()
					.add("display", json()
							.add("icon", json()
									.add("item", Validate.notNull(MonkMod.ITEM_MONK_BASE.getRegistryName()).toString())
									.add("nbt", getAdd(MonkConfiguration.data[level].texture)))
							.add("title", serializeTextComponent(new TextComponentTranslation("monk.advancements.level", level)))
							.add("description", serializeTextComponent(MonkLevelManager.requirements.get(level).getDescriptionComponent()))
							.add("show_toast", true)
							.add("announce_to_chat", true)
							.add("hidden", false)
					)
					.add("parent", level == 1 ? "monk:root" : "monk:level_" + (level - 1))
					.add("criteria", json()
							.add("level", json()
									.add("trigger", "monk:levelup")
									.add("conditions", json()
											.add("level", level))
							)
					).build());

			MonkConfiguration.LevelData datum = MonkConfiguration.data[level];
			ITextComponent textComponent = null;
			for (MonkAbility ability : datum.toAdd) {
				if (textComponent == null) {
					textComponent = ability.getTextComponent();
				} else {
					textComponent.appendText("\n").appendSibling(ability.getTextComponent());
				}
			}

			if (textComponent != null)
				JSonObjBuilder.writeJSon(new File(directory, "level_reward_" + level + ".json"), json()
						.add("display", json()
								.add("icon", json()
										.add("item", Validate.notNull(MonkMod.ITEM_MONK_BASE.getRegistryName()).toString())
										.add("nbt", getAdd(MonkMod.MODID + ":icon/rewards")))
								.add("title", serializeTextComponent(new TextComponentTranslation("monk.advancements.reward", level)))
								.add("description", serializeTextComponent(textComponent))
								.add("show_toast", false)
								.add("announce_to_chat", false)
								.add("hidden", true)

						)
						.add("parent", level == 1 ? "monk:root" : "monk:level_" + (level - 1))

						.add("criteria", json()
								.add("level", json()
										.add("trigger", "monk:levelup")
										.add("conditions", json()
												.add("level", level))
								)
						).build());
		}
	}

	private static String getAdd(String textureName) {
		return "{\"icon\":\"" + textureName + "\"}";
	}

	@Nonnull
	private static JsonElement serializeTextComponent(ITextComponent component) {
		if (MonkMod.debug) {
			LinkedList<ITextComponent> textComponents = new LinkedList<>();
			textComponents.add(component);
			ITextComponent textComponent;
			while ((textComponent = textComponents.pollFirst()) != null) {
				if (textComponent instanceof TextComponentTranslation) {
					String key = ((TextComponentTranslation) textComponent).getKey();
					keys.add(key);
				}
				textComponents.addAll(textComponent.getSiblings());
			}
		}

		return new JsonParser().parse(ITextComponent.Serializer.componentToJson(component));
	}

}
