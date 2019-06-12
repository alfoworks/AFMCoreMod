package ru.allformine.afmcm.gui;

class Utils {
    private static int clamp(final int min, final int max, final int value) {
        return Math.min(max, Math.max(value, min));
    }

    static int colorARGBtoInt(final int alpha, final int red, final int green, final int blue) {
        return (clamp(0, 255, alpha) << 24)
                + (clamp(0, 255, red  ) << 16)
                + (clamp(0, 255, green) <<  8)
                +  clamp(0, 255, blue );
    }
}
