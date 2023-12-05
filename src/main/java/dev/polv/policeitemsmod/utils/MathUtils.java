package dev.polv.policeitemsmod.utils;

/**
 * @author Pol Vallverdu (polv.dev)
 */
public class MathUtils {

    public static int randomInt(int min, int max) {
        return (int) (Math.random() * (max - min + 1) + min);
    }

    public static String format(double value) {
        return String.format("%.2f", value);
    }

}
