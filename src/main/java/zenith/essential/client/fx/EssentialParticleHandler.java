/* 
 * This file was heavily influenced by Vazkii's ParticleRenderDispatch from Psi
 * https://github.com/Vazkii/Psi/blob/master/src%2Fmain%2Fjava%2Fvazkii%2Fpsi%2Fclient%2Ffx%2FParticleRenderDispatcher.java 
 */
package zenith.essential.client.fx;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.profiler.Profiler;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class EssentialParticleHandler {

	public static int essenceFxCount = 0;
	public static int depthIgnoringWispFxCount = 0;

	@SubscribeEvent
	public void onRenderWorldLast(RenderWorldLastEvent event) {
		Tessellator tessellator = Tessellator.getInstance();

		Profiler profiler = Minecraft.getMinecraft().mcProfiler;

		GL11.glPushAttrib(GL11.GL_LIGHTING);
		GlStateManager.depthMask(false);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		GlStateManager.alphaFunc(GL11.GL_GREATER, 0.003921569F);
		GlStateManager.disableLighting();

		profiler.startSection("essential-essence");
		profiler.startSection("essence");
		FXEssence.dispatchQueuedRenders(tessellator);
//		profiler.endStartSection("wisp");
//		FXWisp.dispatchQueuedRenders(tessellator);
		profiler.endSection();
		profiler.endSection();

		GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1F);
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.disableBlend();
		GlStateManager.depthMask(true);
		GL11.glPopAttrib();
	}

}