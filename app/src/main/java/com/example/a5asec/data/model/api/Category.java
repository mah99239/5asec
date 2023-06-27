package com.example.a5asec.data.model.api;

import com.example.a5asec.data.model.db.CategoryEntity;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Category
{


    @Expose
    @SerializedName("id")
    private int id;


    @Expose
    @SerializedName("nameEn")
    private String nameEn;
    @Expose
    @SerializedName("nameAr")
    private String nameAr;

    //This means @Data will not produce a getter for this field so have to explicitly define it
    @Getter(AccessLevel.NONE)
    private String name;

    @Expose
    @SerializedName("iconUrl")
    private String iconUrl;
    @Expose
    @SerializedName("sortIndex")
    private int sortIndex;
    @Expose
    @SerializedName("items")
    private List<ItemsEntity> items;

    public static List<CategoryEntity> asDatabaseModel(List<Category> categories) {
        List<CategoryEntity> categoryEntities = new ArrayList<>();
        for (Category category : categories) {
            CategoryEntity categoryEntity = new CategoryEntity();
            categoryEntity.setId(category.getId());
            categoryEntity.setNameEn(
                    category.getNameEn()); // Set the name using the English name
            categoryEntity.setNameAr(
                    category.getNameAr()); // Set the name using the English name
            categoryEntity.setIconUrl(category.getIconUrl());
            categoryEntity.setSortIndex(category.getSortIndex());

            List<CategoryEntity.ItemsEntity> itemEntities = new ArrayList<>();
            for (ItemsEntity itemsEntity : category.getItems()) {
                CategoryEntity.ItemsEntity itemEntity = new CategoryEntity.ItemsEntity();
                itemEntity.setId(itemsEntity.getId());
                itemEntity.setCategoryId(category.getId());
                itemEntity.setNameEn(
                        itemsEntity.getNameEn()); // Set the name using the English name
                itemEntity.setNameAr(
                        itemsEntity.getNameAr()); // Set the name using the English name
                itemEntity.setCost(itemsEntity.getCost());

                List<CategoryEntity.ItemServicesEntity> itemServiceEntities = new ArrayList<>();
                for (ItemServicesEntity itemServicesEntity : itemsEntity.getItemServices()) {
                    CategoryEntity.ItemServicesEntity itemServiceEntity = new CategoryEntity.ItemServicesEntity();
                    itemServiceEntity.setId(itemServicesEntity.getId());
                    itemServiceEntity.setItemId(itemsEntity.getId());
                    itemServiceEntity.setCost(itemServicesEntity.getCost());

                    CategoryEntity.LaundryServicesEntity laundryServiceEntity = new CategoryEntity.LaundryServicesEntity();
                    laundryServiceEntity.setLaundryServiceId(
                            itemServicesEntity.getLaundryService().getId());
                    laundryServiceEntity.setNameEn(
                            itemServicesEntity.getLaundryService()
                                    .getNameEn()); // Set the name using the English name
                    laundryServiceEntity.setNameAr(
                            itemServicesEntity.getLaundryService()
                                    .getNameAr()); // Set the name using the English name
                    laundryServiceEntity.setIconUrl(
                            itemServicesEntity.getLaundryService()
                                    .getIconUrl());

                    itemServiceEntity.setLaundryService(laundryServiceEntity);
                    itemServiceEntities.add(itemServiceEntity);
                }

                itemEntity.setItemServices(itemServiceEntities);
                itemEntities.add(itemEntity);
            }

            categoryEntity.setItems(itemEntities);
            categoryEntities.add(categoryEntity);
        }
        return categoryEntities;
    }

    public String getName(String language) {
        return language.equals("en") ? nameEn : nameAr;
    }

    @Data
    public static class ItemsEntity
    {
        @Expose
        @SerializedName("id")
        private int id;
        @Expose
        @SerializedName("nameEn")
        private String nameEn;
        @Expose
        @SerializedName("nameAr")
        private String nameAr;

        @Getter(AccessLevel.NONE)
        private String name;
        @Expose
        @SerializedName("itemServices")
        private List<ItemServicesEntity> itemServices;


        @Expose
        @SerializedName("cost")
        private int cost;

        public String getName(String language) {
            return language.equals("en") ? nameEn : nameAr;
        }

    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class ItemServicesEntity
    {


        @Expose
        @SerializedName("laundryService")
        private LaundryServiceEntity laundryService;
        @Expose
        @SerializedName("id")
        private int id;

        @Expose
        @SerializedName("cost")
        private int cost;


    }

    @Data
    public static class LaundryServiceEntity
    {
        @Expose
        @SerializedName("id")
        private int id;
        @Expose
        @SerializedName("nameEn")
        private String nameEn;
        @Expose
        @SerializedName("nameAr")
        private String nameAr;

        @Getter(AccessLevel.NONE)
        private String name;

        @Expose
        @SerializedName("iconUrl")
        @Getter(AccessLevel.NONE)
        private String iconUrl;

        public String getIconUrl() {
         /*   if (!iconUrl.contains("5asec-ksa.com/icons/laundry-service/")) {
                url = "5asec-ksa.com/icons/laundry-service/" + iconUrl;
            }*/
            return iconUrl;
        }

        public String getName(String language) {
            return language.equals("en") ? nameEn : nameAr;
        }

    }


}
