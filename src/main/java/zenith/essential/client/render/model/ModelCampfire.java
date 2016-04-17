package zenith.essential.client.render.model;

import java.util.Collections;
import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraftforge.client.model.ISmartBlockModel;
import zenith.essential.common.block.BlockCampfire;
import zenith.essential.common.block.EssentialBlocks;

public class ModelCampfire implements IBakedModel, ISmartBlockModel {

    IBakedModel solid;
    IBakedModel cutout;

    @Override
    public IBakedModel handleBlockState(IBlockState state) {
        if (solid == null) {
            BlockModelShapes models =  Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes();
//            solid = models.getModelForState(EssentialBlocks.furnaceBlock.getDefaultState().withProperty(FurnaceBlock.STATICMODEL, Boolean.TRUE));
            cutout = models.getModelForState(state);
        }

        if (net.minecraftforge.client.MinecraftForgeClient.getRenderLayer() == EnumWorldBlockLayer.SOLID) {
            return solid;
        } else {
            return cutout;
        }
    }

    @Override
    public List<BakedQuad> getFaceQuads(EnumFacing facing) {
        return Collections.emptyList();
    }

    @Override
    public List<BakedQuad> getGeneralQuads() {
        return Collections.emptyList();
    }

    @Override
    public boolean isAmbientOcclusion() {
        return true;
    }

    @Override
    public boolean isGui3d() {
        return true;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return null;
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return ItemCameraTransforms.DEFAULT;
    }
}