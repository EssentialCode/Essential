package zenith.essential.client.render;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import scala.actors.threadpool.Arrays;
import zenith.essential.api.essence.EnumEssenceAltarTier;
import zenith.essential.api.essence.EnumEssenceType;
import zenith.essential.common.block.EssentialBlocks;
import zenith.essential.common.lib.GeneralConstants;
import zenith.essential.common.tile.TileEntityAltar;

public class RenderAltarAttuned extends EssentialTileEntityRenderer<TileEntityAltar> {
	private final float PIXEL = 1f/16f;

	private ResourceLocation[] meter = new ResourceLocation[]{
			new ResourceLocation(GeneralConstants.MOD_ID, "textures/blocks/altar_meter_0.png"),
			new ResourceLocation(GeneralConstants.MOD_ID, "textures/blocks/altar_meter_1.png"),
			new ResourceLocation(GeneralConstants.MOD_ID, "textures/blocks/altar_meter_2.png"),
			new ResourceLocation(GeneralConstants.MOD_ID, "textures/blocks/altar_meter_3.png"),
			new ResourceLocation(GeneralConstants.MOD_ID, "textures/blocks/altar_meter_4.png"),
			new ResourceLocation(GeneralConstants.MOD_ID, "textures/blocks/altar_meter_5.png"),
			new ResourceLocation(GeneralConstants.MOD_ID, "textures/blocks/altar_meter_6.png"),
			new ResourceLocation(GeneralConstants.MOD_ID, "textures/blocks/altar_meter_7.png"),
			new ResourceLocation(GeneralConstants.MOD_ID, "textures/blocks/altar_meter_8.png"),
			new ResourceLocation(GeneralConstants.MOD_ID, "textures/blocks/altar_meter_9.png"),
			new ResourceLocation(GeneralConstants.MOD_ID, "textures/blocks/altar_meter_10.png")
			};
	
	private ResourceLocation meterOn = new ResourceLocation(GeneralConstants.MOD_ID, "textures/blocks/altar_light_on.png");
	private ResourceLocation meterOff = new ResourceLocation(GeneralConstants.MOD_ID, "textures/blocks/altar_light_off.png");

	public RenderAltarAttuned() {
		super(EssentialBlocks.altarAttuned);
	}
	
	@Override
	public void renderCustom(TileEntityAltar altar){
		ResourceLocation emblem = altar.getEmblem();
		ResourceLocation pegTex = altar.getEssenceType().getRunnerTexture();
//		net.minecraft.client.renderer.RenderHelper.enableStandardItemLighting();
		GL11.glPushMatrix();
		float base = 3.6f * PIXEL;
        float vHeight = 0.37f;
        int essence = altar.getEssenceLevel();
        List<Float> progress = getPegProgress(essence, EnumEssenceAltarTier.getTier(0).getCapacity());
		for(int i = 0; i < 4; i++){
			GL11.glPushMatrix();
			GlStateManager.rotate(90 * i, 0, 1, 0);
			renderEmblem(emblem);
			for(int j = 0; j <= 3; j += 1){
				if(j <= 3){
					float leftOffset = -4.5f * PIXEL;
					float topOffset = base + (j * PIXEL);
					float pegProgress = 0;
					if(progress.size() - 1 >= j){
						pegProgress = progress.get(j);
					}
					float actualProgress = pegProgress + 0.2f;
					renderPeg(leftOffset, topOffset, actualProgress, pegTex, j);
					leftOffset = 4.5f * PIXEL;
					renderPeg(leftOffset, topOffset, actualProgress, pegTex, j);
				}
			}
			GL11.glPopMatrix();
		}
		GL11.glPopMatrix();
	}
	
	private List<Float> getPegProgress(int essence, int maxEssence){
		float ess = (float) essence;
		float remaining = ess;
		float max = (float) maxEssence;
		float quarter = max / 4;
		List<Float> progress = new ArrayList<Float>();
		while(remaining > 0){
			if(remaining >= quarter){
				progress.add(1f);
				remaining -= quarter;
			} else {
				progress.add(remaining / quarter);
				remaining = 0;
			}
		}
		return progress;
	}
	
	private void renderEmblem(ResourceLocation emblem){
		Tessellator tessellator = Tessellator.getInstance();
        bindTexture(emblem);
		GlStateManager.disableLighting();
		GlStateManager.enableLighting();
        WorldRenderer renderer = tessellator.getWorldRenderer();
        renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        float scale = 1f/8f;
        float offset = 8 * PIXEL + (1.01f * (PIXEL/10));
        float vHeight = 0.47f;
        renderer.pos(-scale, +scale + vHeight, offset).tex(0, 0).endVertex();
        renderer.pos(-scale, -scale + vHeight, offset).tex(0, 1).endVertex();
        renderer.pos(+scale, -scale + vHeight, offset).tex(1, 1).endVertex();
        renderer.pos(+scale, +scale + vHeight, offset).tex(1, 0).endVertex();
        tessellator.draw();
		GlStateManager.disableLighting();
	}
	
	private void renderPeg(float leftOffset, float topOffset, float progress, ResourceLocation texture, int texOffset){

		Tessellator tessellator = Tessellator.getInstance();
		bindTexture(texture);
		WorldRenderer renderer = tessellator.getWorldRenderer();
		renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		GlStateManager.disableLighting();
//		GlStateManager.enableLighting();
		
		float sizeOffset = PIXEL/2;
		
		float posOffset = sizeOffset * 1.2f * progress - (0.1f * PIXEL);
		float offset = (6 * PIXEL + PIXEL/10f) + posOffset;

		float t0 = texOffset * PIXEL;
		float t1 = (texOffset + 1) * PIXEL;
		
		float[] sides = {5 * PIXEL, 5 * PIXEL, 14 * PIXEL, 14 * PIXEL};
		float[] tops = {2 * PIXEL, 2 * PIXEL, 9 * PIXEL, 9 * PIXEL};
		
		// because wool
		float sideTexOffset = sides[texOffset];
		float topTexOffset = tops[texOffset];
		
		float t0_side = t0 + sideTexOffset;
		float t1_side = t0_side + PIXEL;

		float t0_top = t0 + topTexOffset;
		float t1_top = t0_top + PIXEL;
		
		// front
		renderer.pos(leftOffset - sizeOffset, topOffset + sizeOffset, offset).tex(t0, t0).endVertex();
		renderer.pos(leftOffset - sizeOffset, topOffset - sizeOffset, offset).tex(t0, t1).endVertex();
		renderer.pos(leftOffset + sizeOffset, topOffset - sizeOffset, offset).tex(t1, t1).endVertex();
		renderer.pos(leftOffset + sizeOffset, topOffset + sizeOffset, offset).tex(t1, t0).endVertex();

		// up
		renderer.pos(leftOffset - sizeOffset, topOffset + sizeOffset, offset - sizeOffset * 2).tex(t0_top, t0_top).endVertex();
		renderer.pos(leftOffset - sizeOffset, topOffset + sizeOffset, offset ).tex(t0_top, t1_top).endVertex();
		renderer.pos(leftOffset + sizeOffset, topOffset + sizeOffset, offset ).tex(t1_top, t1_top).endVertex();
		renderer.pos(leftOffset + sizeOffset, topOffset + sizeOffset, offset - sizeOffset * 2).tex(t1_top, t0_top).endVertex();

		// left
		renderer.pos(leftOffset - sizeOffset, topOffset + sizeOffset, offset - sizeOffset * 2).tex(t0_side, t0_side).endVertex();
		renderer.pos(leftOffset - sizeOffset, topOffset - sizeOffset, offset - sizeOffset * 2).tex(t0_side, t1_side).endVertex();
		renderer.pos(leftOffset - sizeOffset, topOffset - sizeOffset, offset).tex(t1_side, t1_side).endVertex();
		renderer.pos(leftOffset - sizeOffset, topOffset + sizeOffset, offset).tex(t1_side, t0_side).endVertex();

		// down
		renderer.pos(leftOffset - sizeOffset, topOffset - sizeOffset, offset).tex(t0_side, t0_side).endVertex();
		renderer.pos(leftOffset - sizeOffset, topOffset - sizeOffset, offset - sizeOffset * 2).tex(t0_side, t1_side).endVertex();
		renderer.pos(leftOffset + sizeOffset, topOffset - sizeOffset, offset - sizeOffset * 2).tex(t1_side, t1_side).endVertex();
		renderer.pos(leftOffset + sizeOffset, topOffset - sizeOffset, offset).tex(t1_side, t0_side).endVertex();

		// right
		renderer.pos(leftOffset + sizeOffset, topOffset + sizeOffset, offset).tex(t0_side, t0_side).endVertex();
		renderer.pos(leftOffset + sizeOffset, topOffset - sizeOffset, offset).tex(t0_side, t1_side).endVertex();
		renderer.pos(leftOffset + sizeOffset, topOffset - sizeOffset, offset - sizeOffset * 2).tex(t1_side, t1_side).endVertex();
		renderer.pos(leftOffset + sizeOffset, topOffset + sizeOffset, offset - sizeOffset * 2).tex(t1_side, t0_side).endVertex();
		


        tessellator.draw();
		GlStateManager.disableLighting();
	}
	
	private void renderLight(float leftOffset, float topOffset, boolean isLit){
		Tessellator tessellator = Tessellator.getInstance();
		ResourceLocation txt = isLit ? meterOn : meterOff;
        bindTexture(txt);
        WorldRenderer renderer = tessellator.getWorldRenderer();
        renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        float scale = 1f/8f;
        float height = 1f * PIXEL / 2;
        float width =  1f * PIXEL;
        
        float texHeight = 6f * PIXEL;
        float texWidth  = 6f * PIXEL;
        
        float offset = 6 * PIXEL + PIXEL/10f;
        float vHeight = 1.5f * PIXEL;

        renderer.pos(leftOffset + width /2,topOffset + height + vHeight, offset).tex(0, 0).endVertex();
        renderer.pos(leftOffset + width /2,topOffset -height + vHeight, offset).tex(0, texHeight).endVertex();
        renderer.pos(leftOffset + width * 1.5f,topOffset -height + vHeight, offset).tex(texWidth, texHeight).endVertex();
        renderer.pos(leftOffset + width * 1.5f,topOffset + height + vHeight, offset).tex(texWidth, 0).endVertex();
        tessellator.draw();
		
	}

	private void renderGauge(float leftOffset, TileEntityAltar altar){
		Tessellator tessellator = Tessellator.getInstance();
        bindTexture(meter[altar.getMeterOutput()]);
        WorldRenderer renderer = tessellator.getWorldRenderer();
        renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        float scale = 1f/8f;
        float height = 2.3f * PIXEL;
        float width = 1.7f * PIXEL;
        
        float texHeight = 1f;
        float texWidth = 8f/16f;
        
        float offset = 6 * PIXEL + PIXEL/10f;
        float vHeight = 0.37f;

        renderer.pos(leftOffset + width /2, +height + vHeight, offset).tex(0, 0).endVertex();
        renderer.pos(leftOffset + width /2, -height + vHeight, offset).tex(0, texHeight).endVertex();
        renderer.pos(leftOffset + width * 1.5f, -height + vHeight, offset).tex(texWidth, texHeight).endVertex();
        renderer.pos(leftOffset + width * 1.5f, height + vHeight, offset).tex(texWidth, 0).endVertex();
        tessellator.draw();
	}


}
