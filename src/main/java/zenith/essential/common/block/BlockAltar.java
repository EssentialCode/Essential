package zenith.essential.common.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockAltar extends BlockBase {
	public static final String name = "altar";
	
	public BlockAltar(){
		super(name, Material.wood);
		setHardness(1.0F);
		setResistance(2.5F);
		setStepSound(soundTypeWood);
		setBlockBounds();
	}

	private void setBlockBounds() {
		float pixel = 1F / 16;
		float minX = 2F * pixel;
		float minY = 0;
		float minZ = 2F * pixel;
		float maxX = 1F - 2F * pixel;
		float maxY = 12 * pixel;
		float maxZ = 1F - 2F * pixel;
		setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
	}

	@Override
	public boolean isFullCube() {
		return false;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override 
    @SideOnly(Side.CLIENT)
	public EnumWorldBlockLayer getBlockLayer() {
		return EnumWorldBlockLayer.CUTOUT;
	}
	
    @SideOnly(Side.CLIENT)
    public int getBlockColor()
    {
        return ColorizerFoliage.getFoliageColor(0.5D, 1.0D);
    }

    @SideOnly(Side.CLIENT)
    public int getRenderColor(IBlockState state)
    {
        return ColorizerFoliage.getFoliageColorBasic();
    }

    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass)
    {
        return BiomeColorHelper.getFoliageColorAtPos(worldIn, pos);
    }

	

}
