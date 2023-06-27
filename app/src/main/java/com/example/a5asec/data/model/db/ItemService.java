package com.example.a5asec.data.model.db;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

@Data
@Entity(tableName = "table_item_service")
public class ItemService implements Parcelable
    {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "cost_item_service")
    public int costItemService;

    @ColumnInfo(name = "id_item_service")
    public int idItemService;

    @ColumnInfo(name = "count")
    public int count;

    @ColumnInfo(name = "name_en")
    public String nameEn;

    @ColumnInfo(name = "name_ar")
    public String nameAr;

    @ColumnInfo(name = "flag")
    public int flag;

    @Getter(AccessLevel.NONE)
    @Ignore
    public String name;

    public ItemService(String nameEn, String nameAr, int costItemService, int idItemService, int count)
        {
        this.nameEn = nameEn;
        this.nameAr = nameAr;
        this.costItemService = costItemService;
        this.idItemService = idItemService;
        this.count = count;
        }

    public String getName(String language)
        {
        return language.equals("en") ? nameEn : nameAr;
        }


    protected ItemService(Parcel in) {
    id = in.readInt();
    costItemService = in.readInt();
    idItemService = in.readInt();
    count = in.readInt();
    nameEn = in.readString();
    nameAr = in.readString();
    flag = in.readInt();
    name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(id);
    dest.writeInt(costItemService);
    dest.writeInt(idItemService);
    dest.writeInt(count);
    dest.writeString(nameEn);
    dest.writeString(nameAr);
    dest.writeInt(flag);
    dest.writeString(name);
    }

    @Override
    public int describeContents() {
    return 0;
    }

    public static final Creator<ItemService> CREATOR = new Creator<ItemService>() {
    @Override
    public ItemService createFromParcel(Parcel in) {
    return new ItemService(in);
    }

    @Override
    public ItemService[] newArray(int size) {
    return new ItemService[size];
    }
    };
    }
