package zenith.essential.common;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import zenith.essential.common.lib.GeneralConstants;

public class EssentialLogger {
	
	private static final Logger logger = LogManager.getLogger(GeneralConstants.MOD_NAME.toUpperCase());
	private static EssentialLogger instance = new EssentialLogger();
	
	public static EssentialLogger getLogger(){
		return instance;
	}
	
	public static void quickDebug(String msg){
		instance.debug(msg);
	}
	
	public void debug(String msg){
		logger.log(Level.DEBUG, msg);
	}

	public static void quickError(String msg){
		instance.error(msg);
	}
	
	public void error(String msg){
		logger.log(Level.ERROR, msg);
	}
	
	public static void quickInfo(String msg){
		instance.info(msg);
	}

	public void info(String msg){
		logger.log(Level.INFO, msg);
	}
	
	
}
