package com.example.a5asec.data.model.db;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.a5asec.data.model.api.Category;

import java.util.List;

import lombok.Data;

@Data
public class ItemWithServices {
    @Embedded
    public CategoryEntity.ItemsEntity item;

    @Relation(
            parentColumn = "id", entityColumn = "item_id")
    public List< CategoryEntity.ItemServicesEntity> itemServices;

}
