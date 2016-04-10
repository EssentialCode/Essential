package zenith.essential.common.lib;

public class ColorHelper {

	public static int getColor(int r, int g, int b){
		int intVal = r;
		intVal = (intVal << 8) + g; 
		intVal = (intVal << 8) + b;
		return intVal;
	}
	
	public static int getRed(int in){
		return in >> 16 & 0xFF;
	}

	public static int getGreen(int in){
		return in >> 8 & 0xFF;
	}

	public static int getBlue(int in){
		return in & 0xFF;
	}
}
