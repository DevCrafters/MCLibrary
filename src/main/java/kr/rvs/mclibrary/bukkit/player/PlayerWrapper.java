package kr.rvs.mclibrary.bukkit.player;

import kr.rvs.mclibrary.bukkit.inventory.Inventories;
import kr.rvs.mclibrary.reflection.Reflections;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;
import java.util.Optional;

/**
 * Created by Junhyeong Lim on 2017-07-29.
 */
public class PlayerWrapper {
    private final Player player;

    public PlayerWrapper(Player player) {
        this.player = Objects.requireNonNull(player);
    }

    public int hasItems(ItemStack item) {
        return Inventories.hasItems(player, item);
    }

    public boolean hasItem(ItemStack item, int amount) {
        return Inventories.hasItem(player, item, amount);
    }

    public boolean takeItem(ItemStack item, int takeAmount) {
        return Inventories.takeItem(player, item, takeAmount);
    }

    public boolean hasSpace(ItemStack item, int amount) {
        return Inventories.hasSpace(player, item, amount);
    }

    public void sendBaseComponent(BaseComponent component) {
        Players.sendBaseComponent(player, component);
    }

    public int getMaxHealth() {
        return Players.getMaxHealth(player);
    }

    @SuppressWarnings("deprecation")
    public Optional<ItemStack> getItemInHand() {
        ItemStack handItem = player.getItemInHand();
        return handItem != null && handItem.getType() != Material.AIR
                ? Optional.of(handItem)
                : Optional.empty();
    }

    private Object getEntityPlayer() {
        return Reflections.getMethodEx(player.getClass(), "getHandle").invoke(player)
                .orElseThrow(IllegalStateException::new);
    }

    public Integer getContainerCounter() {
        Object entityPlayer = getEntityPlayer();
        return Reflections.getFieldEx(entityPlayer.getClass(), "containerCounter").<Integer>get(entityPlayer)
                .orElseThrow(IllegalStateException::new);
    }
}
