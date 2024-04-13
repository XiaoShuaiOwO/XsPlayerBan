package com.xssssss.xsplayerban.Utils;

public class FlagValue {
    public static String getFlagStrValue(String flag, String startsWith1, String startsWith2, String DefaultValue){
        if (flag.startsWith(startsWith1) || flag.startsWith(startsWith2)) {
            return flag.replace(startsWith1, "").replace(startsWith2, "");
        }
        return DefaultValue;
    }
    public static boolean getFlagBoolValue(String flag, String startsWith1, String startsWith2, boolean DefaultValue){
        if (flag.startsWith(startsWith1) || flag.startsWith(startsWith2)) {
            String flagValue = flag.replace(startsWith1, "").replace(startsWith2, "");
            if (flagValue.equalsIgnoreCase("f") || flagValue.equalsIgnoreCase("false")) {
                return false;
            } else if (flagValue.equalsIgnoreCase("t") || flagValue.equalsIgnoreCase("true")) {
                return true;
            }
        }
        return DefaultValue;
    }
}
