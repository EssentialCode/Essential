package zenith.essential.client.render;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.Vec3;
import zenith.essential.common.EssentialLogger;
import zenith.essential.common.block.BlockBase;
import zenith.essential.common.capability.IInventoryHandler;
import zenith.essential.common.tile.TileEntityBase;

public class EssentialTileEntityRenderer<T extends TileEntityBase> extends TileEntitySpecialRenderer<T> {

	protected BlockBase block;

    protected Vec3 textOffset = new Vec3(0, 0, 0);

    public EssentialTileEntityRenderer(BlockBase block) {
        this.block = block;
    }

    @Override
    public void renderTileEntityAt(T tileEntity, double x, double y, double z, float partialTicks, int destroyStage) {

        IBlockState state = tileEntity.getWorld().getBlockState(tileEntity.getPos());
        if (!(state.getBlock() instanceof BlockBase)) {
            return;
        }

        GlStateManager.pushAttrib();
        GlStateManager.pushMatrix();

        GlStateManager.translate(x + .5, y, z + .5);
        GlStateManager.disableRescaleNormal();

        renderCustom(tileEntity);
        renderHandlers(tileEntity);

        GlStateManager.popMatrix();
        GlStateManager.popAttrib();
    }
    
    protected void rotateFacing(IBlockState state){
        Vec3 rotationData = block.getRotationData(state);
        if(Math.abs(rotationData.xCoord ) > 0){
        	GlStateManager.rotate((float) rotationData.xCoord, 1, 0, 0);
        }
        if(Math.abs(rotationData.yCoord) > 0){
        	GlStateManager.rotate((float) rotationData.yCoord, 0, 1, 0);
        }
        if(Math.abs(rotationData.zCoord) > 0){
        	GlStateManager.rotate((float) rotationData.zCoord, 0, 0, 1);
        }
    }

    protected void renderHandlers(T tileEntity) {
        double distanceSq = Minecraft.getMinecraft().thePlayer.getDistanceSq(tileEntity.getPos());
        // TODO: add to config
        if (distanceSq > (16*16)) {
            return;
        }

        bindTexture(TextureMap.locationBlocksTexture);
        IInventoryHandler selectedHandler = RenderUtils.getFacingInterfaceHandle(tileEntity, block);
        RenderUtils.renderInterfaceHandles(tileEntity, selectedHandler, textOffset);
    }
    
    protected void renderCustom(T tileEntity){
    }


}
