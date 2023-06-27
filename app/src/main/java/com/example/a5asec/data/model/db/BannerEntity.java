package com.example.a5asec.data.model.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.a5asec.data.model.api.Banner;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity(tableName = "banner")
@AllArgsConstructor
@NoArgsConstructor
public class BannerEntity
{
    @PrimaryKey
    public Integer id;
    public String url;
    public Boolean active;
    public Integer createdById;
    public String createdByName;
    public String createdAt;
    public String description;

    public static List<Banner> asDomainModel(List<BannerEntity> bannerEntities) {
        List<Banner> banners = new ArrayList<>();
        for (BannerEntity bannerNetwork : bannerEntities) {
            banners.add(new Banner(bannerNetwork.id, bannerNetwork.url,
                    bannerNetwork.active, bannerNetwork.createdById,
                    bannerNetwork.createdByName, bannerNetwork.createdAt,
                    bannerNetwork.description));
        }
        return banners;
    }
}
