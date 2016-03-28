package zenith.essential.common.proxy;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import zenith.essential.common.event.ForgeEventHandlers;

public class CommonProxy {
	
	public void init(FMLInitializationEvent event){
		
		MinecraftForge.EVENT_BUS.register(new ForgeEventHandlers());
	}
}
