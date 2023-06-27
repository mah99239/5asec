package com.example.a5asec;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.a5asec.data.local.db.dao.LocalOrderDataSource;
import com.example.a5asec.data.model.db.ServiceAndLaundryService;
import com.example.a5asec.ui.view.viewmodel.CartViewModel;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

/**
 * Unit test for {@link  com.example.a5asec.ui.view.viewmodel.CartViewModel}
 */

public class CartViewModelTest
    {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private LocalOrderDataSource mDataSource;

    @Captor
    private ArgumentCaptor<ServiceAndLaundryService> mServiceAndLaundryServiceArgumentCaptor;

    private CartViewModel mViewModel;

    @Before
    public void setUp()
        {
        MockitoAnnotations.openMocks(this);
        mViewModel = new CartViewModel(mDataSource);
        }

    @Test
    public void getAllOrder()
        {
        //Given that the dataSource return an empty list of service.
        when(mDataSource.getAllOrder()).thenReturn(Flowable.empty());

        //when getting the current order.
        mViewModel.getAllOrder()
                .test()
                .assertNoValues();


        }

    @Test
    public void updateUserName_updatesNameInDataSource()
        {
        // Given that a completable is returned when inserting a user
        when(mDataSource.insertOrUpdateService(any())).thenReturn(Single.never());

        // When updating the user name
/*     mViewModel.insertLaundryService()
            .test()
            .assertComplete(); */

        // The user name is updated in the data source
        verify(mDataSource).insertOrUpdateService(mServiceAndLaundryServiceArgumentCaptor.capture().getService());
        assertThat(mServiceAndLaundryServiceArgumentCaptor.getValue().getService(), Matchers.is("new user name"));
        }

    }
