package com.example.a5asec.ui.view.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.a5asec.data.model.api.Address;
import com.example.a5asec.data.model.api.City;
import com.example.a5asec.data.remote.api.ResponseEntity;
import com.example.a5asec.data.repository.AddressRepository;
import com.example.a5asec.utility.ApiError;
import com.example.a5asec.utility.Resource;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AddressViewModel extends ViewModel
    {

    private static final String TAG = "AddressViewModel";
    private final CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private MutableLiveData<Resource<List<Address>>> mResourceAddresses = new MutableLiveData<>();
    private MutableLiveData<Resource<Address>> mAddress = new MutableLiveData<>();
    private MutableLiveData<Resource<List<City>>> mCities = new MutableLiveData<>();
    private AddressRepository mAddressRepository;

    public AddressViewModel()
        {
        }

    public AddressViewModel(AddressRepository addressRepository)
        {
        mAddressRepository = addressRepository;
        }

    public LiveData<Resource<List<Address>>> getResourceAddresses()
        {
        fetchAddress();
        return mResourceAddresses;
        }

    public void setCreateAddress(Address.CreateAddress resourceAddresses)
        {
        addAddress(resourceAddresses);
        }
    public void setUpdateAddress(Address.CreateAddress resourceAddresses)
        {
        updateAddress(resourceAddresses);
        }

    public LiveData<Resource<List<City>>> getCities()
        {
        fetchCities();
        return mCities;
        }

    public LiveData<Resource<Address>> getAddress()
        {

        return mAddress;
        }

    public void setAddress(Address address)
        {
        mAddress.postValue(Resource.success(address));
        }

    private void fetchAddress()
        {

        mResourceAddresses.postValue(Resource.loading(null));
        mCompositeDisposable.add(
                mAddressRepository.getAddress()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())

                        .subscribeWith(new DisposableObserver<List<Address>>()
                            {
                            @Override
                            public void onNext(@NonNull List<Address> listResource)
                                {
                                mResourceAddresses.postValue(Resource.success(listResource));
                                Log.e(TAG, "fetchAddress, onNext");

                                }

                            @Override
                            public void onError(@NonNull Throwable e)
                                {
                                Log.e(TAG, e.getMessage());
                                mResourceAddresses.postValue(Resource.error(ApiError.handleApiError(e), null));
                                Log.e(TAG, "fetchAddress, onError");

                                }

                            @Override
                            public void onComplete()
                                {
                                Log.e(TAG, "fetchAddress, onComplete");
                                mCompositeDisposable.remove(this);
                                }
                            }));


        }


    private void fetchCities()
        {

        mResourceAddresses.postValue(Resource.loading(null));
        mCompositeDisposable.add(
                mAddressRepository.getCities()
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableObserver<List<City>>()
                            {
                            @Override
                            public void onNext(@NonNull List<City> listResource)
                                {

                                mCities.postValue(Resource.success(checkCityIsCovered(listResource)));

                                }

                            @Override
                            public void onError(@NonNull Throwable e)
                                {
                                Log.e(TAG, "fetchCities, onError =" + e.getMessage());
                                mCities.postValue(Resource.error(ApiError.handleApiError(e), null));

                                }

                            @Override
                            public void onComplete()
                                {
                                Log.e(TAG, "fetchCities, onComplete");
                                mCompositeDisposable.remove(this);

                                }
                            }));


        }

    private List<City> checkCityIsCovered(List<City> cities)
        {
        var cityCovered = new ArrayList<City>();
        if(!cities.isEmpty())
            {

            cities.forEach(city ->
                {
                if (city.getIsCovered())
                    {
                    cityCovered.add(city);
                    }
                });
            }
        return cityCovered;
        }
    private void addAddress(Address.CreateAddress address)
        {
        mAddress.postValue(Resource.loading(null));
        mCompositeDisposable.add(
                mAddressRepository.addAddress(address)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())

                        .subscribeWith(new DisposableSingleObserver<>()
                            {
                            @Override
                            public void onSuccess(@NonNull Address address)
                                {
                                mAddress.postValue(Resource.success(address));
                                }

                            @Override
                            public void onError(@NonNull Throwable e)
                                {
                                Log.e(TAG, e.getMessage());
                                mAddress.postValue(Resource.error(e.getMessage(), null));

                                }
                            }));


        }

    public void setPrimaryAddress(int id)
        {
        primaryAddress(id);
        }

    private void primaryAddress(int id)
        {
        mResourceAddresses.postValue(Resource.loading(null));
        mCompositeDisposable.add(
                mAddressRepository.primaryAddress(id)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())

                        .subscribeWith(new DisposableSingleObserver<>()
                            {
                            @Override
                            public void onSuccess(@NonNull Address address)
                                {

                                }

                            @Override
                            public void onError(@NonNull Throwable e)
                                {
                                Log.e(TAG, e.getMessage());

                                }
                            }));


        }

    private void updateAddress(Address.CreateAddress address)
        {
        mResourceAddresses.postValue(Resource.loading(null));
        mCompositeDisposable.add(
                mAddressRepository.updateAddress(address)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())

                        .subscribeWith(new DisposableSingleObserver<>()
                            {
                            @Override
                            public void onSuccess(@NonNull Address address)
                                {

                                }

                            @Override
                            public void onError(@NonNull Throwable e)
                                {
                                Log.e(TAG, e.getMessage());

                                }
                            }));


        }

    private void deleteAddress(int id)
        {
        mResourceAddresses.postValue(Resource.loading(null));
        mCompositeDisposable.add(
                mAddressRepository.deleteAddress(id)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())

                        .subscribeWith(new DisposableSingleObserver<>()
                            {

                            @Override
                            public void onSuccess(@NonNull ResponseEntity responseEntity)
                                {
                                Log.e(TAG, responseEntity.getStatusCode());

                                }

                            @Override
                            public void onError(@NonNull Throwable e)
                                {
                                Log.e(TAG, e.getMessage());

                                }
                            }));


        }

    /**
     * called to reload data
     */
    public void reload()
        {
        fetchAddress();
        }

    @Override
    protected void onCleared()
        {
        super.onCleared();
        mCompositeDisposable.clear();
        mCompositeDisposable.dispose();
        }

    }
