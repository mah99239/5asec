package com.example.a5asec.ui.view.viewmodel;

import android.util.Log;

import androidx.databinding.ObservableInt;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.a5asec.data.model.api.Category;
import com.example.a5asec.data.model.db.ItemService;
import com.example.a5asec.data.model.db.LaundryService;
import com.example.a5asec.data.model.db.ServiceAndLaundryService;
import com.example.a5asec.data.repository.CartRepository;
import com.example.a5asec.utility.Resource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subscribers.ResourceSubscriber;
import lombok.Getter;
import timber.log.Timber;

@HiltViewModel
public class CartViewModel extends ViewModel
{
    private static final String TAG = "CartViewModel";

    private final CartRepository mCartRepositroy;

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    /**
     * Used to store item laundry service id  in laundry service BottomSheet.
     */
    @Getter
    private final Set<Integer> selectedItemServicesId;


    @Getter
    private final Set<Integer> selectedCartRemoveId;

    /**
     * Stored sum all cost with items when user selected item and show in UI
     */
    private final ObservableInt selectedItemServiceSum = new ObservableInt();
    /**
     * items count in Service with data base.
     */
    private final MutableLiveData<Resource<Integer>> mCountService = new MutableLiveData<>();
    /**
     * stored total all cost in item service.
     */
    private final MutableLiveData<Integer> totalItemService = new MutableLiveData<>();

    /**
     * Show all Items service stored in database
     **/
    private final MutableLiveData<Resource<List<ServiceAndLaundryService>>> mListOrder =
            new MutableLiveData<>();
    private final List<ServiceAndLaundryService> mListOrderDeleted = new ArrayList<>();
    /**
     * use to save old item service selected to use when update item services.
     */
    private final MutableLiveData<Set<Integer>> mItemServiceSelectedToUpdate =
            new MutableLiveData<>();
    /**
     * Show Item service when user selected item who stored in ListOrder.
     */
    private final MutableLiveData<ServiceAndLaundryService> mItemInCartSelected =
            new MutableLiveData<>();
    /**
     * stored Laundry Service when user selected item, and use to insert into database.
     */
    @Getter
    private final List<LaundryService> selectionLaundryService = new ArrayList<>();
    /**
     * Used to store count of item in laundry service BottomSheet.
     */
    private ObservableInt mCountObservable;
    /**
     * Used to initialize and set data to store in data base.
     */
    @Getter
    private ItemService selectedItemService;
    /**
     * stored sum cost all item when use item selected.
     */
    @Getter
    private int costSelectedWithService;

    @Inject
    public CartViewModel(CartRepository cartRepository)
    {
        setCountObservable();
        mCartRepositroy = cartRepository;
        selectedItemServicesId = new HashSet<>();
        selectedCartRemoveId = new HashSet<>();
        fetchAllOrder();

    }


    /**
     * Used to add remove id in selectedLaundryServiceId when user selected item.
     *
     * @param id Item Laundry Service
     */
    public void addSelectionLaundryService(int id)
    {
        selectedItemServicesId.add(id);
    }

    /**
     * Used to remove id in selectedLaundryServiceId when user reselected.
     *
     * @param id Item Laundry Service
     */
    public void removeSelectionLaundryService(int id)
    {
        selectedItemServicesId.remove(id);
    }

    /**
     * Add id Item to remove in Cart fragment
     *
     * @param id item Cart selected
     */
    public void addSelectedCartWithId(int id)
    {
        selectedCartRemoveId.add(id);
    }

    public void removeSelectedCartId(int id)
    {
        selectedCartRemoveId.remove(id);
    }

    public void clearItemDeletedSelection()
    {
        selectedCartRemoveId.clear();
        mListOrderDeleted.clear();
    }

    /**
     * Used count in UI and used to store count of item service in database.
     *
     * @return count of item service
     */
    public ObservableInt getCountObservable()
    {
        return mCountObservable;
    }

    /**
     * Used to set count in Laundry service bottomSheet
     */
    public void setCountObservable()
    {
        mCountObservable = new ObservableInt(1);
    }

    /**
     * Called when user select button plus in UI and call in layout Laundry Service BottomSheet.
     */
    public void plusCount()
    {
        mCountObservable.set(mCountObservable.get() + 1);
    }

    /**
     * Called when user select button minus in UI and call in layout Laundry Service BottomSheet.
     */
    public void minusCount()
    {
        if (mCountObservable.get() > 1) {
            mCountObservable.set(mCountObservable.get() - 1);
        }
    }

    /**
     * Called when user selected item LaundryServiceBottomSheet and used item to initialize in
     * ItemService.
     *
     * @param item of @Category.ItemsEntity used to save in database.
     */
    public void setSelectedItemService(Category.ItemsEntity item)
    {
        String nameEn = item.getNameEn();
        String nameAr = item.getNameAr();
        int costItemService = item.getCost();
        int idItemService = item.getId();
        selectedItemService = new ItemService(nameEn, nameAr, costItemService,
                idItemService, mCountObservable.get());
    }


    /**
     * Called when user selected item LaundryServiceBottomSheet and used item to initialize in
     * LaundryService.
     *
     * @param items of @Category.ItemServicesEntity used to save in database.
     */
    public void setSelectionLaundryService(List<Category.ItemServicesEntity> items)
    {
        for (Category.ItemServicesEntity item : items) {
            AtomicInteger flag = new AtomicInteger(1);
            getSelectedItemServicesId().forEach(id -> {
                if (id == item.getId()) {
                    flag.set(0);
                }
            });

            int id = item.getId();
            int cost = item.getCost();
            String url = item.getLaundryService().getIconUrl();
            String nameEN = item.getLaundryService().getNameEn();
            String nameAr = item.getLaundryService().getNameAr();

            selectionLaundryService.add(
                    (new LaundryService(id, cost, url, nameEN, nameAr, flag.get())));
        }
    }

    /**
     * Called when user select new item laundryServiceBottomSheet, rest cost of all item in UI.
     */
    public void restCostSelectedWithService()
    {
        costSelectedWithService = 0;
    }

    /**
     * Sum all cost of item selected in LaundryServiceBottomSheet.
     *
     * @param item used to get cost all item selected.
     */
    public void setCostSelectedWithService(Category.ItemServicesEntity item)
    {
        int cost = item.getCost();
        costSelectedWithService = costSelectedWithService + cost;
    }

    /**
     * Show all cost salary of item in LaundryServiceBottomSheet.
     *
     * @return cost all items selected.
     */
    public ObservableInt getSelectedItemServiceSum()
    {
        return selectedItemServiceSum;
    }

    /**
     * Called when user select new item or remove to get new sum of item and show  in
     * LaundryServiceBottomSheet.
     *
     * @param itemCost of Item Service
     */
    public void sumAllSelectionLaundryService(int itemCost)
    {
        int sum = itemCost + costSelectedWithService;
        selectedItemServiceSum.set(sum);
    }


    /**
     * Called when user clicked insert or update button in UI
     * {@link com.example.a5asec.ui.view.home.LaundryServicesBottomSheet}
     *
     * @param service         of current item service selected in UI
     *                        {@link com.example.a5asec.ui.view.home.LaundryServicesBottomSheet}
     * @param laundryServices all item selected in UI
     * @{@link com.example.a5asec.ui.view.home.LaundryServicesBottomSheet}
     */
    public void addService(ItemService service, List<LaundryService> laundryServices)
    {
        insertService(service, laundryServices);
    }

    /**
     * Insert a service and List of Laundry service if selected in UI.
     *
     * @param service              get service from item service of {@link  ItemService}
     * @param itemsLaundryServices get list of Laundry service{@link LaundryService}
     */
    private void insertService(ItemService service, List<LaundryService> itemsLaundryServices)
    {
        compositeDisposable.add(
                insertService(service)
                        .doAfterSuccess(id -> {
                            int selectedItemLaundryService = itemsLaundryServices.size();

                            if (selectedItemLaundryService > 0) {
                                int idService = Math.toIntExact(id);
                                traceWithLog(
                                        "insertService, idService =  %s" + idService);
                                setIdServiceInLaundryService(
                                        itemsLaundryServices, idService);

                                insertLaundryService(itemsLaundryServices);

                            }
                        }).subscribeWith(new DisposableSingleObserver<Long>()
                        {

                            @Override
                            public void onSuccess(@NonNull Long aLong)
                            {
                                traceWithLog("onSuccess, idService =  %s",
                                        aLong.toString());
                            }

                            @Override
                            public void onError(@NonNull Throwable e)
                            {
                                traceWithLog(e.getMessage());

                            }
                        }));
    }


    private Single<Long> insertService(ItemService itemService)
    {
        //if itemService is null then return  a singleton instance of a never-signaling Single
        // (only calls onSubscribe).
        //else insert item service in database.
        if (itemService == null) return Single.never();
        return mCartRepositroy.insertService(itemService);

    }

    private Single<Integer> updateService(ItemService itemService)
    {
        //if itemService is null then return  a singleton instance of a never-signaling Single
        // (only calls onSubscribe).
        //else insert item service in database.
        if (itemService == null) return Single.never();
        return mCartRepositroy.updateService(itemService);

    }

    /**
     * set id in laundry service record  with item service of record in database.
     *
     * @param idService get id in database
     */
    private void setIdServiceInLaundryService(List<LaundryService> itemsLaundryService,
                                              int idService)
    {
        itemsLaundryService.forEach(laundryService -> {
            laundryService.setIdService(idService);

            traceWithLog(
                    "insertService into setIdService idService = " + " %s" + laundryService.getIdService());

        });
    }

    /**
     * Called when user selected item and go to insert.
     *
     * @param laundryServices get from items selected in @{
     *                        {@link com.example.a5asec.ui.view.home.LaundryServicesBottomSheet}}
     */
    private void insertLaundryService(List<LaundryService> laundryServices)
    {
        compositeDisposable.add(
                insertOrUpdateLaundryService(laundryServices).subscribeOn(
                                Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnComplete(this::refreshCart).subscribe(
                                () -> Timber.tag(TAG)
                                        .e("insertLaundryService, Complete"),
                                throwable -> Timber.tag(TAG)
                                        .e("insertLaundryService, error: %s",
                                                throwable.getMessage())));

    }

    /**
     * update or insert or update list of laundry service in database.
     *
     * @return
     */
    public Completable insertOrUpdateLaundryService(List<LaundryService> laundryServices)
    {
        // if no item of laundry service, then return null
        //else insert list of laundry service.
        if (laundryServices == null) return null;
        return mCartRepositroy.insertLaundryService(laundryServices);
    }

    /**
     * used  when called this method to get  data  and observe in
     * {@link com.example.a5asec.ui.view.home.CartFragment}
     *
     * @return list of item service.
     */
    public LiveData<Resource<List<ServiceAndLaundryService>>> getListOrder()
    {
        return mListOrder;
    }

    /**
     * Get the all order of the item service.
     */
    private void fetchAllOrder()
    {
        mListOrder.postValue(Resource.loading(null));

        compositeDisposable.add(mCartRepositroy.getAllOrder().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

                .subscribeWith(
                        new ResourceSubscriber<List<ServiceAndLaundryService>>()
                        {

                            @Override
                            public void onNext(
                                    @NonNull List<ServiceAndLaundryService> serviceAndLaundryServices)
                            {
                                mListOrder.postValue(Resource.success(
                                        serviceAndLaundryServices));

                                if (!serviceAndLaundryServices.isEmpty()) {
                                    sumAllOrder(serviceAndLaundryServices);
                                }
                                Timber.tag(TAG).e(" subscribe Success ");

                            }

                            @Override
                            public void onError(@NonNull Throwable e)
                            {
                                mListOrder.postValue(
                                        Resource.error(e.toString(), null));
                                Timber.tag(TAG).e(e);

                            }

                            @Override
                            public void onComplete()
                            {
                                traceWithLog("onComplete = ");
                            }
                        }));


    }

    /**
     * Get the all item of service.
     *
     * @return a {@link Flowable} that will emit every time the serviceAndLaundryService has been
     * updated.
     */
    public @NonNull Flowable<List<ServiceAndLaundryService>> getAllOrder()
    {
        return mCartRepositroy.getAllOrder().map(listService -> listService);
    }

    /**
     * Called when sum List of ItemService and stored in sumOrder
     *
     * @param listService of item in Service and LaundryService table in database.
     */
    private void sumAllOrder(List<ServiceAndLaundryService> listService)
    {
        totalItemService.postValue(null);

        compositeDisposable.add(
                Observable.fromIterable(listService).map(data -> {
                    int total = data.getService().getCostItemService();
                    for (LaundryService laundryService : data.getLaundryService()) {
                        total += laundryService.getCost();
                    }

                    total *= data.getService().getCount();
                    traceWithLog("total = %s" + total);

                    return total; // Return values as observable

                }).scan(Integer::sum).subscribe(sum -> {
                    traceWithLog("total = %s" + sum);
                    totalItemService.postValue(sum);
                }));
    }

    public LiveData<Integer> getTotalAllOrder()
    {
        return totalItemService;
    }

    /**
     * @param id
     */
    public void addItemToUpdateService(int id)
    {
        traceWithLog("position of addItemToUpdateOrRemoveService = %s " + id);

        if (mListOrder.getValue() != null || mListOrder.isInitialized()) {
            traceWithLog("List Order of all Items = %s " + mListOrder.getValue()
                    .getMData());

            ServiceAndLaundryService firstItem = mListOrder.getValue()
                    .getMData().stream()
                    .filter(item -> item.getService().getIdItemService() == id).findFirst()
                    .orElse(null);


            if (firstItem != null) {
                traceWithLog("item selected to update = %s" + firstItem);
                mItemInCartSelected.setValue(firstItem);
                mCountObservable.set(firstItem.getService().getCount());

            } else {
                traceWithLog(
                        "position of not equal to addItemToUpdateOrRemoveService = %s " + id);

            }
        }
        getAllIdCheckItemLaundryService();
    }

    public void removeItemSelected()
    {
        getSelectedCartRemoveId().forEach(position -> {
            Timber.tag(TAG).e("Selected item remove position  = %s", position);
            getItemToRemove(position);
        });
    }

    private void getItemToRemove(int id)
    {
        traceWithLog("position of addItemToUpdateOrRemoveService = %s " + id);

        if (mListOrder.getValue() != null) {
            traceWithLog("List Order of all Items = %s " + mListOrder.getValue()
                    .getMData());

            ServiceAndLaundryService firstItem = mListOrder.getValue()
                    .getMData().stream()
                    .filter(item -> item.getService().getId() == id).findFirst()
                    .orElse(null);

            mListOrderDeleted.add(firstItem);
        }
    }

    public LiveData<ServiceAndLaundryService> getItemInCartSelected()
    {
        return mItemInCartSelected;
    }

    public LiveData<Set<Integer>> getItemServiceSelectedToUpdate()
    {
        return mItemServiceSelectedToUpdate;
    }


    public void getAllIdCheckItemLaundryService()
    {
        traceWithLog("Item Cart = " + mItemInCartSelected.getValue());
        List<Integer> allId = new ArrayList<>();

        mItemInCartSelected.getValue().getLaundryService()
                .forEach(laundryService -> {
                    if (laundryService.getFlag() == 0) {
                        allId.add(laundryService.getIdLaundryService());

                        selectedItemServicesId.add(
                                laundryService.getIdLaundryService());
                    }
                });
        traceWithLog("size of items selected in cartViewModel = " + allId.size());

        mItemServiceSelectedToUpdate.setValue((new HashSet<>(allId)));

    }

    public void getIdLaundryToUpdate(int idLaundryService)
    {
        traceWithLog("List of item laundry = " + mItemInCartSelected.getValue().getLaundryService()
                .toString());
        if (mItemInCartSelected.getValue() != null) {
            for (LaundryService service : mItemInCartSelected.getValue().getLaundryService()) {
                if (service.getIdLaundryService() == idLaundryService) {
                    updateLaundryService(service);
                    traceWithLog("LaundryService with id " + idLaundryService + "  found");

                    break;
                }
            }
        } else {
            traceWithLog("mItemInCartSelected is null");
        }
    }

    public LiveData<ServiceAndLaundryService> getItemToUpdateService()
    {
        return mItemInCartSelected;
    }


  /*   public LiveData<ArrayList<Integer>> getSumService()
        {
        return mSumAllOrders;
        } */


    public void updateService()
    {
        var itemservice = mItemInCartSelected.getValue().getService();
        itemservice.setCount(mCountObservable.get());
        compositeDisposable.add(updateService(itemservice).subscribeWith(new DisposableSingleObserver<>()
        {
            @Override
            public void onSuccess(@NonNull Integer aLong)
            {
                traceWithLog("updateService");

            }

            @Override
            public void onError(@NonNull Throwable e)
            {
                traceWithLog("error in updateService" + e);

            }
        }));

    }

    public void updateLaundryService(List<LaundryService> laundryServices)
    {
        mCartRepositroy.insertLaundryService(laundryServices)
                .subscribeOn(Schedulers.io()).doFinally(this::refreshCart)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> traceWithLog("updateLaundryService, Complete"),
                        throwable -> traceWithLog(
                                "updateLaundryService, error: %s",
                                throwable.getMessage()));
    }

    public void updateLaundryService(LaundryService laundryService)
    {
        laundryService.setFlag(laundryService.getFlag() == 0 ? 1 : 0);
        mCartRepositroy.updateLaundryService(laundryService)
                .subscribe(() -> {
                            traceWithLog("update laundry service");
                        },
                        throwable -> traceWithLog(
                                throwable.getMessage()));
    }

    private void getCountOrder()
    {
        compositeDisposable.add(countOrder().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new ResourceSubscriber<Integer>()
                {
                    @Override
                    public void onNext(Integer integer)
                    {
                        mCountService.postValue(Resource.success(integer));
                        traceWithLog("getCountOrder, count = %s" + integer);

                    }

                    @Override
                    public void onError(@NonNull Throwable e)
                    {
                        traceWithLog("getCountOrder, ERROR%s ", e.getMessage());
                        mCountService.postValue(Resource.empty(null));

                    }

                    @Override
                    public void onComplete()
                    {
                        traceWithLog("getCountOrder, complete%s ");

                    }


                })

        );


    }

    private @NonNull Flowable<Integer> countOrder()
    {
        return mCartRepositroy.getCountOrder().map(count -> {
            traceWithLog("count + " + count);
            return count;
        });
        //   return mDataSource.getCountOrder().fromCompletionStage(1)


    }

    public LiveData<Resource<Integer>> getCount()
    {
        getCountOrder();
        return mCountService;
    }


    public void removeOrUndoItemService()
    {
        traceWithLog(
                "size of item delete or undo = " + mListOrderDeleted.size());
        mListOrderDeleted.forEach(item -> {
            ItemService itemService = item.getService();
            itemService.setFlag(itemService.flag == 0 ? 1 : 0);
            removeItemOrUndoWithItemService(itemService);
        });

    }

    private void removeItemOrUndoWithItemService(ItemService item)
    {
        Timber.tag(TAG).e("removeOrUndoItemService, item service = %s", item);

        compositeDisposable.add(
                removeItemService(item).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(count -> {

                            Timber.tag(TAG).e("count of order = %s", count);
                        }));
    }

    public Single<Integer> removeItemService(ItemService item)
    {
        return mCartRepositroy.deleteService(item);
    }

    public void removeLaundryService(int pos)
    {
        compositeDisposable.add(mCartRepositroy.deleteLaundryService(pos)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterSuccess(integer -> {
                    refreshCart();
                }).subscribe(count -> {

                    Timber.tag(TAG).e("removeLaundryService = %s", count);
                }));
    }

    public void removeAllService()
    {
        compositeDisposable.add(
                mCartRepositroy.deleteAllService().subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doAfterSuccess(integer -> {
                            mCartRepositroy.restIdService();
                            mCartRepositroy.restIdLaundryService();
                        }).subscribe(count -> {

                            Log.e(TAG, "removeLaundryService = " + count);
                        }));
    }

    public void refreshCart()
    {
        //fetchAllOrder();
        // getCount();
        // getCountOrder();
    }

    /**
     * called when add new Item service to remove selected from {selectedLaundryServicesId,
     * selectionLaundryService}
     */
    public void clearSelected()
    {
        selectedItemServicesId.clear();
        selectionLaundryService.clear();
    }

    private void traceWithLog(String... message)
    {
        Timber.tag(TAG).e(Arrays.toString(message));

    }

    @Override
    protected void onCleared()
    {
        super.onCleared();
        clearSelected();
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.clear();
        }
    }
}
