package zenith.essential.common.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zenith.essential.common.EssentialCreativeTab;
import zenith.essential.common.item.EssentialItemBlock;
import zenith.essential.common.lib.BlockHelper;

public abstract class BlockBase extends Block {

	public BlockBase(String name, Material material){
		super(material);
		
		setupAndRegister(name);
		setCreativeTab(EssentialCreativeTab.INSTANCE);
	}
	
	private Block setupAndRegister(String name){
		setUnlocalizedName(name);
		setRegistryName(name);
		GameRegistry.registerBlock(this, EssentialItemBlock.class, name);
		return this;
	}
	
	@SideOnly(Side.CLIENT)
    public void initModel() {
    }
	
	public List<IRecipe> getRecipes(){
		return new ArrayList<IRecipe>();
	}
	
	@Override
	public boolean isBlockNormalCube() {
		return false;
	}

	@Override
	public boolean isFullBlock(){
		return false;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
	public String getUnlocalizedIdentifier(int meta){
		return null;
	}

    public EnumFacing getFrontDirection(IBlockState state) {
        return EnumFacing.NORTH;
    }

	public EnumFacing worldToBlockSpace(World world, BlockPos pos, EnumFacing sideHit) {
		IBlockState state = world.getBlockState(pos);
		return BlockHelper.worldToBlockSpace(sideHit, state);
	}

	public abstract Vec3 getRotationData(IBlockState state);
    
}
