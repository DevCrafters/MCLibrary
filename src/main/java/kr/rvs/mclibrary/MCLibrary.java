package kr.rvs.mclibrary;

import kr.rvs.mclibrary.struct.command.CommandArgs;
import kr.rvs.mclibrary.struct.command.CommandType;
import kr.rvs.mclibrary.struct.command.MCCommand;
import kr.rvs.mclibrary.util.bukkit.MCUtils;
import kr.rvs.mclibrary.util.bukkit.command.CommandManager;
import kr.rvs.mclibrary.util.bukkit.command.CommandSenderWrapper;
import kr.rvs.mclibrary.util.bukkit.inventory.GUI;
import kr.rvs.mclibrary.util.bukkit.protocol.PacketMonitoringListener;
import kr.rvs.mclibrary.util.gson.GsonManager;
import kr.rvs.mclibrary.util.gson.SettingManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

/**
 * Created by Junhyeong Lim on 2017-07-26.
 */
public class MCLibrary extends JavaPlugin {
    private static final CommandManager commandManager = new CommandManager();
    private static final GsonManager gsonManager = new GsonManager();
    private static final SettingManager settingManager = new SettingManager();
    private static MCLibrary plugin;

    public MCLibrary() {
        plugin = this;
    }

    public static CommandManager getCommandManager() {
        return commandManager;
    }

    public static GsonManager getGsonManager() {
        return gsonManager;
    }

    public static SettingManager getSettingManager() {
        return settingManager;
    }

    public static MCLibrary getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        GUI.init();
        saveDefaultConfig();
        getCommandManager().registerCommand(new LibraryCommand(), this);

        configInit();
    }

    private void configInit() {
        if (MCUtils.isEnabled("ProtocolLib")
                && getConfig().getBoolean("packet-debug", false)) {
            MCUtils.getProtocolManager().removePacketListeners(this);
            MCUtils.getProtocolManager().addPacketListener(new PacketMonitoringListener());
        }
    }

    @Override
    public void onDisable() {
        settingManager.save();
    }

    class LibraryCommand implements MCCommand {
        @Override
        public String label() {
            return "mclibrary";
        }

        @CommandArgs(
                args = "reload",
                desc = "Reload config file"
        )
        public void onReload(CommandSender sender, List<String> args) {
            reloadConfig();
            configInit();
            for (String key : getConfig().getKeys(true)) {
                sender.sendMessage(key + ": " + getConfig().get(key));
            }
        }

        @CommandArgs(
                type = CommandType.PLAYER_ONLY,
                perm = "mclibrary.killall",
                args = "killall",
                desc = "Kill all entities"
        )
        public void onKillall(CommandSenderWrapper sender, List<String> args) {
            sender.getPlayer().getWorld().getEntities()
                    .stream()
                    .filter(entity -> entity instanceof Creature)
                    .forEach(Entity::remove);
        }
    }
}
