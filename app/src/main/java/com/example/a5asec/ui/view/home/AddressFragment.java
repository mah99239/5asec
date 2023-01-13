package com.example.a5asec.ui.view.home;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.a5asec.R;
import com.example.a5asec.databinding.FragmentAddressBinding;
import com.example.a5asec.ui.view.viewmodel.AddressViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Objects;


public class AddressFragment extends Fragment
    {
    private static final String TAG = "AddressFragment";
    FragmentAddressBinding mBinding;
    private AddressViewModel mAddressViewModel;
    private GoogleMap mGoogleMap;
    private LatLng mLatLng;
    private Geocoder geocoder;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
        {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_address, container, false);
        showProgress();
        setupMap();
        mBinding.mapAddress.onCreate(savedInstanceState);
        mBinding.mapAddress.onResume();
        return mBinding.getRoot();
        }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
        {
        super.onViewCreated(view, savedInstanceState);
        var argCheck =AddressFragmentArgs.fromBundle(getArguments()).getArgLatLang();
        if(argCheck != null)
            {

            try
                {
                List<Address>  addresses = geocoder.getFromLocationName(argCheck, 1);
                Address address = addresses.get(0);
                Log.e(TAG, "latalng = " + address);
                mLatLng = new LatLng(address.getLatitude(), address.getLongitude());
                hideProgress();

                } catch (IOException e)
                {
                e.printStackTrace();
                }



            }
        else
            {
            setupUi();

            }
        }
    private void setupUi()
        {
        setupViewModel();
        setupInfoAddressObserver();
        }
    private void setupMap()
        {
         geocoder = new Geocoder(requireContext());

        mBinding.mapAddress.getMapAsync(this::getMapReady);

        }
    public void getMapReady(GoogleMap googleMap)
        {
        double latitudeFloat = 21.492500;
        double langFloat = 39.177570;
        var latLang = new LatLng(latitudeFloat, langFloat);
        this.mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        setMarkerInfoAddress(Objects.requireNonNullElse(mLatLng, latLang));


        }

    private void setupInfoAddressObserver()
        {

        mAddressViewModel.getAddress().observe(getViewLifecycleOwner(), addressResource ->
            {
            switch (addressResource.mStatus)
                {

                case SUCCESS -> {
                Log.e(TAG, "SUCCESS");
                var address = addressResource.getMData();
                hideProgress();

                var lat = Double.parseDouble(address.getArea().getLat());
                var lang = Double.parseDouble(address.getArea().getLang());
                mLatLng = new LatLng(lat, lang);

                }

                case LOADING -> {
                Log.e(TAG, "LOADING");

                }
                case ERROR, NULL -> {

                Log.e(TAG, "ERROR");
                Log.e(TAG, "ERROR" + addressResource.getMMessage());

                }

                }
            });
        }
    private void showProgress()
        {
        mBinding.cpiAddress.setVisibility(View.VISIBLE);
        mBinding.mapAddress.setVisibility(View.GONE);

        }
    private void hideProgress()
        {
        mBinding.mapAddress.setVisibility(View.VISIBLE);
        mBinding.cpiAddress.setVisibility(View.GONE);

        }
    private void setMarkerInfoAddress(LatLng latLng)
        {

        Log.e(TAG, " MarkerOptions, latlng = " + latLng);

        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .draggable(true);

        mGoogleMap.addMarker(markerOptions);

        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));


        }
    private void setupViewModel()
        {
        NavController navController = NavHostFragment.findNavController(this);
        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.nav_home_manage_address);
        mAddressViewModel = new ViewModelProvider(backStackEntry).get(AddressViewModel.class);
        }

    @Override
    public void onStart()
        {
        super.onStart();
        mBinding.mapAddress.onStart();
        }

    @Override
    public void onResume()
        {
        mBinding.mapAddress.onResume();
        super.onResume();
        }

    @Override
    public void onPause()
        {
        super.onPause();
        mBinding.mapAddress.onPause();
        }

    @Override
    public void onDestroy()
        {
        super.onDestroy();
        mBinding.mapAddress.onDestroy();
        mBinding = null;

        }

    @Override
    public void onLowMemory()
        {
        super.onLowMemory();
        mBinding.mapAddress.onLowMemory();
        mBinding = null;
        }

    }