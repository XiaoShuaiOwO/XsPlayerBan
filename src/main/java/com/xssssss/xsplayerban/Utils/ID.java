package com.xssssss.xsplayerban.Utils;

import com.xssssss.xsplayerban.Main;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import static com.xssssss.xsplayerban.Main.config;

public class ID {
    public static String createID (String IDtype) {
        List<String> IDList = Main.data.getStringList("UsedID");
        String Start = config.getString(IDtype+"IDFormat.Start");
        String Characters = config.getString(IDtype+"IDFormat.Characters");
        int Length = config.getInt(IDtype+"IDFormat.Length");
        String ID;
        Random random = new Random();
        do {
            StringBuilder sb = new StringBuilder(Start);
            for (int i = 0; i < Length; i++) {
                sb.append(Characters.charAt(random.nextInt(Characters.length())));
            }
            ID = sb.toString();
        } while (IDList.contains(ID)); // 确保生成的BanID不是已存在的
        IDList.add(ID);
        Main.data.set("UsedID",IDList);
        return ID;
    }
}
