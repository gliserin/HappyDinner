package org.odhs.happydinner.util;

import java.util.Arrays;

public class StringManager {
    public static String reverseString(String s, String regex, String newRegex) {
        StringBuilder sb = new StringBuilder();

        String[] arr = s.split(regex);
        for (int i = 0; i < arr.length; i++) {
            char[] chars = arr[i].toCharArray();
            Arrays.sort(chars);
            arr[i] = new String(chars);
        }

        for (String value : arr) {
            sb.append(value).append(newRegex);
        }

        return sb.toString().substring(0, sb.toString().length()-newRegex.length());
    }
}
