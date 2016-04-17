package zenith.essential.client.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import zenith.essential.common.EssentialLogger;
import zenith.essential.common.block.BlockBase;
import zenith.essential.common.block.EssentialBlocks;
import zenith.essential.common.tile.TileEntityAltar;

public class RenderAltarAttuned extends EssentialTileEntityRenderer<TileEntityAltar> {

	public RenderAltarAttuned() {
		super(EssentialBlocks.altarAttuned);
	}
	
	@Override
	public void renderCustom(TileEntityAltar altar){
		ResourceLocation emblem = altar.getEmblem();
		net.minecraft.client.renderer.RenderHelper.enableStandardItemLighting();
		GlStateManager.pushMatrix();
		renderEmblem(emblem);
        GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		GlStateManager.rotate(90, 0, 1, 0);
		renderEmblem(emblem);
        GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		GlStateManager.rotate(180, 0, 1, 0);
		renderEmblem(emblem);
        GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		GlStateManager.rotate(270, 0, 1, 0);
		renderEmblem(emblem);
        GlStateManager.popMatrix();
	}
	
	private void renderEmblem(ResourceLocation emblem){
		Tessellator tessellator = Tessellator.getInstance();
        bindTexture(emblem);
        WorldRenderer renderer = tessellator.getWorldRenderer();
        renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        float scale = 1f/8f;
        float pixel = 1f/16f;
        double full = 1.0/8.0;
        float offset = 8 * pixel + (1.1f * (pixel/10));
        float vHeight = 0.47f;
        renderer.pos(-scale, +scale + vHeight, offset).tex(0, 0).endVertex();
        renderer.pos(-scale, -scale + vHeight, offset).tex(0, 1).endVertex();
        renderer.pos(+scale, -scale + vHeight, offset).tex(1, 1).endVertex();
        renderer.pos(+scale, +scale + vHeight, offset).tex(1, 0).endVertex();
        tessellator.draw();
	}


}
