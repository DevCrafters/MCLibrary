package kr.rvs.mclibrary.bukkit.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import kr.rvs.mclibrary.bukkit.item.MaterialAndData;
import org.bukkit.Material;

import java.io.IOException;

/**
 * Created by Junhyeong Lim on 2017-08-27.
 */
public class MaterialAndDataAdapter extends TypeAdapter<MaterialAndData> {
    @Override
    @SuppressWarnings({"unchecked", "deprecation"})
    public void write(JsonWriter out, MaterialAndData value) throws IOException {
        out.value(value.getMaterial().getId() + ":" + value.getData());
    }

    @Override
    @SuppressWarnings("deprecation")
    public MaterialAndData read(JsonReader in) throws IOException {
        if (in.hasNext() && in.peek() == JsonToken.STRING) {
            String[] split = in.nextString().split(":", 1);
            return new MaterialAndData(Material.getMaterial(Integer.parseInt(split[0])), Short.parseShort(split[1]));
        }
        return null;
    }
}
