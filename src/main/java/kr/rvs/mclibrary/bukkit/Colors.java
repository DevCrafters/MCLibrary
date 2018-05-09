package kr.rvs.mclibrary.bukkit;

import org.bukkit.ChatColor;
import org.bukkit.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Created by JunHyeong Lim on 2018-05-09
 */
public class Colors {
    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static List<String> colorize(List<String> list) {
        List<String> newList = new ArrayList<>();
        for (String element : list) {
            newList.add(colorize(element));
        }

        return newList;
    }

    public static List<String> asColorizeList(String... args) {
        for (int i = 0; i < args.length; i++) {
            args[i] = colorize(args[i]);
        }
        return Arrays.asList(args);
    }

    public static Optional<ChatColor> getChatColorByColor(Color color) {
        ChatColor ret = null;
        if (color == Color.WHITE) {
            ret = ChatColor.WHITE;
        } else if (color == Color.SILVER) {
            ret = ChatColor.GRAY;
        } else if (color == Color.GRAY) {
            ret = ChatColor.DARK_GRAY;
        } else if (color == Color.BLACK) {
            ret = ChatColor.BLACK;
        } else if (color == Color.RED) {
            ret = ChatColor.RED;
        } else if (color == Color.MAROON) {
            ret = ChatColor.DARK_RED;
        } else if (color == Color.YELLOW || color == Color.OLIVE) {
            ret = ChatColor.YELLOW;
        } else if (color == Color.LIME) {
            ret = ChatColor.GREEN;
        } else if (color == Color.GREEN) {
            ret = ChatColor.DARK_GREEN;
        } else if (color == Color.AQUA) {
            ret = ChatColor.AQUA;
        } else if (color == Color.BLUE) {
            ret = ChatColor.BLUE;
        } else if (color == Color.NAVY) {
            ret = ChatColor.DARK_BLUE;
        } else if (color == Color.FUCHSIA) {
            ret = ChatColor.LIGHT_PURPLE;
        } else if (color == Color.PURPLE) {
            ret = ChatColor.DARK_PURPLE;
        } else if (color == Color.ORANGE) {
            ret = ChatColor.GOLD;
        }
        return Optional.ofNullable(ret);
    }
}
