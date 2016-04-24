package zenith.essential.common.capability;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import zenith.essential.common.EssentialLogger;
import zenith.essential.common.tile.TileEntityBase;

@SuppressWarnings("rawtypes")
public class InventoryHandlerBase<T extends InventoryHandlerBase> implements IInventoryHandler {

	private int slot;
	private boolean showStack;
	private EnumFacing side;
	private float minX;
	private float maxX;
	private float minY;
	private float maxY;
	private Vec3 renderOffset;
	private int rotationOffset;
	private boolean showText = true;

	private float scale = 1F;
	protected IItemHandlerModifiable inventory;
	
	public InventoryHandlerBase(){
	}

	/**
	 * @param inventory
	 * @param slot
	 * @param hitMin
	 * @param hitMax
	 * @param renderOffset
	 */
	public InventoryHandlerBase(IItemHandlerModifiable inventory, int slot, Vec3 hitMin, Vec3 hitMax, Vec3 renderOffset) {
		this.setInventory(inventory)
			.setSlot(slot)
			.setHit(hitMin, hitMax)
			.setRenderOffset(renderOffset)
			.showStack(true);
	}
	
	@SuppressWarnings("unchecked")
	public T setInventory(IItemHandlerModifiable inventory){
		this.inventory = inventory;
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T showStack(boolean show){
		this.showStack = show;
		return (T) this;
	}
	
	@SuppressWarnings("unchecked")
	public T setSlot(int slot){
		this.slot = slot;
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T setHit(Vec3 hitMin, Vec3 hitMax) {
		this.minX = (float) hitMin.xCoord;
		this.maxX = (float) hitMax.xCoord;
		this.minY = (float) hitMin.yCoord;
		this.minY = (float) hitMax.yCoord;
		return (T) this;
	}
	
	@SuppressWarnings("unchecked")
	public T setRenderOffset(Vec3 renderOffset) {
		this.renderOffset = renderOffset;
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T setRotationOffset(int rotationOffset) {
		this.rotationOffset = rotationOffset;
		return (T) this;
	}
	
	public int getRotationOffset(){
		return rotationOffset;
	}
	
	
	public int getSlot(){
		return slot;
	}

	@Override
	public float getMinX() {
		return minX;
	}

	@Override
	public float getMaxX() {
		return maxX;
	}

	@Override
	public float getMinY() {
		return minY;
	}

	@Override
	public float getMaxY() {
		return maxY;
	}
	
	@SuppressWarnings("unchecked")
	public T setSide(EnumFacing side){
		this.side = side;
		return (T) this;
	}

	@Override
	public EnumFacing getSide() {
		return side;
	}

	@Override
	public float getScale() {
		return scale;
	}

	@Override
	public Vec3 getRenderOffset() {
		return renderOffset;
	}
	

	@Override
	public ItemStack getCurrentStack() {
		if(!showStack){
			return null;
		}
		ItemStack stack = inventory.getStackInSlot(slot);
		return stack;
	}

	@Override
	public boolean acceptAsInput(ItemStack stack) {
		return false;
	}

	@Override
	public int insertInput(TileEntityBase te, ItemStack stack) {
		return 0;
	}

	@Override
	public boolean isOutput() {
		return false;
	}

	@Override
	public boolean isCrafting() {
		return false;
	}
	
	@Override
	public ItemStack extractOutput(TileEntityBase te, EntityPlayer player, int amount) {
		return null;
	}

	public IItemHandlerModifiable getInventory(){
		return inventory;
	}
	
	@SuppressWarnings("unchecked")
	public T setShowText(boolean showText){
		this.showText = showText;
		return (T) this;
	}

	public boolean showText(){
		return this.showText;
	}

}
