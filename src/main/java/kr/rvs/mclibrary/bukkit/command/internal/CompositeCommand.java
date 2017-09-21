package kr.rvs.mclibrary.bukkit.command.internal;

import kr.rvs.mclibrary.Static;
import kr.rvs.mclibrary.bukkit.command.BaseCommand;
import kr.rvs.mclibrary.bukkit.command.exception.CommandNotFoundException;
import kr.rvs.mclibrary.bukkit.player.CommandSenderWrapper;
import kr.rvs.mclibrary.collection.OptionalHashMap;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * Created by Junhyeong Lim on 2017-09-19.
 */
public class CompositeCommand extends OptionalHashMap<String, ICommand> implements ICommand {
    @Override
    public void execute(CommandSender sender, BaseCommand cmd, String label, CommandArguments args) {
        ICommand command = get(args.pollFirst());
        if (command != null) {
            command.execute(sender, cmd, label, args);
        } else {
            throw new CommandNotFoundException(cmd, this, new CommandSenderWrapper(sender));
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String label, CommandArguments args) {
        // Sub
        String firstArg = args.peekFirst();
        ICommand command = get(firstArg);
        if (command != null) {
            args.remove(0);
            return command.tabComplete(sender, label, args);
        }

        // Else
        return keySet().stream()
                .filter(key -> key.startsWith(firstArg))
                .collect(Collectors.toList());
    }

    @Override
    public CompositeCommand computeIfAbsent(String key, Function<? super String, ? extends ICommand> mappingFunction) {
        ICommand command = super.computeIfAbsent(key, mappingFunction);
        if (!(command instanceof CompositeCommand)) {
            Static.log(Level.WARNING, String.format(
                    "CompositeCommand expected, but find %s, key: %s",
                    command.toString(), key)
            );
            put(key, command = new CompositeCommand());
        }

        return (CompositeCommand) command;
    }
}
