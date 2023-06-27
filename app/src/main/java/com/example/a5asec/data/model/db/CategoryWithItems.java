package com.example.a5asec.data.model.db;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

import lombok.Data;

@Data
public class CategoryWithItems
{
    @Embedded
    public CategoryEntity category;

    @Relation(entity = CategoryEntity.ItemsEntity.class, parentColumn = "id", entityColumn = "category_id")
    public List<ItemWithServices> items;
}
