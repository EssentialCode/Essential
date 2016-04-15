package zenith.essential.common.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zenith.essential.common.EssentialLogger;

public class BlockTable extends BlockBase {
	public static final String name = "table";

    public static final PropertyEnum<BlockPlanks.EnumType> WOOD_TYPE = PropertyEnum.<BlockPlanks.EnumType>create("woodType", BlockPlanks.EnumType.class);
    public static final PropertyBool ROTATE = PropertyBool.create("rotate");

	public BlockTable() {
		this(name);
	}
	
	public BlockTable(String nameIn){
		super(nameIn, Material.wood);
		setHardness(1.0F);
		setResistance(2.5F);
		setStepSound(soundTypeWood);
        this.setDefaultState(this.blockState.getBaseState()
        		.withProperty(WOOD_TYPE, BlockPlanks.EnumType.OAK)
        		.withProperty(ROTATE, false)
        		);
		
	}
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack){
		Vec3 lookVector = placer.getLookVec();
		BlockPlanks.EnumType woodType = getStateFromMeta(stack.getMetadata()).getValue(WOOD_TYPE);
		boolean rotate = Math.abs(lookVector.xCoord) < 0.5F;
		world.setBlockState(pos, this.blockState.getBaseState()
			.withProperty(WOOD_TYPE, woodType)
			.withProperty(ROTATE, rotate)
			, 2);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void initModel() {
		for( BlockPlanks.EnumType enumType : BlockPlanks.EnumType.values()) {
			ModelLoader.setCustomModelResourceLocation( 
					Item.getItemFromBlock(this),
					enumType.getMetadata(), 
					new ModelResourceLocation(getRegistryName(), "rotate=false,woodtype=" + enumType.getName())
				);
		}
	}
	
	

    public int damageDropped(IBlockState state)
    {
        return ((BlockPlanks.EnumType)state.getValue(WOOD_TYPE)).getMetadata();
    }

    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list)
    {
		list.addAll(getAllSubBlocks());
    }
    
    @SideOnly(Side.CLIENT)
    private List<ItemStack> getAllSubBlocks(){
    	ArrayList<ItemStack> stacks = new ArrayList<ItemStack>();
    	for(BlockPlanks.EnumType enumType : BlockPlanks.EnumType.values()){
    		stacks.add(new ItemStack(Item.getItemFromBlock(this), 1, enumType.getMetadata()));
    	}
    	return stacks;
    }

    public int getMetaFromState(IBlockState state)
    {
    	int rotateBit = state.getValue(ROTATE) ? 1 : 0;
    	int woodTypeMeta = ((BlockPlanks.EnumType) state.getValue(WOOD_TYPE)).getMetadata();
    	int meta = (rotateBit << 3) + woodTypeMeta;
        return meta;
    }

    public IBlockState getStateFromMeta(int meta)
    {
    	boolean rotate = (meta & 0b1000) >> 3 == 1;
    	int woodTypeMeta = meta & 0b0111;
        return this.getDefaultState()
        		.withProperty(WOOD_TYPE, BlockPlanks.EnumType.byMetadata(woodTypeMeta))
        		.withProperty(ROTATE, rotate);
    }

    public MapColor getMapColor(IBlockState state)
    {
        return ((BlockPlanks.EnumType)state.getValue(WOOD_TYPE)).func_181070_c();
    }

    protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] {WOOD_TYPE, ROTATE});
    }

	public String getUnlocalizedIdentifier(int meta){
		BlockPlanks.EnumType enumType = BlockPlanks.EnumType.byMetadata(meta);
		if(enumType != null){
			return enumType.getUnlocalizedName();
		}
		return null;
	}
}
