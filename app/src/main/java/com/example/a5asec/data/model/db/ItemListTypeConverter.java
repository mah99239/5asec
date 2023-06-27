package com.example.a5asec.data.model.db;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class ItemListTypeConverter {
    private static final Gson gson = new Gson();

    @TypeConverter
    public static List<CategoryEntity.ItemsEntity> fromString(String value) {
        Type listType = new TypeToken<List<CategoryEntity.ItemsEntity>>() {}.getType();
        return gson.fromJson(value, listType);
    }

    @TypeConverter
    public static String fromList(List<CategoryEntity.ItemsEntity> list) {
        return gson.toJson(list);
    }
}
