package kr.rvs.mclibrary.util.general;

import kr.rvs.mclibrary.util.collection.NullableArrayList;
import org.apache.commons.lang.Validate;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;
import java.util.function.Consumer;

/**
 * Created by Junhyeong Lim on 2017-08-17.
 */
public class VarargsParser {
    private final Object[] args;
    private final Class[] types;
    private int count = 2;

    public VarargsParser(Object[] args, Class[] types) {
        this.args = args;
        this.types = types;
    }

    public VarargsParser count(int count) {
        Validate.isTrue(types.length == count);
        this.count = count;
        return this;
    }

    public void parse(Consumer<Section> sectionCallback) {
        Queue queue = new ArrayDeque<>(Arrays.asList(args));
        while (queue.size() >= count) {
            NullableArrayList<Object> values = new NullableArrayList<>(count);
            for (int i = 0; i < count; i++) {
                values.add(queue.poll());
            }
            sectionCallback.accept(new Section(values));
        }
    }

    public class Section {
        private final NullableArrayList values;

        public Section(NullableArrayList values) {
            this.values = values;
        }

        @SuppressWarnings("unchecked")
        public <T> T get(Integer index) {
            Object value = values.get(index);
            return value != null ? (T) value : null;
        }
    }
}
