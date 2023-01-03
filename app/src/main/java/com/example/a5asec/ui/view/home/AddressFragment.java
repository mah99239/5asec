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
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;


public class AddressFragment extends Fragment implements OnMapReadyCallback
    {
    private static final String TAG = "AddressFragment";
    FragmentAddressBinding mBinding;
    private AddressViewModel mAddressViewModel;
    private GoogleMap mMap;
    private Geocoder geocoder;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
        {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_address, container, false);

        return mBinding.getRoot();
        }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
        {
        super.onViewCreated(view, savedInstanceState);
        setupUi();
        }
    private void setupUi()
        {
        setupMap();
        setupViewModel();
        setupInfoAddressObserver();
        }
    private void setupMap()
        {

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.f_add_address_map);

        if (mapFragment != null)
            {
            mapFragment.getMapAsync(this);
            }
        geocoder = new Geocoder(requireContext());

        }

    private void setupInfoAddressObserver()
        {
        ShowProgress();

        mAddressViewModel.getAddress().observe(getViewLifecycleOwner(), addressResource ->
            {
            switch (addressResource.mStatus)
                {

                case SUCCESS -> {
                Log.e(TAG, "SUCCESS");
                var address = addressResource.getMData();
                hideProgress();

                var lat = Double.parseDouble(address.getArea().getCity().getLat());
                var lang = Double.parseDouble(address.getArea().getCity().getLang());
                var latLng = new LatLng(lat, lang);
                setMarkerInfoAddress(latLng);
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
    private void ShowProgress()
        {
        mBinding.cpiAddress.setVisibility(View.VISIBLE);
      //  mBinding.fAddressMap.setVisibility(View.GONE);

        }
    private void hideProgress()
        {
     //   mBinding.fAddressMap.setVisibility(View.VISIBLE);
        mBinding.cpiAddress.setVisibility(View.GONE);


        }
    private void setMarkerInfoAddress(LatLng latLng)
        {

        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng);

        mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));

        }
    private void setupViewModel()
        {
        NavController navController = NavHostFragment.findNavController(this);
        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.nav_home_manage_address);
        mAddressViewModel = new ViewModelProvider(backStackEntry).get(AddressViewModel.class);
        }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap)
        {
        double latitudeFloat = 21.492500;
        double langFloat = 39.177570;
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        try
            {

            List<Address> addresses = geocoder.getFromLocation(latitudeFloat, langFloat, 1);


            if (addresses.size() > 0)
                {

                Address address = addresses.get(0);
                LatLng jaddah = new LatLng(address.getLatitude(), address.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(jaddah)

                        .alpha(0.7f)
                        .zIndex(1.0f)
                        .title(address.getLocality());
                mMap.addMarker(markerOptions);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(jaddah, 12));


                }
            } catch (IOException e)
            {
            e.printStackTrace();
            }


        }
    }