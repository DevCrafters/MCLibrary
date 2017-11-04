package kr.rvs.mclibrary.bukkit.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import kr.rvs.mclibrary.MCLibrary;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;

/**
 * Created by Junhyeong Lim on 2017-11-04.
 */
@SuppressWarnings("deprecation")
public class ItemStackTypeAdapter extends TypeAdapter<ItemStack> {
    private final TypeAdapter<ItemMeta> metaAdapter;

    public ItemStackTypeAdapter(TypeAdapter<ItemMeta> metaAdapter) {
        this.metaAdapter = metaAdapter;
    }

    public ItemStackTypeAdapter() {
        this(MCLibrary.getGsonManager().getGson().getAdapter(ItemMeta.class));
    }

    @Override
    public void write(JsonWriter out, ItemStack value) throws IOException {
        out.beginObject();
        out.name("id").value(value.getTypeId());
        if (value.getDurability() > 0)
            out.name("damage").value(value.getDurability());
        if (value.getAmount() > 0)
            out.name("amount").value(value.getAmount());
        if (value.getItemMeta() != null) {
            out.name("meta");
            metaAdapter.write(out, value.getItemMeta());
        }
        out.endObject();
    }

    @Override
    public ItemStack read(JsonReader in) throws IOException {
        int id = 1;
        short data = 0;
        int amount = 1;
        ItemMeta meta = null;

        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "id":
                    id = in.nextInt();
                    break;
                case "damage":
                    data = (short) in.nextInt();
                    break;
                case "amount":
                    amount = in.nextInt();
                    break;
                case"meta":
                    meta = metaAdapter.read(in);
                    break;
            }
        }
        in.endObject();

        ItemStack item = new ItemStack(id, amount, data);
        if (meta != null)
            item.setItemMeta(meta);
        return item;
    }
}
