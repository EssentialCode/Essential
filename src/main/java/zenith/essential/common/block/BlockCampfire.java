package zenith.essential.common.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;
import zenith.essential.common.EssentialLogger;
import zenith.essential.common.item.EssentialItems;

public class BlockCampfire extends BlockBase {
	public static final String name = "campfire";
    public static final PropertyEnum<CampfireState> STATE = PropertyEnum.<CampfireState>create("state", CampfireState.class);

	public BlockCampfire() {
		super(name, Material.wood);
		setHardness(1.0F);
		setResistance(2.5F);
		setStepSound(soundTypeWood);
		setBlockBounds();
        this.setDefaultState(this.blockState.getBaseState().withProperty(STATE, CampfireState.DONE));
	}

	@Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        worldIn.setBlockState(pos, state.withProperty(STATE, CampfireState.NEW), 2);
    }

	private void setBlockBounds() {
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
	public int getLightValue(IBlockAccess world, BlockPos pos) {
		IBlockState state = world.getBlockState(pos);
		CampfireState prop = state.getValue(STATE);
		if(prop == CampfireState.BURNING){
			return 15;
		}
		return 0;
	};

	@Override
	public boolean onBlockActivated(
			World world, BlockPos pos, IBlockState state, EntityPlayer player, 
			EnumFacing side, float hitX, float hitY, float hitZ) {

		ItemStack stack = player.getHeldItem();
		if(stack == null){
			return false;
		}
		Item item = stack.getItem();
		if(item == EssentialItems.firebow){
			return false;
		} else if (item == Items.flint_and_steel){
			world.setBlockState(pos, state.withProperty(STATE, CampfireState.BURNING), 2);
		}
		return true;
	}

	@Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(STATE, CampfireState.byMetadata(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return ((CampfireState)state.getValue(STATE)).getMetadata();
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] {STATE});
    }

    @Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, BlockPos pos, IBlockState state, Random rand) {
		if(world.getBlockState(pos).getValue(STATE) == CampfireState.BURNING){
			if (rand.nextInt(24) == 0) {
				world.playSound((double)((float)pos.getX() + 0.5F), (double)((float)pos.getY() + 0.5F), (double)((float)pos.getZ() + 0.5F), "fire.fire", 1.0F + rand.nextFloat(), rand.nextFloat() * 0.7F + 0.3F, false);
			}
			
			if(rand.nextInt(3) == 0) {
				for (int j1 = 0; j1 < 2; ++j1) {
					double d7 = (double)pos.getX() + rand.nextDouble();
					double d12 = (double)(pos.getY() + 1) - rand.nextDouble() * 0.10000000149011612D;
					double d17 = (double)pos.getZ() + rand.nextDouble();
					world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d7, d12, d17, 0.0D, 0.0D, 0.0D, new int[0]);
				}
			}
		}
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

	@Override
	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelResourceLocation location = new ModelResourceLocation(getRegistryName()+"Item");
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, location);
	}

	@Override
	public List<IRecipe> getRecipes() {
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
	
	public static enum CampfireState implements IStringSerializable {
        NEW("new"),
		BURNING("burning"),
		DONE("done");

        private final String name;
        private static final CampfireState[] STATE_LOOKUP = new CampfireState[values().length];

        CampfireState(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
        
        public static CampfireState byMetadata(int metaIn) {
        	int meta = metaIn;
        	if(meta < 0 || meta > STATE_LOOKUP.length) {
        		meta = 0;
        	}
        	CampfireState state = STATE_LOOKUP[meta];
        	EssentialLogger.getLogger().info("STATE_LOOKUP: " + state.name + " (" + state.ordinal() + ")");
        	return state;

        }
        
        public int getMetadata() {
        	return ordinal();
        }

		static {
			for (CampfireState state : values()) {
				STATE_LOOKUP[state.getMetadata()] = state;
			}
		}
    }
}