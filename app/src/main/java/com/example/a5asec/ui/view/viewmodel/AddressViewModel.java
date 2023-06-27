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
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableCompletableObserver;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AddressViewModel extends ViewModel
    {

    private static final String TAG = "AddressViewModel";
     final CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    MutableLiveData<Resource<List<Address>>> mResourceAddresses = new MutableLiveData<>();
    private final MutableLiveData<Resource<Address>> mAddress = new MutableLiveData<>();
    private final MutableLiveData<Resource<Address>> mUpdateAddress = new MutableLiveData<>();
    private final MutableLiveData<Resource<Address>> mUpdatePrimaryAddress = new MutableLiveData<>();
    private final MutableLiveData<Resource<ResponseEntity>> mResponse = new MutableLiveData<>();
    private final MutableLiveData<Resource<List<City>>> mCities = new MutableLiveData<>();
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
    private void fetchAddress()
        {

        mResourceAddresses.postValue(Resource.loading(null));
        mCompositeDisposable.add(
                mAddressRepository.getAddress()
                        .subscribeOn(Schedulers.io())
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


    public void setCreateAddress(Address.CreateAddress resourceAddresses)
        {
        addAddress(resourceAddresses);
        }

    private void addAddress(Address.CreateAddress address)
        {
        mAddress.postValue(Resource.loading(null));
        mCompositeDisposable.add(
                mAddressRepository.addAddress(address)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())

                        .subscribeWith(new DisposableSingleObserver<>()
                            {
                            @Override
                            public void onSuccess(@NonNull Address address)
                                {
                                Log.e(TAG, "addAddress, onSuccess");

                                mAddress.postValue(Resource.success(address));

                                }

                            @Override
                            public void onError(@NonNull Throwable e)
                                {
                                Log.e(TAG, e.getMessage());
                                mAddress.postValue(Resource.error(e.getMessage(), null));
                                mCompositeDisposable.remove(this);

                                }
                            }));


        }

    public LiveData<Resource<Address>> getUpdateAddress()
        {
        return mUpdateAddress;
        }
    public void setUpdateAddress(Address.UpdateAddress resourceAddresses)
        {
        int id = mAddress.getValue().getMData().getId();
        resourceAddresses.setId(id);
        updateAddress(resourceAddresses);
        Log.e(TAG, "setUpdateAddress");

        }

    private void updateAddress(Address.UpdateAddress address)
        {
        Log.e(TAG, "updateAddress = " + address);
        Log.e(TAG, "updateAddress, mAddressRepository = " + mAddressRepository);
        Log.e(TAG, "updateAddress, mCompositeDisposable , size = " + mCompositeDisposable.size());
        mUpdateAddress.postValue(Resource.loading(null));

       mCompositeDisposable.add(
                mAddressRepository.updateAddress(address)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())

                        .subscribeWith(new DisposableSingleObserver<>()
                            {


                            @Override
                            public void onSuccess(@NonNull Address address)
                                {
                                Log.e(TAG, "updateAddress, onSuccess");
                                mUpdateAddress.postValue(Resource.success(address));

                                //reload();
                                }

                            @Override
                            public void onError(@NonNull Throwable e)
                                {
                                Log.e(TAG, "updateAddress, onError = " + e.getMessage());

                                Log.e(TAG, e.getMessage());
                                mUpdateAddress.postValue(Resource.error(e.getMessage(), null));

                                }
                            }));


        }


    public LiveData<Resource<List<City>>> getCities()
        {
        fetchCities();
        return mCities;
        }
    private void fetchCities()
        {

        mResourceAddresses.postValue(Resource.loading(null));
        mCompositeDisposable.add(
                mAddressRepository.getCities()
                        .subscribeOn(Schedulers.io())
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
        if (!cities.isEmpty())
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

    public LiveData<Resource<Address>> getAddress()
        {

        return mAddress;
        }

    public void setAddress(Address address)
        {
        mAddress.postValue(Resource.success(address));
        }






    public LiveData<Resource<List<Address>>> setPrimaryAddress(int id)
        {
        primaryAddress(id);
        return mResourceAddresses;
        }

    private void primaryAddress(long id)
        {
        Log.e(TAG, "primaryAddress, GO ON,   "  );
        mResourceAddresses.postValue(Resource.loading(null));

        mCompositeDisposable.add(
                mAddressRepository.primaryAddress(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())

                        .subscribeWith(new DisposableSingleObserver<>()
                            {

                            @Override
                            public void onSuccess(@NonNull List<Address> address)
                                {
                                Log.e(TAG, "primaryAddress, onSuccess, address = " + address );
                                mResourceAddresses.postValue(Resource.success(address));

                                }

                            @Override
                            public void onError(@NonNull Throwable e)
                                {
                                mResourceAddresses.postValue(Resource.error(e.getMessage() ,null));

                                Log.e(TAG, "primaryAddress, OnError = " + e.getMessage());

                                }
                            }));


        }



    public LiveData<Resource<ResponseEntity>> deleteAddress()
        {
        int id = Objects.requireNonNull(mAddress.getValue()).getMData().getId();
        deleteAddress(id);
        return mResponse;
        }

    private void deleteAddress(int id)
        {
      //  mResourceAddresses.postValue(Resource.loading(null));
        mCompositeDisposable.add(
                mAddressRepository.deleteAddress(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())

                        .subscribeWith(new DisposableCompletableObserver()
                            {


                            @Override
                            public void onComplete()
                                {
                                reload();
                                mResponse.postValue(Resource.success(new ResponseEntity()));
                                }

                            @Override
                            public void onError(@NonNull Throwable e)
                                {
                                Log.e(TAG, "onError " + e.getMessage());
                                mResponse.postValue(Resource.error(e.getMessage(), null));
                                reload();


                                }
                            }));


        }


    @Override
    protected void onCleared()
        {
        super.onCleared();
        mCompositeDisposable.dispose();
        }

    }
