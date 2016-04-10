package zenith.essential.common.item;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zenith.essential.common.EssentialLogger;
import zenith.essential.common.lib.ColorHelper;

public class ItemFireBow extends ItemBase {

	private static EssentialLogger log = EssentialLogger.getLogger();

	public static String name = "itemFireBow";
	public static final int MAX_HEAT = 50;
	public static final short MAX_COOLING = 15;
	public static final String HEAT_KEY = "heat";
	public static final String COOLING_KEY = "cooling";

	public ItemFireBow(){
		super(name);
		this.maxStackSize = 1;
        this.setMaxDamage(64);
	}
	
	@SideOnly(Side.CLIENT)
    public void initModel() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName()));

//        ModelLoader.setCustomMeshDefinition(this, new ItemMeshDefinition() {
//            @Override
//            public ModelResourceLocation getModelLocation(ItemStack stack) {
//                NBTTagCompound cmp = stack.getTagCompound();
//                short heat = 0;
//                if (cmp != null) {
//                    heat = cmp.getShort(HEAT_KEY);
//                }
//                int level = (9*heat) / MAX_HEAT;
//                if (level < 0) {
//                    level = 0;
//                } else if (level > 8) {
//                    level = 8;
//                }
//                return new ModelResourceLocation(getRegistryName() + (8-level));
//            }
//        });
    }

	@SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int renderPass)
    {
		if(renderPass == 0){
			return ColorHelper.getColor(255, 192, 73);
		} else if (renderPass == 1){
			return 0xeeeeee;
		}

		NBTTagCompound cmp = stack.getTagCompound();
		short heat = 0;
		if (cmp != null) {
			heat = cmp.getShort(HEAT_KEY);
		}
		int level = (9*heat) / MAX_HEAT;
		if (level < 0) {
			level = 0;
		} else if (level > 8) {
			level = 8;
		}
		int color = 0;
		int baseGreen = 192;
		int green = baseGreen - (level * 20);
		color = ColorHelper.getColor(255, green, 73);
		return  color;
    }

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos,
			EnumFacing side, float hitX, float hitY, float hitZ) {

        if (!playerIn.canPlayerEdit(pos, side, stack))
        {
            return false;
        }
        else
        {
			NBTTagCompound cmp = stack.getTagCompound();
			short heat;
			if(cmp == null || !cmp.hasKey(HEAT_KEY)){
				heat = (short) 0;
				cmp = new NBTTagCompound();
				cmp.setShort(HEAT_KEY, heat);
				stack.setTagCompound(cmp);
			} else {
				heat = cmp.getShort(HEAT_KEY);
			}
			
			heat = (short) (heat +  1);
			cmp.setShort(COOLING_KEY, MAX_COOLING);
			
			if (heat >= MAX_HEAT){
				startFire(stack, cmp, playerIn, worldIn, pos, side);
			} else {
				stack.getTagCompound().setShort(HEAT_KEY, heat);
			}

            return true;
        }
    }

	private void startFire(ItemStack stack, NBTTagCompound cmp, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side) {
		pos = pos.offset(side);
		if (worldIn.isAirBlock(pos))
		{
			worldIn.playSoundEffect((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, "fire.ignite", 1.0F, itemRand.nextFloat() * 0.4F + 0.8F);
			worldIn.setBlockState(pos, Blocks.fire.getDefaultState());
		}

		stack.damageItem(1, playerIn);
		cmp.setShort(HEAT_KEY, (short) 0);
	}
	
	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entity, int par4, boolean par5){
		NBTTagCompound cmp = stack.getTagCompound();
		short heat, cooling;
		if(cmp == null){
			cmp = new NBTTagCompound();
			stack.setTagCompound(cmp);
		}
		
		if(!cmp.hasKey(HEAT_KEY)){
			heat = 0;
			cmp.setShort(HEAT_KEY, heat);
		}
		
		if(!cmp.hasKey(COOLING_KEY)){
			cooling = 0;
			cmp.setShort(COOLING_KEY, cooling);
		}
		cooling = cmp.getShort(COOLING_KEY);
		heat = cmp.getShort(HEAT_KEY);
		if (heat > 0){
			if(cooling > 0){
				cooling--;
				cmp.setShort(COOLING_KEY, cooling);
				return;
			}
			heat--;
			cooling = 2;
			cmp.setShort(HEAT_KEY, heat);
			cmp.setShort(COOLING_KEY, cooling);
		}
	}
	
	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged){
		return !areItemStacksEqual(oldStack, newStack);
	}
/**
     * compares ItemStack argument1 with ItemStack argument2; returns true if both ItemStacks are equal
     */
    public static boolean areItemStacksEqual(ItemStack stackA, ItemStack stackB)
    {
    	if(stackA == null && stackB == null){
    		return true;
    	}
    	if(stackA != null && stackB != null){
    		if( stackA.stackSize != stackB.stackSize || 
				stackA.getItem() != stackB.getItem() || 
				stackA.getItemDamage() != stackB.getItemDamage()){
    			return false;
    		}
    		return true;
    	}
    	return false;
    }
}