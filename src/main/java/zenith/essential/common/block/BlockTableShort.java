package zenith.essential.common.block;

import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockTableShort extends BlockTable {
	public static final String name = "tableShort";


	public BlockTableShort(){
		this(name);
	}

	public BlockTableShort(String name){
		super(name);
		setBlockBounds();
	}
	
	private void setBlockBounds() {
		float pixel = 1F / 16;
		float minX = 0;
		float minY = 0;
		float minZ = 0;
		float maxX = 1F;
		float maxY = 12F * pixel;
		float maxZ = 1F;
		setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ){
		if(!world.isRemote && player.getCurrentEquippedItem().getItem() == Items.bowl){
			IBlockState newState = EssentialBlocks.altar.getDefaultState()
									.withProperty(BlockAltar.ROTATE, state.getValue(ROTATE))
									.withProperty(BlockAltar.WOOD_TYPE, state.getValue(WOOD_TYPE));
			world.setBlockState(pos, newState);
			if(!player.capabilities.isCreativeMode){
				ItemStack stack = player.getCurrentEquippedItem();
				stack.stackSize--;
			}
			return true;
		}
		return false;
	}

}
