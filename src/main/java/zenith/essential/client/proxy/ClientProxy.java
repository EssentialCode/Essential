package zenith.essential.client.proxy;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import zenith.essential.client.handler.TickHandler;
import zenith.essential.common.block.EssentialBlocks;
import zenith.essential.common.item.EssentialItems;
import zenith.essential.common.proxy.CommonProxy;

public class ClientProxy extends CommonProxy {
	public static final TickHandler TICK_HANDLER = new TickHandler();

	@Override
	public void preInit(FMLPreInitializationEvent event){
		super.preInit(event);
		EssentialItems.initModels();
		EssentialBlocks.initModels();
	}
	
	@Override
	public void init(FMLInitializationEvent event){
		super.init(event);
		MinecraftForge.EVENT_BUS.register(TICK_HANDLER);
	}

}
