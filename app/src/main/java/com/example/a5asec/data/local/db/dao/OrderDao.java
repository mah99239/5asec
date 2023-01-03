package com.example.a5asec.data.local.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.a5asec.data.model.db.ItemService;
import com.example.a5asec.data.model.db.LaundryService;
import com.example.a5asec.data.model.db.ServiceAndLaundryService;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface OrderDao
    {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Single<Long> insertService(ItemService service);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertLaundryService(List<LaundryService> laundryServices);

    @Update
    Single<Integer> updateService(ItemService... order);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    Completable updateLaundryService(List<LaundryService> order);


    @Update
    Single<Integer> deleteService(ItemService... names);

    @Query("DELETE FROM table_laundry_service WHERE id_service = :id ")
    Single<Integer> deleteLaundryService(int id);

    @Query("delete  FROM table_item_service ")
    Single<Integer> deleteAllService();


    @Query("SELECT * FROM table_item_service left outer JOIN table_laundry_service ON " +
            "table_item_service.id = table_laundry_service.id_service where flag = 0 ")
    Flowable<List<ServiceAndLaundryService>> getOrder();

    @Transaction
    @Query("SELECT * FROM table_item_service where flag = 0 ")
    Observable<List<ServiceAndLaundryService>> getAllOrder();


    @Transaction
    @Query("SELECT sum(count) FROM table_item_service where flag = 0")
    Single<Integer> getCountOrder();


/*     @Transaction
    @Query(" select cost_item_service from table_item_service where id = 21 union all select SUM(cost) " +
            "from table_laundry_service where id_service = 2") */
  //  Single<Integer> geSumCostAllService();

/*     @Transaction
    @Query("SELECT  (sum(cost_item_service)  + sum(table_laundry_service.cost))   * count FROM table_item_service  JOIN" +
            " table_laundry_service  " +
            "where table_item_service.id = :id AND (table_laundry_service.id_service = :id )") */
 //   Single<Integer> geSumCostOfService(int id);



    @Query("delete from sqlite_sequence where name='table_item_service'")
    Completable restIdService();

    @Query("delete from sqlite_sequence where name='table_laundry_service'")
    Completable restIdLaundryService();



/*     @Query("  SELECT ((SELECT  sum(cost_item_service) FROM table_item_service where id = :id )" +
            " +   (SELECT  sum(cost) FROM table_laundry_service where id_service = :id)) ")
    Single<Integer> test1(int id); */


    }
