package com.rwtema.monkmod.advancements;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class JSonObjBuilder {
	@Nonnull
	private final JsonObject jsonObject = new JsonObject();

	public static void writeJSon(@Nonnull File file, JsonObject model) {
		try (FileWriter writer = new FileWriter(file.getPath())) {
			JsonWriter jsonWriter = new JsonWriter(writer);
			jsonWriter.setIndent("  ");
			jsonWriter.setLenient(true);
			Streams.write(model, jsonWriter);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static JSonObjBuilder json() {
		return new JSonObjBuilder();
	}

	@Nonnull
	public JsonObject build() {
		return jsonObject;
	}

	@Nonnull
	public JSonObjBuilder add(@Nonnull String property, JsonElement value) {
		jsonObject.add(property, value);
		return this;
	}

	@Nonnull
	public JSonObjBuilder add(@Nonnull String property, @Nonnull JSonObjBuilder value) {
		jsonObject.add(property, value.build());
		return this;
	}

	@Nonnull
	public JSonObjBuilder add(@Nonnull String property, String value) {
		jsonObject.addProperty(property, value);
		return this;
	}

	@Nonnull
	public JSonObjBuilder add(@Nonnull String property, Number value) {
		jsonObject.addProperty(property, value);
		return this;
	}

	@Nonnull
	public JSonObjBuilder add(@Nonnull String property, Boolean value) {
		jsonObject.addProperty(property, value);
		return this;
	}

	@Nonnull
	public JSonObjBuilder add(@Nonnull String property, Character value) {
		jsonObject.addProperty(property, value);
		return this;
	}
}
