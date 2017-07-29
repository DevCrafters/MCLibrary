package kr.rvs.mclibrary.struct.command;

/**
 * Created by Junhyeong Lim on 2017-07-27.
 */
public interface CommandLayout {
    String LABEL_KEY = "{cmd}";
    String ARGS_KEY = "{args}";
    String USAGE_KEY = "{usage}";
    String DESCRIPTION_KEY = "{desc}";

    default String prefix() {
        return "&e&l---------------------------";
    }

    default String suffix() {
        return "&e&l---------------------------";
    }

    default String content() {
        /*
        Example: /test arg (usage): This is a test command.
         */
        return String.format("&6&l/%s %s %s: &f&l%s",
                LABEL_KEY, ARGS_KEY, USAGE_KEY, DESCRIPTION_KEY);
    }
}
