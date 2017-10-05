package kr.rvs.mclibrary.bukkit.command.duplex;

import kr.rvs.mclibrary.bukkit.command.CommandInfo;
import kr.rvs.mclibrary.bukkit.command.ICommand;
import kr.rvs.mclibrary.bukkit.player.CommandSenderWrapper;
import kr.rvs.mclibrary.bukkit.player.PlayerUtils;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Junhyeong Lim on 2017-10-05.
 */
public class ModernHelpExecutor extends AbstractHelpExecutor {
    public ModernHelpExecutor(ICommand command, String label, int line) {
        super(command, label, line);
    }

    @Override
    public void sendCommandInfo(CommandSenderWrapper wrapper, String args, CommandInfo commandInfo) {
        CommandSender nativeSender = wrapper.getSender();
        String usage = commandInfo.usage();
        String perm = commandInfo.perm();
        boolean hasPerm = StringUtils.isEmpty(perm) || nativeSender.hasPermission(perm);
        ChatColor color = hasPerm ? ChatColor.GOLD : ChatColor.RED;
        String desc = commandInfo.desc();
        StringBuilder builder = new StringBuilder()
                .append(color);
        StringBuilder cmdSection = new StringBuilder()
                .append('/').append(getLabel());

        if (StringUtils.isNotEmpty(args))
            cmdSection.append(' ').append(args);
        if (StringUtils.isNotEmpty(usage))
            cmdSection.append(' ').append(usage);
        builder.append(cmdSection);
        if (StringUtils.isNotEmpty(desc))
            builder.append(": ").append(ChatColor.WHITE).append(desc);

        String contents = builder.toString();

        if (nativeSender instanceof Player) {
            Player player = (Player) nativeSender;
            String cmd = cmdSection.toString();
            BaseComponent component = new TextComponent(contents);
            component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{
                    new TextComponent("클릭 시 입력창에 자동완성됩니다.")
            }));
            component.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, cmd));
            PlayerUtils.sendBaseComponent(player, component);
        } else {
            nativeSender.sendMessage(contents);
        }
    }
}
