package me.theclashfruit.ddg.util;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import net.minecraft.util.Identifier;

import java.io.IOException;

public class IdentifierTypeAdapter extends TypeAdapter<Identifier> {
    @Override
    public void write(JsonWriter out, Identifier value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(value.toString());
        }
    }

    @Override
    public Identifier read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }

        String value = in.nextString();
        return Identifier.of(value);
    }
}
