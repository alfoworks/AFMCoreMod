package ru.allformine.afmcm.messaging;

import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;

public class MessagingUtils {
    public static String[] splitByMaxChars(String string, int max) {
        ArrayList<String> out = new ArrayList<>();
        String[] wrapped = WordUtils.wrap(string, max).split(System.lineSeparator());

        for (String part : wrapped) {
            out.addAll(splitIgnoringWords(part, max));
        }

        return out.toArray(new String[]{});
    }

    private static ArrayList<String> splitIgnoringWords(String string, int max) {
        ArrayList<String> parts = new ArrayList<>();

        int length = string.length();
        for (int i = 0; i < length; i += max) {
            parts.add(string.substring(i, Math.min(length, i + max)));
        }

        return parts;
    }
}
