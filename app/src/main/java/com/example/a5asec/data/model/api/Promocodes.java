package com.example.a5asec.data.model.api;


import java.util.Objects;

import lombok.Data;

@Data
public final class Promocodes
    {
    private final int value;
    private final boolean used;
    private final String start;
    private final int id;
    private final String expirationDate;
    private final String discountType;
    private final int days;
    private final String createdByName;
    private final int createdById;
    private final String createdAt;
    private final String code;
    private final String clientName;
    private final int clientId;


    }