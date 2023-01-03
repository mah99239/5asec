package com.example.a5asec.data.repository;

import com.example.a5asec.data.model.api.Address;
import com.example.a5asec.data.model.api.City;
import com.example.a5asec.data.remote.api.AddressHelper;
import com.example.a5asec.data.remote.api.ResponseEntity;

import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public class AddressRepository
    {
    private static final String TAG = "AddressRepository";
    private AddressHelper mAddressHelper;


    public AddressRepository(AddressHelper address)
        {
        this.mAddressHelper = address;
        }

    @NonNull
    public Observable<List<Address>> getAddress()
        {
        return mAddressHelper.getAddress();
        }

    public Observable<List<City>> getCities()
        {
        return mAddressHelper.getCities();
        }

    public Single<ResponseEntity> deleteAddress(int id){return mAddressHelper.deleteAddress(id);}

    public  Single<Address> addAddress( Address.CreateAddress address){return mAddressHelper.addAddress(address);}

    public  Single<Address> updateAddress( Address.CreateAddress address){return mAddressHelper.updateAddress(address);}
    public Single<Address> primaryAddress(int id)

        {
        return mAddressHelper.primaryAddress(id);
        }
    }
