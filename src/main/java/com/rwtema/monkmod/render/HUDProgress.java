package com.rwtema.monkmod.render;

import com.rwtema.monkmod.MonkMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

public class HUDProgress {
	public static final HUDProgress INSTANCE = new HUDProgress();
	private static final int FADE_IN_TIME = 20;
	private static final int FULL_TIME = 20;
	private static final float FADE_MULTIPLIER = 0.1F;
	private static final float GONE_TIME = FULL_TIME + 3.141F / FADE_MULTIPLIER / 2;
	private static final int DISPLAY = 64;
	private int progress;
	private float displayAngle;
	private int maxprogress;
	private int timeSinceLastUpdate = 10000;
	private float transparency;
	private ResourceLocation location = new ResourceLocation(MonkMod.MODID, "textures/circle.png");

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void clientTick(TickEvent.ClientTickEvent event) {
		if (!Minecraft.getMinecraft().isGamePaused() && event.phase == TickEvent.Phase.START) {

			timeSinceLastUpdate++;
			if (maxprogress >= 0) {
				float target = maxprogress == 0 ? 0 : (float) (progress < maxprogress ? ((progress * Math.PI * 2) / maxprogress) : Math.PI * 2);
				float dt = target - displayAngle;
				displayAngle = displayAngle + Math.min(Math.abs(dt), 0.1F) * Math.signum(dt);
			} else {
				displayAngle = 0;
				timeSinceLastUpdate = 50;
			}
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void hudDraw(RenderGameOverlayEvent.Post event) {
		if (event.getType() != RenderGameOverlayEvent.ElementType.CROSSHAIRS) return;
		if (maxprogress < 0) return;
		GuiIngameForge currentScreen = (GuiIngameForge) Minecraft.getMinecraft().ingameGUI;
		ScaledResolution resolution = event.getResolution();
		int size = resolution.getScaleFactor() * DISPLAY / 4;
		int cx = resolution.getScaledWidth()  - size;
		int cy = resolution.getScaledHeight() - size;

		float partialTicks = event.getPartialTicks();

		float v = timeSinceLastUpdate + partialTicks;
		float baseTransparency;
		if (v > GONE_TIME) {
			transparency = 0;
			return;
		} else if (v > FULL_TIME) {
			baseTransparency = MathHelper.cos((v - FULL_TIME) * FADE_MULTIPLIER);
		} else if (v < FADE_IN_TIME && transparency < 1) {
			baseTransparency = transparency + (1F / FADE_IN_TIME);
		} else {
			baseTransparency = 1;
		}

		transparency = baseTransparency;

		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.enableBlend();
		GlStateManager.color(1, 1, 1, 1);
//		GlStateManager.disableCull();

		float target = (float) ((progress * Math.PI * 2) / maxprogress);
		float dt =  target - displayAngle;
		float displayAngle2 = displayAngle + Math.min(Math.abs(dt), 0.1F) * Math.signum(dt);
		float partialAngle = displayAngle + (displayAngle2 - displayAngle) * partialTicks;

		Minecraft.getMinecraft().getTextureManager().bindTexture(location);

		Tessellator instance = Tessellator.getInstance();
		BufferBuilder buffer = instance.getBuffer();


		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);

		float r = 0, g = 0, b = 0.1F;
		if(target < displayAngle){
			r = 1;
		}else{
			g = 1;
		}
		for (int i = 0; i < 180; i++) {
			float angle1 = i / 180F * 2F * 3.141592F;
			float angle2 = (i + 1) / 180F * 2F * 3.141592F;
			if (angle1 > Math.max(target, partialAngle)) {
				break;
			} else if (angle2 > Math.max(target, partialAngle)) {
				angle2 = Math.max(target, partialAngle);
			}
			float t = baseTransparency * 1;
			if(angle1 > target && angle1 <= partialAngle){
				t *= 1-(angle1 - target)/ (partialAngle - target);
			} else 			if(angle1 > partialAngle && angle1 < target){
				t *= 1-(angle1 - partialAngle)/ (target- partialAngle);
			}


			addAnglePos(buffer, angle1, 1.0F, r, g, b, t, cx, cy, size);
			addAnglePos(buffer, angle1, 0.0F, r, g, b, t, cx, cy, size);
			addAnglePos(buffer, angle2, 0.0F, r, g, b, t, cx, cy, size);
			addAnglePos(buffer, angle2, 1.0F, r, g, b, t, cx, cy, size);
		}

		buffer.pos(cx - size / 2, cy + size / 2, 0).tex(0.5, 0.5).color(1, 1, 1, baseTransparency).endVertex();
		buffer.pos(cx + size / 2, cy + size / 2, 0).tex(1.0, 0.5).color(1, 1, 1, baseTransparency).endVertex();
		buffer.pos(cx + size / 2, cy - size / 2, 0).tex(1.0, 0.0).color(1, 1, 1, baseTransparency).endVertex();
		buffer.pos(cx - size / 2, cy - size / 2, 0).tex(0.5, 0.0).color(1, 1, 1, baseTransparency).endVertex();

		instance.draw();
//			handler.render(currentScreen, event.getResolution(), event.getPartialTicks());

	}

	@SideOnly(Side.CLIENT)
	private void addAnglePos(BufferBuilder buffer, float angle, float rad, float r, float g, float b, float a, int cx, int cy, int size) {

		float c = MathHelper.cos(angle - 3.141F / 2);
		float s = MathHelper.sin(angle - 3.141F / 2);

		buffer
				.pos(cx + MathHelper.clamp(c * 0.709F * rad, -0.5F, 0.5F) * size,
						cy + MathHelper.clamp(s * 0.709F * rad, -0.5F, 0.5F) * size,
						0)
				.tex(MathHelper.clamp(64 + c * 92 * rad, 0, 128) / 256.0,
						MathHelper.clamp(64 + s * 92 * rad, 0, 128) / 256.0)
				.color(r, g, b, a)
				.endVertex();
	}

	public void handle(int progress, int max) {
		this.progress = progress;
		this.maxprogress = max;
		timeSinceLastUpdate = 0;
	}
}
