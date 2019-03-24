package ru.allformine.afmcm.proxy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Util {
    static byte[][] splitArray(byte[] arrayToSplit, int chunkSize) {
        if (chunkSize <= 0) {
            return null;
        }

        int rest = arrayToSplit.length % chunkSize;
        int chunks = arrayToSplit.length / chunkSize + (rest > 0 ? 1 : 0);

        byte[][] arrays = new byte[chunks][];

        for (int i = 0; i < (rest > 0 ? chunks - 1 : chunks); i++) {
            arrays[i] = Arrays.copyOfRange(arrayToSplit, i * chunkSize, i * chunkSize + chunkSize);
        }
        if (rest > 0) {
            arrays[chunks - 1] = Arrays.copyOfRange(arrayToSplit, (chunks - 1) * chunkSize, (chunks - 1) * chunkSize + rest);
        }
        return arrays;
    }

    public static List<String> splitString(String msg, int lineSize) {
        List<String> res = new ArrayList<>();

        Pattern p = Pattern.compile("\\b.+" + (lineSize-1) + "}\\b\\W?");
        Matcher m = p.matcher(msg);

        while(m.find()) {
            System.out.println(m.group().trim());   // Debug
            res.add(m.group());
        }
        return res;
    }
}
