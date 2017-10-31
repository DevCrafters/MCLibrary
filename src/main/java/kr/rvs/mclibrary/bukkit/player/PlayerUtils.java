package kr.rvs.mclibrary.bukkit.player;

import kr.rvs.mclibrary.Static;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * Created by Junhyeong Lim on 2017-07-28.
 */
public class PlayerUtils {
    private static BaseComponentSender COMPONENT_SENDER = (player, component) -> player.spigot().sendMessage(component);

    static {
        try {
            Player.class.getDeclaredMethod("spigot");
        } catch (NoSuchMethodException e) {
            COMPONENT_SENDER = ((player, component) ->
                    Bukkit.dispatchCommand(player, "tellraw " + ComponentSerializer.toString(component)));
        }
    }

    public static Collection<? extends Player> getOnlinePlayers() {
        try {
            return Bukkit.getOnlinePlayers();
        } catch (NoSuchMethodError th) {
            List<Player> onlinePlayers = new ArrayList<>();
            try {
                Method method = Bukkit.class.getMethod("getOnlinePlayers");
                onlinePlayers.addAll(Arrays.asList((Player[]) method.invoke(null)));
            } catch (Exception e) {
                Static.log(e);
            }

            return onlinePlayers;
        }
    }

    public static int hasItems(Player player, ItemStack item) {
        int hasAmount = 0;
        for (ItemStack elemItem : player.getInventory()) {
            if (elemItem == null || elemItem.getType() == Material.AIR
                    || !elemItem.isSimilar(item))
                continue;

            hasAmount += elemItem.getAmount();
        }

        return hasAmount;
    }

    public static boolean isHasItem(Player player, ItemStack item, int amount) {
        int hasAmount = 0;
        for (ItemStack elemItem : player.getInventory()) {
            if (elemItem == null || elemItem.getType() == Material.AIR
                    || !elemItem.isSimilar(item))
                continue;

            hasAmount += elemItem.getAmount();

            if (hasAmount >= amount)
                return true;
        }

        return false;
    }

    public static boolean takeItem(Player player, ItemStack item, int takeAmount) {
        Inventory inv = player.getInventory();
        Map<Integer, ItemStack> removeItemMap = new HashMap<>();

        for (int i = 0; i < inv.getSize(); i++) {
            ItemStack elemItem = inv.getItem(i);
            if (elemItem == null || elemItem.getType() == Material.AIR
                    || !elemItem.isSimilar(item))
                continue;

            int amount = elemItem.getAmount();

            if (takeAmount > amount) {
                takeAmount -= amount;
                removeItemMap.put(i, elemItem);
            } else {
                if (takeAmount < amount) {
                    elemItem.setAmount(amount - takeAmount);
                    inv.setItem(i, elemItem);
                } else {
                    removeItemMap.put(i, elemItem);
                }

                for (Integer slot : removeItemMap.keySet()) {
                    inv.clear(slot);
                }

                return true;
            }
        }

        return false;
    }

    public static Optional<Player> getPlayerOptional(String name) {
        return Optional.ofNullable(Bukkit.getPlayer(name));
    }

    public static Optional<Player> getPlayerOptional(UUID uuid) {
        return Optional.ofNullable(Bukkit.getPlayer(uuid));
    }

    public static int getMaxHealth(Player player) {
        try {
            Method maxHealthMethod = Player.class.getMethod("getMaxHealth");
            return (int) maxHealthMethod.invoke(player);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    public static void setMaxHealth(Player player, int maxHealth) {
        Optional<Method> setMaxHealthMethod = Stream.of(Damageable.class.getDeclaredMethods())
                .filter(method -> method.getName().equals("setMaxHealth"))
                .findFirst();
        setMaxHealthMethod.ifPresent(method -> {
            try {
                method.invoke(player, maxHealth);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new IllegalStateException(e);
            }
        });
    }

    public static void sendBaseComponent(Player player, BaseComponent component) {
        COMPONENT_SENDER.send(player, component);
    }

    public interface BaseComponentSender {
        void send(Player player, BaseComponent component);
    }
}
