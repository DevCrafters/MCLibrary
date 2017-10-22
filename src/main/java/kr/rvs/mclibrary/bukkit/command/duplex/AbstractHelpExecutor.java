package kr.rvs.mclibrary.bukkit.command.duplex;

import kr.rvs.mclibrary.bukkit.command.CommandArguments;
import kr.rvs.mclibrary.bukkit.command.CommandInfo;
import kr.rvs.mclibrary.bukkit.command.ICommand;
import kr.rvs.mclibrary.bukkit.player.CommandSenderWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Junhyeong Lim on 2017-10-05.
 */
public abstract class AbstractHelpExecutor implements ICommand {
    private final ICommand command;
    private final String label;
    private final int line;
    private final List<CommandStorage> storageList = new ArrayList<>();
    private final String helpArg;
    private int lastHashCode = -1;

    public AbstractHelpExecutor(ICommand command, String label, int line) {
        this.command = command;
        this.label = label;
        this.line = line;
        this.helpArg = label.matches("[a-zA-Z]+") ? "help" : "도움말";

        if (command instanceof MapCommand) {
            MapCommand compositeExecutor = (MapCommand) command;
            if (!compositeExecutor.containsKey(helpArg))
                compositeExecutor.put(helpArg, this);
        }
    }

    private void checkDiff() {
        int hashCode = command.hashCode();
        if (lastHashCode != hashCode) {
            storageList.clear();
            init("", command);
            lastHashCode = hashCode;
        }
    }

    private String argSpacing(String args, String key) {
        return args.isEmpty() ? key : key.isEmpty() ? args : args + ' ' + key;
    }

    private void init(String args, ICommand command) {
        if (command instanceof MapCommand) {
            MapCommand compositeExecutor = (MapCommand) command;

            for (Map.Entry<String, ICommand> entry : compositeExecutor.entrySet()) {
                String key = entry.getKey();
                ICommand val = entry.getValue();

                if (val instanceof MapCommand) {
                    MapCommand mapCommand = (MapCommand) val;
                    ICommand base = mapCommand.getBaseCommand();
                    init(argSpacing(args, key), val);
                    if (base != null)
                        init(argSpacing(args, ""), base);
                } else if (val instanceof CommandInfo) {
                    CommandStorage storage = new CommandStorage(argSpacing(args, key), (CommandInfo) val);
                    storageList.add(storage);
                }
            }
        } else if (command instanceof CommandInfo) {
            CommandStorage storage = new CommandStorage("", (CommandInfo) command);
            storageList.add(storage);
        }
    }

    public int getMaxPage() {
        return storageList.size() / line + (storageList.size() % line > 0 ? 1 : 0);
    }

    @Override
    public void execute(CommandSenderWrapper wrapper, CommandArguments args) {
        checkDiff();

        int maxPage = getMaxPage();
        int currPage = Math.min(Math.max(args.getInt(0, 1), 1), maxPage);
        int start = (currPage - 1) * line;
        int end = Math.min(currPage * line, storageList.size());
        boolean paging = maxPage > 1;

        // Header
        StringBuilder header = new StringBuilder().append("&e--------- &f도움말: /").append(label).append(' ');
        if (paging) {
            header.append(String.format("(%d/%d) ", currPage, maxPage));
        }
        header.append("&e");
        for (int i = header.length(); i < 55; i++) {
            header.append('-');
        }
        wrapper.sendMessage(header);

        // Additional
        if (paging) {
            wrapper.sendMessage(String.format(
                    "&7'/%s %s [페이지]' 를 입력할 수 있습니다.",
                    label, helpArg
            ));
        } else {
            wrapper.sendMessage("&7하위 명령어 목록");
        }

        // Contents
        for (int i = start; i < end; i++) {
            CommandStorage storage = storageList.get(i);
            sendCommandInfo(wrapper, storage.args, storage.info);
        }
    }

    @Override
    public List<String> tabComplete(CommandSenderWrapper wrapper, CommandArguments args) {
        checkDiff();

        List<String> ret = new ArrayList<>();
        int maxPage = getMaxPage();
        for (int i = 1; i <= maxPage; i++) {
            ret.add(String.valueOf(i));
        }
        return ret;
    }

    public abstract void sendCommandInfo(CommandSenderWrapper wrapper, String args, CommandInfo commandInfo);

    public String getLabel() {
        return label;
    }

    static class CommandStorage {
        private final String args;
        private final CommandInfo info;

        public CommandStorage(String args, CommandInfo info) {
            this.args = args;
            this.info = info;
        }
    }
}
