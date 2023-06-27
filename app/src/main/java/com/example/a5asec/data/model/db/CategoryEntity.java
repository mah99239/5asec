package com.example.a5asec.data.model.db;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.a5asec.data.model.api.Category;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import timber.log.Timber;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(tableName = "categories")
public class CategoryEntity {
    @PrimaryKey
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "name_en")
    public String nameEn;

    @ColumnInfo(name = "name_ar")
    public String nameAr;

    @ColumnInfo(name = "icon_url")
    public String iconUrl;

    @ColumnInfo(name = "sort_index")
    public int sortIndex;

    @Ignore
    public List<ItemsEntity> items;

    public static List<Category> asDomainModel(List<CategoryWithItems> categoryEntities) {
        List<Category> categories = new ArrayList<>();
        try {
            Timber.tag(CategoryEntity.class.getName())
                    .e(categoryEntities.toString());

            for (CategoryWithItems categoryEntity : categoryEntities) {
                Category category = new Category();
                category.setId(categoryEntity.category.getId());
                category.setNameEn(
                        categoryEntity.category.getNameEn()); // Set the name using the English name
                category.setNameAr(
                        categoryEntity.category.getNameAr()); // Set the name using the English name
                category.setIconUrl(categoryEntity.category.getIconUrl());
                category.setSortIndex(categoryEntity.category.getSortIndex());
                List<Category.ItemsEntity> itemsEntities = new ArrayList<>();
                for (ItemWithServices itemEntity : categoryEntity.getItems()) {
                    Category.ItemsEntity itemsEntity = new Category.ItemsEntity();
                    itemsEntity.setId(itemEntity.getItem().getId());
                    itemsEntity.setNameEn(itemEntity.getItem()
                            .getNameEn()); // Set the name using
                    // the English name
                    itemsEntity.setNameAr(itemEntity.getItem()
                            .getNameAr()); // Set the name using
                    // the English name
                    itemsEntity.setCost(itemEntity.getItem().getCost());

                    List<Category.ItemServicesEntity> itemServiceEntities = new ArrayList<>();
                    for (ItemServicesEntity itemServiceEntity : itemEntity.getItemServices()) {
                        Category.ItemServicesEntity itemService = new Category.ItemServicesEntity();
                        itemService.setId(itemServiceEntity.getId());
                        itemService.setCost(itemServiceEntity.getCost());

                        Category.LaundryServiceEntity laundryServiceEntity = new Category.LaundryServiceEntity();
                        laundryServiceEntity.setId(
                                itemServiceEntity.getLaundryService()
                                        .getLaundryServiceId());
                        laundryServiceEntity.setNameEn(
                                itemServiceEntity.getLaundryService()
                                        .getNameEn()); // Set the name using the English name
                        laundryServiceEntity.setNameAr(
                                itemServiceEntity.getLaundryService()
                                        .getNameAr()); // Set the name using the English name
                        laundryServiceEntity.setIconUrl(
                                itemServiceEntity.getLaundryService()
                                        .getIconUrl());

                        itemService.setLaundryService(laundryServiceEntity);
                        itemServiceEntities.add(itemService);
                    }

                    itemsEntity.setItemServices(itemServiceEntities);
                    itemsEntities.add(itemsEntity);
                }

                category.setItems(itemsEntities);
                categories.add(category);
            }
        } catch (Exception e) {
            Timber.tag(CategoryEntity.class.getName()).e(e);
        }
        return categories;
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Entity(tableName = "items")
    public static class ItemsEntity {
        @PrimaryKey
        @ColumnInfo(name = "id")
        public int id;

        @ColumnInfo(name = "name_en")
        public String nameEn;

        @ColumnInfo(name = "name_ar")
        public String nameAr;

        @ColumnInfo(name = "cost")
        public int cost;

        @Ignore
        public String name;


        @ColumnInfo(name = "category_id")
        public int categoryId;

        @Ignore
        public List<ItemServicesEntity> itemServices;


    }

    @Data
    @NoArgsConstructor
    @Entity(tableName = "item_services")
    public static class ItemServicesEntity {

        @PrimaryKey
        @ColumnInfo(name = "id")
        public int id;
        @ColumnInfo(name = "item_id")
        public int itemId;

        @ColumnInfo(name = "cost")
        public int cost;

        @Embedded
        public LaundryServicesEntity laundryService;


    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Entity(tableName = "laundry_services")
    public static class LaundryServicesEntity {

        @PrimaryKey
        @ColumnInfo(name = "laundry_service_id")
        public int laundryServiceId;

        @ColumnInfo(name = "name_en")
        public String nameEn;

        @ColumnInfo(name = "name_ar")
        public String nameAr;


        @ColumnInfo(name = "icon_url")
        public String iconUrl;
    }

}

