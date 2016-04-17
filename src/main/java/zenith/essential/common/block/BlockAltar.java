package zenith.essential.common.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.Vec3;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockAltar extends BlockTableShort {
	public static final String name = "altar";
	
	public BlockAltar(){
		super(name);
		setBlockBounds();
	}

	private void setBlockBounds() {
		float minX = 0;
		float minY = 0;
		float minZ = 0;
		float maxX = 1F;
		float maxY = 1F;
		float maxZ = 1F;
		setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
	}

	@Override
	public Vec3 getRotationData(IBlockState state) {
		return new Vec3(0,0,0);
	}

	

}
