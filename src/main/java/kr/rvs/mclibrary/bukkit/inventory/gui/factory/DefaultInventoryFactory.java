package kr.rvs.mclibrary.bukkit.inventory.gui.factory;

import kr.rvs.mclibrary.bukkit.MCUtils;
import kr.rvs.mclibrary.bukkit.inventory.gui.GUISignature;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

/**
 * Created by Junhyeong Lim on 2017-09-09.
 */
public class DefaultInventoryFactory implements InventoryFactory {
    @Override
    public Inventory create(GUISignature signature, HumanEntity viewer) {
        String title = MCUtils.colorize(signature.getTitle());
        Inventory inv = signature.getType() == InventoryType.CHEST ?
                Bukkit.createInventory(null, signature.getSize(), title) :
                Bukkit.createInventory(null, signature.getType(), title);
        signature.getContents().forEach(inv::setItem);

        return inv;
    }
}
