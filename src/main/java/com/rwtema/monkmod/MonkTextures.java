package com.rwtema.monkmod;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ItemLayerModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.rwtema.monkmod.MonkMod.ITEM_MONK_BASE;

@SideOnly(Side.CLIENT)
public class MonkTextures {
	static final String MEDITATE = MonkMod.MODID + ":icon/meditate";
	private static TextureMap map;

	public static void init() {
		MinecraftForge.EVENT_BUS.register(MonkTextures.class);
		ModelResourceLocation modelResourceLocation = getModelLocation();
		ModelLoader.setCustomModelResourceLocation(ITEM_MONK_BASE, 0, modelResourceLocation);
		ModelBakery.registerItemVariants(ITEM_MONK_BASE, modelResourceLocation);
		ModelLoader.setCustomMeshDefinition(ITEM_MONK_BASE, stack -> modelResourceLocation);
	}

	@Nonnull
	private static ModelResourceLocation getModelLocation() {
		return new ModelResourceLocation(Validate.notNull(ITEM_MONK_BASE.getRegistryName()).toString(), "inventory");
	}

	@SubscribeEvent

	public static void registerTexture(@Nonnull TextureStitchEvent.Pre event) {
		map = event.getMap();
		ModContainer containerFor = FMLCommonHandler.instance().findContainerFor(MonkMod.instance);
		CraftingHelper.findFiles(containerFor, "assets/" + MonkMod.MODID + "/textures/icon", null, (root, file) -> {
			if ("png".equals(FilenameUtils.getExtension(file.toString()))) {
				String baseName = FilenameUtils.getBaseName(file.toString());
				map.registerSprite(new ResourceLocation(MonkMod.MODID, "icon/" + baseName));
			}
			return true;
		}, true, true);
	}

	@SuppressWarnings("unchecked")
	@SubscribeEvent
	public static void registerModels(@Nonnull ModelBakeEvent event) {
		ModelResourceLocation modelResourceLocation = getModelLocation();
		event.getModelRegistry().putObject(modelResourceLocation, new Model());
	}

	private static class Model implements IBakedModel {
		Map<String, IBakedModel> modelMap = new HashMap<>();

		@Nonnull
		@Override
		public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
			return getDefaultTex().getQuads(state, side, rand);
		}

		private IBakedModel getDefaultTex() {
			return getOrLoadModel(MEDITATE);
		}

		@Override
		public boolean isAmbientOcclusion() {
			return getDefaultTex().isAmbientOcclusion();
		}

		@Override
		public boolean isGui3d() {
			return getDefaultTex().isAmbientOcclusion();
		}

		@Override
		public boolean isBuiltInRenderer() {
			return getDefaultTex().isBuiltInRenderer();
		}

		@Nonnull
		@Override
		public TextureAtlasSprite getParticleTexture() {
			return getDefaultTex().getParticleTexture();
		}

		@Nonnull
		@Override
		public ItemOverrideList getOverrides() {
			return new ItemOverrideList(ImmutableList.of()) {
				@Nonnull
				@Override
				public IBakedModel handleItemState(@Nonnull IBakedModel originalModel, ItemStack stack, @Nullable World world, @Nullable EntityLivingBase entity) {
					NBTTagCompound tagCompound = stack.getTagCompound();
					String icon = tagCompound != null ? tagCompound.getString("icon") : "";
					return getOrLoadModel(icon);
				}
			};
		}

		protected IBakedModel getOrLoadModel(String icon) {
			return modelMap.computeIfAbsent(icon, s -> new MyIBakedModel(map.getAtlasSprite(s)));
		}
	}

	private static class MyIBakedModel implements IBakedModel {
		final TextureAtlasSprite sprite;
		final List<BakedQuad> quads;

		private MyIBakedModel(TextureAtlasSprite sprite) {
			this.sprite = sprite;
			quads = ItemLayerModel.getQuadsForSprite(0, sprite, DefaultVertexFormats.ITEM, Optional.of(TRSRTransformation.identity()));
		}

		@Override
		public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
			return quads;
		}

		@Override
		public boolean isAmbientOcclusion() {
			return false;
		}

		@Override
		public boolean isGui3d() {
			return false;
		}

		@Override
		public boolean isBuiltInRenderer() {
			return false;
		}

		@Nonnull
		@Override
		public TextureAtlasSprite getParticleTexture() {
			return sprite;
		}

		@Nonnull
		@Override
		public ItemOverrideList getOverrides() {
			return ItemOverrideList.NONE;
		}
	}
}