package com.example.a5asec.data.model.db;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

import lombok.Data;

@Data
public class ServiceAndLaundryService
    {
    @Embedded public ItemService service;

    @Relation(parentColumn = "id",
    entityColumn = "id_service")
    public  List<LaundryService> laundryServices;


    }
