package com.rwtema.monkmod.helper;

@SuppressWarnings("deprecation")
public class TranslateHelper {
	public static String translateKey(String key) {
		return net.minecraft.util.text.translation.I18n.translateToLocal(key);
	}

	public static boolean canTranslate(String key) {
		return net.minecraft.util.text.translation.I18n.canTranslate(key);
	}
}
