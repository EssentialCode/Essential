package zenith.essential.client.fx;

import java.util.ArrayDeque;
import java.util.Queue;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.client.model.b3d.B3DModel.Vertex;
import zenith.essential.common.EssentialLogger;
import zenith.essential.common.lib.GeneralConstants;

public class FXEssence extends EntityFX {

	public static final ResourceLocation particles = new ResourceLocation(GeneralConstants.MOD_ID, "textures/misc/essence.png");

	public static Queue<FXEssence> queuedRenders = new ArrayDeque<FXEssence>();

	// Queue values
	float f;
	float f1;
	float f2;
	float f3;
	float f4;
	float f5;

	public FXEssence(World world, double x, double y, double z, float size, float red, float green, float blue, int m) {
		super(world, x, y, z, 0.0D, 0.0D, 0.0D);

		particleRed = red;
		particleGreen = green;
		particleBlue = blue;
		particleAlpha = 0.5F; // So MC renders us on the alpha layer, value not actually used
		particleGravity = 0;
		motionX = motionY = motionZ = 0;
		particleScale *= size;
		particleMaxAge = 3 * m;
		multiplier = m;
		setSize(0.01F, 0.01F);
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
	}
	
	public void setSpeed(double x, double y, double z) {
		motionX = x;
		motionY = y;
		motionZ = z;
	}

	public static void dispatchQueuedRenders() {
		EssentialParticleHandler.essenceFxCount = 0;

		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

		for(FXEssence essenceFx : queuedRenders){
			essenceFx.renderQueued();
		}
		queuedRenders.clear();
	}

	private void renderQueued() {
		Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer renderer = tessellator.getWorldRenderer();
		Minecraft.getMinecraft().renderEngine.bindTexture(particles);
		EssentialLogger.quickInfo(particles.getResourcePath());
//		renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
		renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

		EssentialParticleHandler.essenceFxCount++;

		int part = particle + particleAge/multiplier;

		float uStart = part % 8 / 8.0F;
		float uEnd = uStart + 0.0624375F*2;
		float vStart = 2 * 1f / 4f;
		float vEnd = vStart + 1f/8f;
		
//		EssentialLogger.quickInfo("uStart: " + uStart + " uEnd: " + uEnd + " vStart: " + vStart + " vEnd: " + vEnd);
		
		float scale = 1F * particleScale;
		if (shrink) scale *= (particleMaxAge-particleAge+1)/(float)particleMaxAge;
		float offsetX = (float)(prevPosX + (posX - prevPosX) * f - interpPosX);
		float offsetY = (float)(prevPosY + (posY - prevPosY) * f - interpPosY);
		float offsetZ = (float)(prevPosZ + (posZ - prevPosZ) * f - interpPosZ);
		float float1 = 1.0F;
		
		renderer.pos(offsetX - f1 * scale - f4 * scale, offsetY - f2 * scale, offsetZ - f3 * scale - f5 * scale)
			.tex(uEnd, vEnd)
//			.color(particleRed * float1, particleGreen * float1, particleBlue * float1, 1)
			.endVertex();
		renderer.pos(offsetX - f1 * scale + f4 * scale, offsetY + f2 * scale, offsetZ - f3 * scale + f5 * scale)
			.tex(uEnd, vStart)
//			.color(particleRed * float1, particleGreen * float1, particleBlue * float1, 1)
			.endVertex();
		renderer.pos(offsetX + f1 * scale + f4 * scale, offsetY + f2 * scale, offsetZ + f3 * scale + f5 * scale)
			.tex(uStart, vStart)
//			.color(particleRed * float1, particleGreen * float1, particleBlue * float1, 1)
			.endVertex();
		renderer.pos(offsetX + f1 * scale - f4 * scale, offsetY - f2 * scale, offsetZ + f3 * scale - f5 * scale)
			.tex(uStart, vEnd)
//			.color(particleRed * float1, particleGreen * float1, particleBlue * float1, 1)
			.endVertex();

		tessellator.draw();
	}

	@Override
	public void renderParticle(WorldRenderer renderer, Entity p_180434_2_, float f, float f1, float f2, float f3, float f4, float f5) {
		this.f = f;
		this.f1 = f1;
		this.f2 = f2;
		this.f3 = f3;
		this.f4 = f4;
		this.f5 = f5;

		queuedRenders.add(this);
	}

	@Override
	public void onUpdate() {
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;

		if (particleAge++ >= particleMaxAge){
//			EssentialLogger.quickInfo("im ded");
			setDead();
		}

		motionY -= 0.04D * particleGravity;

//		if (!noClip)
//			pushOutOfBlocks(posX, (getEntityBoundingBox().minY + getEntityBoundingBox().maxY) / 2.0D, posZ);

		posX += motionX;
		posY += motionY;
		posZ += motionZ;
//		EssentialLogger.quickInfo("x: " + posX + " y: " + posY + " z: " + posZ);

		if (slowdown) {
			motionX *= 0.908000001907348633D;
			motionY *= 0.908000001907348633D;
			motionZ *= 0.908000001907348633D;

			if (isCollided) {
				motionX *= 0.69999998807907104D;
				motionZ *= 0.69999998807907104D;
			}
		}
	}

	public void setGravity(float value) {
		particleGravity = value;
	}

	public int multiplier = 2;
	public boolean shrink = true;
	public int particle = 16;
	public boolean tinkle = false;
	public boolean slowdown = true;
	public int currentColor = 0;
}