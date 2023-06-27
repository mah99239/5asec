package com.example.a5asec.data.model.api;

import com.example.a5asec.data.model.db.BannerEntity;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Banner
{
    private Integer id;
    private String url;
    private Boolean active;
    private Integer createdById;
    private String createdByName;
    private String createdAt;
    private String description;

    public static List<BannerEntity> asDatabaseModel(List<Banner> bannerList)
    {
        List<BannerEntity> banners = new ArrayList<>();
        for (Banner banner : bannerList) {
            banners.add(new BannerEntity(banner.id, banner.url, banner.active,
                    banner.createdById, banner.createdByName, banner.createdAt,
                    banner.description));
        }
        return banners;
    }

}
