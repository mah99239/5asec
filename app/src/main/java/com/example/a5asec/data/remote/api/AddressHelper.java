package com.example.a5asec.data.remote.api;

import com.example.a5asec.data.model.api.Address;
import com.example.a5asec.data.model.api.City;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public class AddressHelper
    {
    private final AddressApi mAddressApi;

    public AddressHelper(AddressApi addressApi)
        {
        this.mAddressApi = addressApi;
        }

    public Observable<List<Address>> getAddress()
        {
        return mAddressApi.getAddress();
        }
    public Observable<List<City>> getCities()
        {
        return mAddressApi.getCities();
        }


    public  Single<ResponseEntity> deleteAddress( int id){return mAddressApi.deleteAddress(id);}

    public  Single<Address> addAddress( Address.CreateAddress address){return mAddressApi.addAddress(address);}

    public  Single<Address> updateAddress( Address.CreateAddress address){return mAddressApi.updateAddress(address);}
    public Single<Address> primaryAddress(int id)

        {
        return mAddressApi.primaryAddress(id);
        }
    }
