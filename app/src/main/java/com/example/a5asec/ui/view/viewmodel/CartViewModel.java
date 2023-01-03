package com.example.a5asec.ui.view.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.a5asec.data.local.db.dao.OrderDataSource;
import com.example.a5asec.data.model.db.ItemService;
import com.example.a5asec.data.model.db.LaundryService;
import com.example.a5asec.data.model.db.ServiceAndLaundryService;
import com.example.a5asec.utility.Resource;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CartViewModel extends ViewModel
    {
    private static final String TAG = "CartViewModel";
    private OrderDataSource mDataSource;
    private MutableLiveData<Resource<List<ServiceAndLaundryService>>> mOrderMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<ServiceAndLaundryService> mOrder = new MutableLiveData<>();
   private MutableLiveData<Resource<Integer>>mCountService = new MutableLiveData<>();
    private MutableLiveData<Resource<Integer>> mSumAllOrder = new MutableLiveData<>();
    private MutableLiveData<Resource<Integer>> mSumOrder = new MutableLiveData<>();
    private MutableLiveData<Resource<ArrayList<Integer>>> mSumAllOrders = new MutableLiveData<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private MutableLiveData<List<Integer>> mItemServiceChecked = new MutableLiveData<>();

    public CartViewModel(OrderDataSource dataSource)
        {
        this.mDataSource = dataSource;
        }

    public LiveData<Resource<List<ServiceAndLaundryService>>> getAllOrder()
        {
        fetchAllOrder();
        return mOrderMutableLiveData;
        }

    private void fetchAllOrder()
        {
        mOrderMutableLiveData.postValue(Resource.loading(null));

        compositeDisposable.add(mDataSource.getAllOrder().
                subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

                .subscribeWith(new DisposableObserver<List<ServiceAndLaundryService>>()
                    {
                    @Override
                    public void onNext(@NonNull List<ServiceAndLaundryService> serviceAndLaundryServices)
                        {
                        mOrderMutableLiveData.postValue(Resource.success(serviceAndLaundryServices));
                        setSumAllOrder(serviceAndLaundryServices);
                        }

                    @Override
                    public void onError(@NonNull Throwable e)
                        {
                        mOrderMutableLiveData.postValue(Resource.error(e.toString(), null));
                        Log.e(TAG, "onError = " + e);

                        }

                    @Override
                    public void onComplete()
                        {
                        Log.e(TAG, "onComplete = ");

                        }
                    }));


        }


    public void addItemToUpdateService(ServiceAndLaundryService item)
        {
        mOrder.setValue(item);
        Log.e(TAG, "addItemToUpdateService = " + mOrder.getValue());
        setItemLaundryServiceChecked();
        }

    public void setItemLaundryServiceChecked()
        {
        mItemServiceChecked.postValue(getAllIdCheckItemLaundryService());
        Log.e(TAG, "setItemLaundryServiceChecked = " + mOrder.getValue());

        }

    public LiveData<List<Integer>> getItemLaundryServiceChecked()
        {
        return mItemServiceChecked;
        }

    private List<Integer> getAllIdCheckItemLaundryService()
        {
        Log.e(TAG, "Item Cart = " + mOrder.getValue());
        List<Integer> allId = new ArrayList<>();

        mOrder.getValue().getLaundryServices().forEach(laundryService ->
            {
            if (laundryService.getFlag() == 0) allId.add(laundryService.getIdLaundryService());
            }
        );

        return allId;
        }

    public void addService(ItemService service, List<LaundryService> laundryServices)
        {
        insertService(service, laundryServices);
        }


    public LiveData<ServiceAndLaundryService> getItemToUpdateService()
        {
        return mOrder;
        }


    public LiveData<Resource<ArrayList<Integer>>> getSumService()
        {
        return mSumAllOrders;
        }

    private void insertService(ItemService service, List<LaundryService> laundryServices)
        {
        mDataSource.insertOrUpdateService(service)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterSuccess(id ->
                    {
                    int selectionLaundryService = laundryServices.size();

                    if (selectionLaundryService > 0)
                        {
                        int idService = Math.toIntExact(id);
                        Log.e(TAG, "insertService, idService =  " + idService);

                        laundryServices.forEach(laundryService ->
                            {
                            laundryService.setIdService(idService);
                            Log.e(TAG, "insertService into setIdService idService =  " + laundryService.getIdService());

                            });
                        insertLaundryService(laundryServices);

                        refreshCart();
                        }
                    })
                .subscribe(new SingleObserver<>()
                    {
                    @Override
                    public void onSubscribe(@NonNull Disposable d)
                        {

                        }

                    @Override
                    public void onSuccess(@NonNull Long aLong)
                        {
                        Log.e(TAG, "onSuccess, idService =  " + aLong);
                        getCountOrder();
                        }

                    @Override
                    public void onError(@NonNull Throwable e)
                        {
                        Log.e(TAG, "onSuccess, idService =  " + e);

                        }
                    });


        }

    private void insertLaundryService(List<LaundryService> laundryServices)
        {

        compositeDisposable.add(mDataSource.insertOrUpdateLaundryService(laundryServices)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(this::refreshCart)
                .subscribe(() ->
                            {
                            Log.e(TAG, "insertLaundryService, Complete");
                            }
                        ,
                        throwable -> Log.e(TAG, "insertLaundryService, error: " + throwable.getMessage())));

        }

    public void updateService(ItemService service)
        {
        mDataSource.updateService(service)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(this::refreshCart)
                .subscribe(new SingleObserver<>()
                    {
                    @Override
                    public void onSubscribe(@NonNull Disposable d)
                        {

                        }

                    @Override
                    public void onSuccess(@NonNull Integer aLong)
                        {
                        Log.e(TAG, "onSuccess, idService =  " + aLong);
                        getCountOrder();
                        }

                    @Override
                    public void onError(@NonNull Throwable e)
                        {
                        Log.e(TAG, "onError, idService =  " + e);

                        }
                    });


        }

    public void updateLaundryService(List<LaundryService> laundryServices)
        {
        mDataSource.insertOrUpdateLaundryService(laundryServices)
                .subscribeOn(Schedulers.io())
                .doFinally(this::refreshCart)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> Log.e(TAG, "updateLaundryService, Complete")
                        ,
                        throwable -> Log.e(TAG, "updateLaundryService, error: " + throwable.getMessage()));
        }

    private void getCountOrder()
        {
        compositeDisposable.add(
                mDataSource.getCountOrder().subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableSingleObserver<Integer>()
                            {


                            @Override
                            public void onSuccess(@NonNull Integer count)
                                {
                                mCountService.postValue(Resource.success(count));
                                Log.e(TAG, "getCountOrder, count = " + count );

                                }

                            @Override
                            public void onError(@NonNull Throwable e)
                                {
                                Log.e(TAG,"getCountOrder, ERROR"  + e.getMessage() );
                                mCountService.postValue(Resource.empty(null));

                                }
                            }));

        }

    public LiveData<Resource<Integer>> getCount()
        {
        getCountOrder();
        return mCountService;
        }

    public LiveData<Resource<Integer>> getSumAllOrder()
        {
        // sumCostAllService();
        return mSumAllOrder;
        }

    public void setSumAllOrder(List<ServiceAndLaundryService> services)
        {
        ArrayList<Integer> sumAllOrder = new ArrayList<>();
        mSumAllOrders.postValue(Resource.loading(null));
        if (services.size() == 0)
            {
            mSumAllOrders.postValue(Resource.empty(null));
            } else
            {

            services.forEach(serviceAndLaundryService ->
                {


                int total = serviceAndLaundryService.getService().getCostItemService();
                for (LaundryService laundryService : serviceAndLaundryService.getLaundryServices())
                    {
                    total += laundryService.getCost();
                    }

                total *= serviceAndLaundryService.getService().getCount();

                Log.e(TAG, "total = " + total);
                sumAllOrder.add(total);

                });
            mSumAllOrders.postValue(Resource.success(sumAllOrder));
            }
        }

    public void setTotalAllOrder()
        {


        int total = 0;
        for (int i : mSumAllOrders.getValue().getMData())
            {
            total += i;
            }
        mSumOrder.postValue(Resource.success(total));

        }

    public LiveData<Resource<Integer>> getTotalAllOrder()
        {

        //  sumCostService(id);
        return mSumOrder;
        }


    public void removeItemService(ItemService pos)
        {
        compositeDisposable.add(
                mDataSource.delete(pos).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(count ->
                            {

                            getSumAllOrder();

                            Log.e(TAG, "count of order = " + count);
                            }
                        ));
        }

    public void removeLaundryService(int pos)
        {
        compositeDisposable.add(
                mDataSource.deleteLaundryService(pos).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doAfterSuccess(integer ->
                            {
                            refreshCart();
                            })
                        .subscribe(count ->
                            {

                            Log.e(TAG, "removeLaundryService = " + count);
                            }
                        ));
        }

    public void removeAllService()
        {
        compositeDisposable.add(
                mDataSource.deleteAllOrder().subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doAfterSuccess(integer ->
                            {
                            mDataSource.restIdService();
                            mDataSource.restIdLaundryService();
                            })
                        .subscribe(count ->
                            {

                            Log.e(TAG, "removeLaundryService = " + count);
                            }
                        ));
        }

    public void refreshCart()
        {
        getSumAllOrder();
        getCount();
        getCountOrder();
        fetchAllOrder();
        }

    @Override
    protected void onCleared()
        {
        super.onCleared();
        if (compositeDisposable != null && !compositeDisposable.isDisposed())
            {
            compositeDisposable.clear();
            }
        }
    }
