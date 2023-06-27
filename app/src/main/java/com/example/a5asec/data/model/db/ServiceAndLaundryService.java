package com.example.a5asec.data.model.db;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import lombok.Data;

@Data
public class ServiceAndLaundryService implements Parcelable {
    public static final Creator<ServiceAndLaundryService> CREATOR =
            new Creator<>() {
                @Override
                public ServiceAndLaundryService createFromParcel(Parcel in) {
                    return new ServiceAndLaundryService(in);
                }

                @Override
                public ServiceAndLaundryService[] newArray(int size) {
                    return new ServiceAndLaundryService[size];
                }
            };
    @Embedded
    public final ItemService service;
    @Relation(parentColumn = "id", entityColumn = "id_service")
    public final List<LaundryService> laundryService;

    public ServiceAndLaundryService(@NonNull ItemService service,
                                    @NonNull List<LaundryService> laundryService) {
        this.service = Objects.requireNonNull(service);
        this.laundryService = Objects.requireNonNull(laundryService);
    }

    protected ServiceAndLaundryService(Parcel in) {
        List<LaundryService> laundryService1;
        service = in.readParcelable(ItemService.class.getClassLoader());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            laundryService1 = in.readParcelableList(Collections.emptyList(),
                    LaundryService.class.getClassLoader());
        }
        else {
            laundryService1 = in.createTypedArrayList(LaundryService.CREATOR);
            if (laundryService1 == null) {
                laundryService1 = Collections.emptyList();
            }
        }
        laundryService = laundryService1;
    }

    public ItemService getService() {return service;}

    public List<LaundryService> getLaundryServiceWithFlagZero() {
        List<LaundryService> laundryServices = new ArrayList<>();
        for (LaundryService laundryServiceItem : laundryService) {
            if (laundryServiceItem.flag == 0) {
                laundryServices.add(laundryServiceItem);
            }
        }
        return laundryServices;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(service, flags);
        dest.writeTypedList(laundryService);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}