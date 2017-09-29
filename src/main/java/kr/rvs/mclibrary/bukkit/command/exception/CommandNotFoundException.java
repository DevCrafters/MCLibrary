package kr.rvs.mclibrary.bukkit.command.exception;

import kr.rvs.mclibrary.bukkit.command.CommandArguments;
import kr.rvs.mclibrary.bukkit.command.Executable;
import kr.rvs.mclibrary.bukkit.player.CommandSenderWrapper;

/**
 * Created by Junhyeong Lim on 2017-09-27.
 */
public class CommandNotFoundException extends CommandException {
    public CommandNotFoundException(CommandSenderWrapper wrapper, CommandArguments arguments, Executable source) {
        super(wrapper, arguments, source);
    }
}
