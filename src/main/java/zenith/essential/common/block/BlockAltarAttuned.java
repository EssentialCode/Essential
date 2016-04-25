package zenith.essential.common.block;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
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
import zenith.essential.common.Essential;
import zenith.essential.common.EssentialLogger;
import zenith.essential.common.lib.ColorHelper;
import zenith.essential.common.proxy.CommonProxy;
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
		IBlockState theState = getStateFromMeta(meta);
        return new TileEntityAltar(theState.getValue(ESSENCE_TYPE));
    }

	@Override
	public int getLightValue(IBlockAccess world, BlockPos pos) {
		TileEntity te = world.getTileEntity(pos);
		if(te instanceof TileEntityAltar){
			TileEntityAltar altar = (TileEntityAltar) te;
			return Math.floorDiv(altar.getMeterOutput(), 2) + 10;
		}
		return 0;
	};
	
	
    @Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, BlockPos pos, IBlockState state, Random rand) {
    	TileEntity te = world.getTileEntity(pos);
    	if(te instanceof TileEntityAltar){
    		TileEntityAltar altar = (TileEntityAltar) te;
//			if (rand.nextInt(24) == 0) {
//				world.playSound((double)((float)pos.getX() + 0.5F), (double)((float)pos.getY() + 0.5F), (double)((float)pos.getZ() + 0.5F), "fire.fire", 1.0F + rand.nextFloat(), rand.nextFloat() * 0.7F + 0.3F, false);
//			}

			if(altar.isWorking() && rand.nextInt(5) == 0) {
				MapColor color = state.getValue(ESSENCE_TYPE).getColor();
				float r = (float) ColorHelper.getRed(color.colorValue) / 255f;
				float g = (float) ColorHelper.getGreen(color.colorValue) /255f;
				float b = (float) ColorHelper.getBlue(color.colorValue) / 255f;

				{
					double xPos = (double)pos.getX() + rand.nextDouble() / 2 + 0.25f;
					double yPos = (double)(pos.getY() + 1) - rand.nextDouble() * 0.10000000149011612D;
					double zPos = (double)pos.getZ() + rand.nextDouble() / 2 + 0.25f;
					float grav = -0.05F - (float) Math.random() * 0.03F;

					
					CommonProxy proxy = Essential.proxy;
					proxy.essenceFX(world, xPos, yPos, zPos, r, g, b, grav, 0.25F, 15);
					
				}


//					EssentialLogger.quickInfo("Making a particle!");
			}
		}
	}

	/*
	  for(int i = 0; i < particles; i++) {
		double x = player.posX + (Math.random() - 0.5) * 2.1 * player.width;
		double y = player.posY - player.getYOffset();
		double z = player.posZ + (Math.random() - 0.5) * 2.1 * player.width;
		float grav = -0.15F - (float) Math.random() * 0.03F;
		Psi.proxy.sparkleFX(world, x, y, z, r, g, b, grav, 0.25F, 15);
	  }
	 * 
	 */

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
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
    		EnumFacing side, float hitX, float hitY, float hitZ) {
    	// TODO Auto-generated method stub
    	return ((TileEntityAltar) worldIn.getTileEntity(pos)).activatedByPlayer(playerIn);
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
