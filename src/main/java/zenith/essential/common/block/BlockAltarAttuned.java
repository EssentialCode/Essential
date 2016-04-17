package zenith.essential.common.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zenith.essential.api.essence.EnumEssenceType;
import zenith.essential.client.render.RenderAltarAttuned;
import zenith.essential.common.tile.TileEntityAltar;

public class BlockAltarAttuned extends BlockBase implements ITileEntityProvider{

	public static final String name = "altarAttuned";
	public static final PropertyEnum<EnumEssenceType> ESSENCE_TYPE = PropertyEnum.<EnumEssenceType>create("essenceType", EnumEssenceType.class);
	
	public BlockAltarAttuned(){
		super(name, Material.wood);
		this.setDefaultState(this.blockState.getBaseState()
        		.withProperty(ESSENCE_TYPE, EnumEssenceType.WOOD));
	}

	@Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityAltar();
    }

	@Override
	@SideOnly(Side.CLIENT)
	public void initModel() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAltar.class, new RenderAltarAttuned());
		for( EnumEssenceType essType : EnumEssenceType.values()) {
			ModelLoader.setCustomModelResourceLocation( 
					Item.getItemFromBlock(this),
					essType.getMetadata(), 
					new ModelResourceLocation(getRegistryName(), "essenceType=" + essType.getName())
				);
		}
	}

    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list)
    {
		list.addAll(getAllSubBlocks());
    }
    
    @SideOnly(Side.CLIENT)
    private List<ItemStack> getAllSubBlocks(){
    	ArrayList<ItemStack> stacks = new ArrayList<ItemStack>();
    	for(EnumEssenceType enumType : EnumEssenceType.values()){
    		stacks.add(new ItemStack(Item.getItemFromBlock(this), 1, enumType.getMetadata()));
    	}
    	return stacks;
    }

    protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] {ESSENCE_TYPE});
    }

    public int getMetaFromState(IBlockState state)
    {
    	int meta = state.getValue(ESSENCE_TYPE).getMetadata();
        return meta;
    }

    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState()
        		.withProperty(ESSENCE_TYPE, EnumEssenceType.byMetadata(meta));
    }

	@Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
        return false;
    }

	@Override
	public Vec3 getRotationData(IBlockState state) {
		// TODO Auto-generated method stub
		return null;
	}

}
