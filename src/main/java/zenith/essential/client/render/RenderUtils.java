package zenith.essential.client.render;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
// TODO: fix this
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zenith.essential.client.handler.TickHandler;
import zenith.essential.client.proxy.ClientProxy;
import zenith.essential.common.EssentialLogger;
import zenith.essential.common.block.BlockBase;
import zenith.essential.common.capability.IInventoryHandler;
import zenith.essential.common.tile.TileEntityBase;

import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("deprecation")
@SideOnly(Side.CLIENT)
public final class RenderUtils {

    public static void renderItemDefault(ItemStack is, int rotation, float scale) {
        if (is != null) {
            GlStateManager.pushMatrix();

            RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
            GlStateManager.scale(scale, scale, scale);
            if (rotation != 0) {
                GlStateManager.rotate(rotation, 0F, 1F, 0F);
            }

            renderItem.renderItem(is, ItemCameraTransforms.TransformType.NONE);

            GlStateManager.popMatrix();
        }
    }

    public static void renderItemCustom(ItemStack is, int rotation, float scale, int rotationOffset) {
        if (is != null) {
            GlStateManager.pushMatrix();

            GlStateManager.scale(scale, scale, scale);
            if (rotation != 0) {
                GlStateManager.rotate(rotation, 0F, 1F, 0F);
            }

            customRenderItem(is, rotationOffset);

            GlStateManager.popMatrix();
        }
    }

    public static void customRenderItem(ItemStack is, int rotationOffset) {
        RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
        TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();

        IBakedModel ibakedmodel = renderItem.getItemModelMesher().getItemModel(is);

        textureManager.bindTexture(TextureMap.locationBlocksTexture);
        textureManager.getTexture(TextureMap.locationBlocksTexture).setBlurMipmap(false, false);
        preTransform(renderItem, is, rotationOffset);
        GlStateManager.enableRescaleNormal();
        GlStateManager.alphaFunc(516, 0.1F);
//        GlStateManager.enableBlend();
//        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.pushMatrix();
        ibakedmodel = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(ibakedmodel, ItemCameraTransforms.TransformType.NONE);

        renderItem.renderItem(is, ibakedmodel);
        if(is.stackSize > 1){
			GL11.glTranslatef(-0.07f, 0.07f, -0.03f);
			renderItem.renderItem(is, ibakedmodel);
			GL11.glTranslatef(0.07f, -0.07f, 0.03f);
        }
        GlStateManager.cullFace(1029);
        GlStateManager.popMatrix();
        GlStateManager.disableRescaleNormal();
//        GlStateManager.disableBlend();
        textureManager.bindTexture(TextureMap.locationBlocksTexture);
        textureManager.getTexture(TextureMap.locationBlocksTexture).restoreLastBlurMipmap();
    }

    private static void preTransform(RenderItem renderItem, ItemStack stack, int rotationOffset) {
        IBakedModel ibakedmodel = renderItem.getItemModelMesher().getItemModel(stack);
        Item item = stack.getItem();

        if (item != null) {
            boolean flag = ibakedmodel.isGui3d();

            if (!flag) {
                GlStateManager.scale(2.0F, 2.0F, 2.0F);
            }

			float rotation = (ClientProxy.TICK_HANDLER.getRotationTicks() + rotationOffset) % 360;
			GlStateManager.rotate(rotation, 0, 1, 0);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }




    /**
     * Return the interface handler that the player is currently pointing at
     * @param te
     * @return
     */
    public static IInventoryHandler getFacingInterfaceHandle(TileEntityBase te, BlockBase block) {
        MovingObjectPosition mouseOver = Minecraft.getMinecraft().objectMouseOver;
        if (mouseOver != null && te.getPos().equals(mouseOver.getBlockPos())) {
            EnumFacing directionHit = mouseOver.sideHit;
            double sx = mouseOver.hitVec.xCoord - te.getPos().getX();
            double sy = mouseOver.hitVec.yCoord - te.getPos().getY();
            double sz = mouseOver.hitVec.zCoord - te.getPos().getZ();
            EnumFacing front = block.getFrontDirection(te.getWorld().getBlockState(te.getPos()));
            double sx2 = TileEntityBase.calculateHitX(sx, sy, sz, directionHit, front);
            double sy2 = TileEntityBase.calculateHitY(sx, sy, sz, directionHit, front);
            

            directionHit = block.worldToBlockSpace(te.getWorld(), te.getPos(), mouseOver.sideHit);
            for (IInventoryHandler handle : te.getInventoryHandlers()) {
                if (handle.getSide() == directionHit && handle.getMinX() <= sx2 && sx2 <= handle.getMaxX() && handle.getMinY() <= sy2 && sy2 <= handle.getMaxY()) {
                	return handle;
                } else {
//                	EssentialLogger.quickInfo(String.format(
//                			"%s? %s; x: %f, minX: %f, maxX: %f", 
//                			handle.getSide().getName(),
//                			directionHit.getName(),
//                			sx2,
//                			handle.getMinX(),
//                			handle.getMaxX()
//                			));
                }
            }
        }
        return null;
    }

    public static void renderInterfaceHandles(TileEntityBase te, IInventoryHandler selectedHandle, Vec3 textOffset) {
        for (IInventoryHandler handle : te.getInventoryHandlers()) {
            boolean selected = selectedHandle == handle;
            ItemStack ghosted = null;
            ItemStack heldItem = Minecraft.getMinecraft().thePlayer.getHeldItem();
            ItemStack stackInSlot = handle.getCurrentStack();
            if (selected && heldItem != null && stackInSlot == null) {
                if (handle.acceptAsInput(heldItem)) {
                    ghosted = heldItem;
                }
            }
            renderItemStackInWorld(handle.getRenderOffset(), selected, handle.isCrafting(), ghosted, stackInSlot, handle.getScale(), handle.getRotationOffset());
        }
        if (Minecraft.getMinecraft().thePlayer.isSneaking()) {
            for (IInventoryHandler handle : te.getInventoryHandlers()) {
                boolean selected = selectedHandle == handle;
                ItemStack ghosted = null;
                ItemStack heldItem = Minecraft.getMinecraft().thePlayer.getHeldItem();
                ItemStack stackInSlot = handle.getCurrentStack();
                if (selected && heldItem != null && stackInSlot == null) {
                    if (handle.acceptAsInput(heldItem)) {
                        ghosted = heldItem;
                    }
                }

//                boolean showRequirements = selected && handle.isCrafting();
//                List<String> present = Collections.emptyList();
//                List<String> missing = Collections.emptyList();
//                if (showRequirements) {
//                    long time = System.currentTimeMillis();
//                    if ((time - lastUpdateTime) > 300) {
//                        lastUpdateTime = time;
//                        PacketHandler.INSTANCE.sendToServer(new PacketGetInfoFromServer(new IngredientsInfoPacketServer(te.getPos())));
//                    }
//                    present = te.getIngredients();
//                    missing = te.getMissingIngredients();
//                }

                List<String> present = new ArrayList<String>();
                List<String> missing = new ArrayList<String>();
//                renderTextOverlay(handle.getRenderOffset(), present, missing, ghosted, stackInSlot, handle.getScale(), textOffset);
//                renderTextOverlay(handle.getRenderOffset(), present, missing, ghosted, stackInSlot, handle.getScale(), textOffset);
                if(handle.showText() && stackInSlot != null){
					renderFloatingText(
						(float) handle.getRenderOffset().xCoord, 
						(float) handle.getRenderOffset().yCoord,
						(float) handle.getRenderOffset().zCoord,
						handle.getScale(),
						String.valueOf(stackInSlot.stackSize));
                }
                
//    private void renderFloatingText(float x, float y, float z, float scale, float text){
            }
        }
    }

    private static void renderItemStackInWorld(Vec3 offset, boolean selected, boolean crafting, ItemStack ghosted, ItemStack stack, float scale, int rotationOffset) {
        if (ghosted != null) {
            stack = ghosted;
        }
        if (stack != null) {
            net.minecraft.client.renderer.RenderHelper.enableStandardItemLighting();
            GlStateManager.translate(offset.xCoord, offset.yCoord, offset.zCoord);

            if (ghosted != null || crafting) {
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(GL11.GL_ONE, GL11.GL_ONE);
            }
            renderItemCustom(stack, 0, 0.4f * scale, rotationOffset);
            if (selected && ghosted == null) {
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(GL11.GL_ONE, GL11.GL_ONE);
                GlStateManager.depthFunc(GL11.GL_EQUAL);
                renderItemCustom(stack, 0, 0.6f * scale, rotationOffset);
                GlStateManager.depthFunc(GL11.GL_LEQUAL);
                GlStateManager.disableBlend();
            }
            if (ghosted != null || crafting) {
                GlStateManager.disableBlend();
            }

            GlStateManager.translate(-offset.xCoord, -offset.yCoord, -offset.zCoord);
        }
    }
    
    private static void renderFloatingText(float x, float y, float z, float scale, String text){
    	FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;
		GL11.glPushMatrix();
		GL11.glTranslatef(x, y, z);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
		rotateToPlayer();
		GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
		GL11.glTranslatef(0f, 0.1f, 0f);
		GL11.glScalef(0.01F*scale, 0.01F*scale, 1F);
		fontRenderer.drawStringWithShadow(text, -fontRenderer.getStringWidth(text)/2, -fontRenderer.FONT_HEIGHT/2, 0xffffffff);
		GL11.glPopMatrix();
    }

    private static void renderTextOverlay(Vec3 offset, List<String> present, List<String> missing, ItemStack ghosted, ItemStack stack, float scale, Vec3 textOffset) {
		FontRenderer fontrenderer = Minecraft.getMinecraft().fontRendererObj;
        if (ghosted != null) {
            stack = ghosted;
        }
        if (stack != null) {
            net.minecraft.client.renderer.RenderHelper.enableStandardItemLighting();

            GL11.glPushMatrix();

//            GL11.glNormal3f(0.0F, 1.0F, 0.0F);


//            RenderManager
//            GlStateManager.translate(offset.xCoord + textOffset.xCoord, offset.yCoord + 0.5 + textOffset.yCoord, offset.zCoord + 0.5 + textOffset.zCoord);
            GL11.glTranslated(offset.xCoord + -0.5 + textOffset.xCoord, offset.yCoord + 0.5 + textOffset.yCoord, offset.zCoord + 0.2 + textOffset.zCoord);
            rotateToPlayer();
//    		GL11.glRotatef(180f, 0f, 0f, 1f);
            float f3 = 0.0075F;
            float factor = 1.5f;
            GlStateManager.scale(f3 * factor, -f3 * factor, f3);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();

            if ((!missing.isEmpty()) || (!present.isEmpty())) {
                GlStateManager.pushMatrix();
                GlStateManager.scale(.5, .5, .5);
                int y = 60 - 10;
                for (String s : missing) {
                    fontrenderer.drawStringWithShadow(s, 60, y, 0xffff0000);
                    y -= 10;
                }
                for (String s : present) {
                    fontrenderer.drawStringWithShadow(s, 60, y, 0xff00ff00);
                    y -= 10;
                }
                GlStateManager.popMatrix();
            }

            fontrenderer.drawStringWithShadow(String.valueOf(stack.stackSize), 40, 40, 0xffffffff);
            GlStateManager.enableDepth();
            GlStateManager.enableLighting();

            GL11.glPopMatrix();
        }
    }

//    public static void renderBillboardQuad(double scalex, double scaley, double offsetx, double offsety, IIcon icon) {
//        GL11.glPushMatrix();
//
//        rotateToPlayer();
//
//        Tessellator tessellator = Tessellator.instance;
//        tessellator.startDrawingQuads();
//        tessellator.addVertexWithUV(offsetx - scalex, offsety - scaley, 0, icon.getMinU(), icon.getMinV());
//        tessellator.addVertexWithUV(offsetx - scalex, offsety + scaley, 0, icon.getMinU(), icon.getMaxV());
//        tessellator.addVertexWithUV(offsetx + scalex, offsety + scaley, 0, icon.getMaxU(), icon.getMaxV());
//        tessellator.addVertexWithUV(offsetx + scalex, offsety - scaley, 0, icon.getMaxU(), icon.getMinV());
//        tessellator.draw();
//        GL11.glPopMatrix();
//    }


    public static void renderBillboardQuad(double scale, float vAdd1, float vAdd2) {
//        GL11.glPushMatrix();
//
//        rotateToPlayer();
//
//        Tessellator tessellator = Tessellator.instance;
//        tessellator.startDrawingQuads();
//        tessellator.addVertexWithUV(-scale, -scale, 0, 0, 0+vAdd1);
//        tessellator.addVertexWithUV(-scale, +scale, 0, 0, 0+vAdd1+vAdd2);
//        tessellator.addVertexWithUV(+scale, +scale, 0, 1, 0+vAdd1+vAdd2);
//        tessellator.addVertexWithUV(+scale, -scale, 0, 1, 0+vAdd1);
//        tessellator.draw();
//        GL11.glPopMatrix();
    }

    public static void rotateToPlayer() {
            float playerViewY = Minecraft.getMinecraft().getRenderManager().playerViewY;
            float playerViewX = Minecraft.getMinecraft().getRenderManager().playerViewX;
            GL11.glRotatef(-playerViewY, 0.0F, 1.0F, 0.0F);
    		GL11.glRotatef(playerViewX, 1.0F, 0.0F, 0.0F);            
    }
}