package com.example.a5asec.ui.view.home;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.a5asec.R;
import com.example.a5asec.data.model.api.Setting;
import com.example.a5asec.databinding.FragmentContactBinding;
import com.example.a5asec.ui.view.viewmodel.SettingViewModel;
import com.example.a5asec.utility.NetworkConnection;
import com.example.a5asec.utility.Status;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 *  Fragment for the Contact screen, show data contact form rest api.
 */
public class ContactFragment extends Fragment implements OnMapReadyCallback
    {
    private static final String TAG = "ContactFragment";


    private FragmentContactBinding mBinding;
    private ShimmerFrameLayout mShimmerFrameLayout;
    private ConstraintLayout mContainers;
    private SettingViewModel mSettingViewModel;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
        {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_contact, container, false);

        return mBinding.getRoot();
        }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
        {
        super.onViewCreated(view, savedInstanceState);

        setupUI();



        }


    private void setupUI()
        {
        setupAnimation();
        setupViewModel();
        setupObserver();
        }

    private void setupAnimation()
        {
        var shimmer = new Shimmer.AlphaHighlightBuilder()
                .setDuration(2000L) // how long the shimmering animation takes to do one full sweep

                .setRepeatMode(ValueAnimator.REVERSE)
                .setAutoStart(true)
                .build();

        mContainers = mBinding.clContactContainer;
        mContainers.setVisibility(View.INVISIBLE);
        mShimmerFrameLayout = mBinding.shContact;
        mShimmerFrameLayout.setShimmer(shimmer);


        }

    @Override
    public void onResume()
        {
        super.onResume();
        mShimmerFrameLayout.startShimmer();

        }

    @Override
    public void onPause()
        {
        mShimmerFrameLayout.stopShimmer();
        super.onPause();

        }

//    @Override
//    public void onMapReady(@NonNull GoogleMap googleMap)
//        {
//
//        double lat = Double.parseDouble(mSettingViewModel.getSetting().getValue().getMData().getDefaultLat());
//        double lang = Double.parseDouble(mSettingViewModel.getSetting().getValue().getMData().getDefaultLang());
//
//        // Add a marker in Sydney and move the camera
//     *//*     LatLng sydney = new LatLng(-223, -2112);
//        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
//        googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//        googleMap.moveCamera(CameraUpdateFactory.zoomTo(15)); *//*
//
//        googleMap.addMarker(new MarkerOptions()
//                .position(new LatLng(37.4233438, -122.0728817))
//                .title("LinkedIn")
//                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
//
//        googleMap.addMarker(new MarkerOptions()
//                .position(new LatLng(37.4629101,-122.2449094))
//                .title("Facebook")
//                .snippet("Facebook HQ: Menlo Park"));
//
//        googleMap.addMarker(new MarkerOptions()
//                .position(new LatLng(37.3092293, -122.1136845))
//                .title("Apple"));
//
//        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.4233438, -122.0728817), 10));
//
//        }



    private void setupObserver()
        {
        mSettingViewModel.getSetting().observe(getViewLifecycleOwner(), settingResource ->
            {
            switch (settingResource.mStatus)
                {

                case SUCCESS -> {

                Log.e(TAG, "SUCCESS");
                Log.e(TAG, "suc:" + settingResource.getMData());
                stopAnimation();

                var setting = settingResource.getMData();
                renderData(setting);
                setupMap();

                }

                case LOADING -> {
                Log.e(TAG, "LOADING");
                startAnimation();

                }
                case ERROR -> {
                startAnimation();
                Log.e(TAG, "ERROR");

                }
                }
            });


        }

    private void startAnimation()
        {
        mContainers.setVisibility(View.GONE);
        mShimmerFrameLayout.startShimmer();
        mShimmerFrameLayout.setVisibility(View.VISIBLE);
        }

    private void stopAnimation()
        {
        new Handler(Looper.getMainLooper()).postDelayed(() ->
            {
            mContainers.setVisibility(View.VISIBLE);
            mShimmerFrameLayout.stopShimmer();
            mShimmerFrameLayout.setVisibility(View.GONE);
            }, 2000L);

        }

    private void renderData(Setting setting)
        {
        String contactPhone = setting.getContactPhone();
        String contactEmail = setting.getContactEmail();
        String facebookLink = setting.getFacebookLink();
        String twitterLink = setting.getTwitterLink();
        String instagramLink = setting.getInstagramLink();

        mBinding.tvContactPhone.setText(contactPhone);
        mBinding.tvContactEmail.setText(contactEmail);

        mBinding.tvContactPhone.setOnClickListener(v ->
            {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse(contactPhone));
            startActivity(intent);

            });
        mBinding.tvContactEmail.setOnClickListener(v ->
            {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(contactEmail));
            startActivity(intent);

            });
        mBinding.ivContactFace.setOnClickListener(v ->
                openBrowser(facebookLink));

        mBinding.ivContactTwit.setOnClickListener(v ->
                openBrowser(twitterLink));

        mBinding.ivContactInst.setOnClickListener(v ->
                openBrowser(instagramLink));
        }

    public void openBrowser(String url)
        {

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
        }

    private void setupViewModel()
        {
        mSettingViewModel = new ViewModelProvider(requireActivity())
                .get(SettingViewModel.class);
        }

    /**
     * check if data null and internet connection called reload data from viewmodel
     */
    private void checkConnections()
        {
        NetworkConnection networkConnection = new NetworkConnection(getContext());
        networkConnection.observe(this, isConnected ->
            {


            if (isConnected) // if internet not connect, Show message try connection internet
                {
                if(mSettingViewModel.getSetting().getValue().getMStatus().equals(Status.ERROR))
                    {
                    mSettingViewModel.reload();

                    }
                }
            });
        }

    private void setupMap()
        {

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment)getChildFragmentManager()
                .findFragmentById(R.id.f_contact_map);

            mapFragment.getMapAsync(this);

        }
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap)
        {
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
       // googleMap.getUiSettings().setZoomGesturesEnabled(true);
        /*   googleMap.getUiSettings().setZoomGesturesEnabled(true);
        googleMap.getUiSettings().setRotateGesturesEnabled(true); */
        try{
        String latitudeString =mSettingViewModel.getSetting().getValue().getMData().getDefaultLat();
        String langString =mSettingViewModel.getSetting().getValue().getMData().getDefaultLang();
        double latitudeFloat = Double.parseDouble(latitudeString);
        double langFloat = Double.parseDouble(langString);



        Log.e(TAG, "lat = " + latitudeFloat);
        Log.e(TAG, "lang = " + langFloat);
        LatLng sydney = new LatLng(latitudeFloat, langFloat);



        googleMap.addMarker(new MarkerOptions()
                .position(sydney)
                .title("your location")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(latitudeFloat, langFloat), 15));

        }
        catch (NumberFormatException ex){
        ex.printStackTrace();
        }


        }
    }