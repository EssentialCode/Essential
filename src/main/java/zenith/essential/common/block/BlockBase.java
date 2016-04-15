package zenith.essential.common.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zenith.essential.common.EssentialCreativeTab;
import zenith.essential.common.item.EssentialItemBlock;

public class BlockBase extends Block {

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
	public boolean isFullBlock(){
		return false;
	}

	@Override
	public boolean isOpaqueCube() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public String getUnlocalizedIdentifier(int meta){
		return null;
	}
}
