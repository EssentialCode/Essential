package zenith.essential.common.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;
import zenith.essential.common.EssentialLogger;

public class BlockCampfire extends BlockBase {
	public static final String name = "campfire";

	public BlockCampfire() {
		super(name, Material.wood);
		setHardness(1.0F);
		setResistance(2.5F);
		setStepSound(soundTypeWood);
		setBlockBounds();

	}

	@Override
	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelResourceLocation location = new ModelResourceLocation(getRegistryName()+"Item");
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, location);
    }
	
	private void setBlockBounds(){
		float pixel = 1F / 16;
		float minX = 0.2F * pixel;
		float minY = 0;
		float minZ = 0.2F * pixel;
		float maxX = 1F - 0.2F * pixel;
		float maxY = 7 * pixel;
		float maxZ = 1F - 0.2F * pixel;
		setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}
	
	@Override
	public boolean isFullCube(){
		return false;
	}

	@Override
	public List<IRecipe> getRecipes(){
		List<IRecipe> recipes = new ArrayList<IRecipe>();
		recipes.add(new ShapedOreRecipe(new ItemStack(EssentialBlocks.campfire),
				"SSS",
				"SLS",
				"SSS",
				'S', "stickWood",
				'L', "logWood"
			));
		return recipes;
		
	}
	
}
