package com.kkb.dts.utils;

import java.util.ArrayList;
import java.util.List;

public class DbKeyUtil {

    public static void main(String[] args) {
        //testing for data base column-key to java field
        String[] dbKeys = {"id", "user_age", "user_addr_"};
        DbKeyUtil t = new DbKeyUtil();
        convertToJavas(dbKeys);
        System.out.println("-----------------------------------");
        //testing for Java field to data base column-key
        String javaFieldNames[] = {"id", "userAge", "userHomeAddr"};
        t.getDBKey(javaFieldNames);
    }


    /*
     * Java field to data base column-key
     */

    private void getDBKey(String... javaFieldNames) {
        if (javaFieldNames != null && javaFieldNames.length > 0) {
            for (String name : javaFieldNames) {
                StringBuilder buffer = new StringBuilder();

                char[] array = name.toCharArray();
                List<Integer> insertIndexes = new ArrayList<>();
                for (int i = 0; i < array.length; i++) {
                    char c = array[i];
                    if (i != 0 && Character.isUpperCase(c)) {
                        insertIndexes.add(i);
                    }
                }
                if (insertIndexes.size() > 0) {
                    int flag = 0;
                    for (Integer insertIndex : insertIndexes) {
                        String word = toLowercase4FirstLetter(name.substring(flag, insertIndex));
                        buffer.append(word).append("_");
                        flag = insertIndex;
                    }
                    String last = toLowercase4FirstLetter(name.substring(flag));
                    buffer.append(last);
                    System.out.println(buffer.toString());
                } else {
                    System.out.println(name);
                }
            }
        }
    }

    private String toLowercase4FirstLetter(String word) {
        if (word != null && word.length() > 0) {
            String firstLetter = word.substring(0, 1);
            String others = word.substring(1);
            return firstLetter.toLowerCase() + others;
        } else {
            return "";
        }
    }

    /*
     * data base column-key to java field
     */
    public static void convertToJavas(String... dbKeys) {
        if (dbKeys != null && dbKeys.length > 0) {
            for (String key : dbKeys) {
                convertToJava(key);
            }
        }
    }

    public static String convertToJava(String dbKey) {
        String[] words = dbKey.split("_");
        return toUppercase4FirstLetter(words);
    }

    private static String toUppercase4FirstLetter(String... words) {
        StringBuilder buffer = new StringBuilder();
        if (words != null && words.length > 0) {
            for (int i = 0; i < words.length; i++) {
                String word = words[i];
                String firstLetter = word.substring(0, 1);
                String others = word.substring(1);
                String upperLetter = null;
                if (i != 0) {
                    upperLetter = firstLetter.toUpperCase();
                } else {
                    upperLetter = firstLetter;
                }
                buffer.append(upperLetter).append(others);
            }
            return buffer.toString();
        }
        return "";
    }

}