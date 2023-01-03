package com.example.a5asec.data.model.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

@Data
@Entity(tableName = "table_laundry_service", foreignKeys = {@ForeignKey(entity = ItemService.class,
        parentColumns = "id",
        childColumns = "id_service"
        , onDelete = ForeignKey.CASCADE)
})
public class LaundryService
    {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public long id;


    @ColumnInfo(name = "id_service")
    public int idService;

    @ColumnInfo(name = "id_laundry_service")
    public int idLaundryService;

    @ColumnInfo(name = "cost")
    public int cost;

    @ColumnInfo(name = "icon_url")
    public String iconUrl;

    @ColumnInfo(name = "name_en")
    public String nameEn;
    @ColumnInfo(name = "name_ar")
    public String nameAr;

    @ColumnInfo(name = "laundry_flag")
    public int flag;

    @Getter(AccessLevel.NONE)
    @Ignore
    public String name;

    public LaundryService(int idLaundryService, int cost, String iconUrl, String nameEn, String nameAr)
        {
        this.idLaundryService = idLaundryService;
        this.cost = cost;
        this.iconUrl = iconUrl;
        this.nameEn = nameEn;
        this.nameAr = nameAr;
        }
    @Ignore
    public String getName(String language)
        {
        return language.equals("en") ? nameEn : nameAr;
        }

    }
