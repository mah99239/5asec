package com.example.a5asec.ui.view.home;

import static com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_LONG;

import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.a5asec.R;
import com.example.a5asec.data.model.api.City;
import com.example.a5asec.databinding.FragmentAddAddressBinding;
import com.example.a5asec.ui.view.viewmodel.AddressViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import lombok.val;


public class AddAddressFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener
        , GoogleMap.OnMarkerDragListener
    {

    private static final String TAG = "AddAddressFragment";

    private FragmentAddAddressBinding mBinding;
    private AddressViewModel mAddressViewModel;
    private GoogleMap mMap;
    private Geocoder geocoder;
    private List<City> citiesCovered;
    private City mSelectionCity;
    HighLightArrayAdapter mCityAdapter;
    HighLightArrayAdapter mAreaAdapter;
    private int mIdAreaSelection;
    private int argCheck;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
        {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_address, container, false);
        argCheck = AddAddressFragmentArgs.fromBundle(getArguments()).getNavHomeAddressArgCheck();
        return mBinding.getRoot();
        }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
        {
        super.onViewCreated(view, savedInstanceState);
        setupUI();
        setupMap();

        }

    private void setupUI()
        {

        setupViewModel();
        switch (argCheck)
            {

            case 2 -> setupUiInfo();  // show info address

            case 3 -> { // show edit address
            setupUiEdit();
            setupClickUpdateAddress();
            }
            default -> setupUiAddAddress(); // show add address
            }

        }

    /**
     * this show add address screen.
     */
    private void setupUiAddAddress()
        {
        setupCitiesObserver();
        setupCLickAddAddress();
        }

    /**
     * this show info address screen.
     */
    private void setupUiInfo()
        {
        disableEnabledFields();
        setupInfoAddressObserver();
        mBinding.btnAddAddress.setVisibility(View.GONE);
        }

    /**
     * this show edit address screen.
     */
    private void setupUiEdit()
        {
        setupInfoAddressObserver();
        setupCitiesObserver();
        }

    /**
     * this show's cities covered for client supported .
     */
    private void setupCityAutoCompleteVIew()
        {

        //TODO: show selection user in list .
         mCityAdapter = new HighLightArrayAdapter
                (getContext(), android.R.layout.simple_list_item_single_choice,
                        getCities());
        mBinding.actvAddAddressCity.setAdapter(mCityAdapter);
        mBinding.actvAddAddressCity.setOnItemClickListener((parent, view, position, id) ->
            {
            mBinding.actvAddAddressCity.setListSelection(position);
            var currentSelectionCity = citiesCovered.get(position);

            fetchAreaToAutoCompleteView(currentSelectionCity);
            mCityAdapter.setSelection(position);

            });

        }


    /**
     * show's areas supported in the city.
     */
    private void setupAreaAutoCompleteVIew()
        {
        mBinding.actvAddAddressArea.setText("");
        mAreaAdapter = new HighLightArrayAdapter
                (getContext(), android.R.layout.simple_list_item_single_choice,
                        getArea());

        mBinding.actvAddAddressArea.setEnabled(true);
        mBinding.actvAddAddressArea.setAdapter(mAreaAdapter);
        mBinding.actvAddAddressArea.setOnItemClickListener((parent, view, position, id) ->
            {

            var latitude = Double.parseDouble(mSelectionCity.getAreas().get(position).getLat());
            var lang = Double.parseDouble(mSelectionCity.getAreas().get(position).getLang());
            LatLng latlng = new LatLng(latitude, lang);
            mIdAreaSelection = mSelectionCity.getAreas().get(position).getId();
            mAreaAdapter.setSelection(position);

            setMarkerMap(latlng);
            });


        }

    /**
     * fetch areas to   in the city.
     * @param city pass when user selected city
     */
    private void fetchAreaToAutoCompleteView( City city)
        {
        var latitude = Double.parseDouble(city.getLat());
        var lang = Double.parseDouble(city.getLang());
        LatLng latlng = new LatLng(latitude, lang);

        setMarkerMap(latlng);
        setAreaInCity(city);
        setupAreaAutoCompleteVIew();
        }

    private void setAreaInCity(City areas)
        {
        mSelectionCity = areas;
        }

    private void setCity(String city)
        {
        citiesCovered.forEach(currentCity ->
            {
            if (currentCity.getNameEn().equals(city))
                {
                mSelectionCity = currentCity;
                return;
                }
            Log.e(TAG," setCity selectionCity" + mSelectionCity);


            });


        }

    private void setArea(String area)
        {
        mSelectionCity.getAreas().forEach(currentCity ->
            {
            if (currentCity.getNameEn().equals(area))
                {}
              //  mSelectionCity = currentCity;

            });
        }
    @NonNull
    private List<String> getArea()
        {
        val language = AppCompatDelegate.getApplicationLocales().toLanguageTags();

        var nameAreas = new ArrayList<String>();
        mSelectionCity.getAreas().forEach(areas -> nameAreas.add(areas.getName(language)));
        return nameAreas;

        }

    @NonNull
    private List<String> getCities()
        {
        val language = AppCompatDelegate.getApplicationLocales().toLanguageTags();

        var nameCity = new ArrayList<String>();
        citiesCovered.forEach(city -> nameCity.add(city.getName(language)));
        return nameCity;
        }

    private void setCitiesCovered(@NonNull List<City> cities)
        {
        citiesCovered = cities;

        }


    private boolean checkEmptyCityAndArea()
        {
        var city = Objects.requireNonNull(mBinding.actvAddAddressCity.getText()).toString();
        var area = Objects.requireNonNull(mBinding.actvAddAddressArea.getText()).toString();
        if (TextUtils.isEmpty(city) || TextUtils.isEmpty(area))
            {
            if (TextUtils.isEmpty(city))
                {
                mBinding.tilAddAddressCity.setError(getString(R.string.add_address_city_empty));

                }
            if (TextUtils.isEmpty(area))
                {

                mBinding.tilAddAddressArea.setError(getString(R.string.add_address_area_empty));

                }
            String message = getString(R.string.add_address_error_message);
            messageSnackbar(message);
            return false;
            }
        return true;
        }


    private void addAddress()
        {
        if (checkEmptyCityAndArea())
            {
            var latCity = mSelectionCity.getLat();
            var langCity = mSelectionCity.getLang();
            var idArea = mIdAreaSelection;

            var street = Objects.requireNonNull(mBinding.etAddAddressStreet.getText()).toString();
            var building = Objects.requireNonNull(mBinding.etAddAddressBuilding.getText()).toString();
            var floor = Objects.requireNonNull(mBinding.etAddAddressFloor.getText()).toString();
            var apartment = Objects.requireNonNull(mBinding.etAddAddressApartment.getText()).toString();
            var landmark = Objects.requireNonNull(mBinding.etAddAddressLandmark.getText()).toString();
            var notes = Objects.requireNonNull(mBinding.etAddAddressNotes.getText()).toString();


            var address = new com.example.a5asec.data.model.api.Address.CreateAddress(apartment, street, idArea,
                    building, floor, langCity, latCity, landmark, notes);
            if (argCheck == 1)
                {
                mAddressViewModel.setCreateAddress(address);
                setupAddAddressObserver();
                } else
                {
                mAddressViewModel.setUpdateAddress(address);
                setupAddAddressObserver();

                }

            } else
            {
            Log.e(TAG, "false");
            }
        }


    private void disableEnabledFields()
        {
        mBinding.actvAddAddressCity.setEnabled(false);
        mBinding.actvAddAddressArea.setEnabled(false);
        mBinding.etAddAddressStreet.setEnabled(false);
        mBinding.etAddAddressBuilding.setEnabled(false);
        mBinding.etAddAddressFloor.setEnabled(false);
        mBinding.etAddAddressApartment.setEnabled(false);
        mBinding.etAddAddressLandmark.setEnabled(false);
        mBinding.etAddAddressNotes.setEnabled(false);
        mBinding.cbAddAddressPrimary.setEnabled(false);
        }

    private void initDataToFields(@NonNull com.example.a5asec.data.model.api.Address address)
        {
        val language = AppCompatDelegate.getApplicationLocales().toLanguageTags();
        String notSpecfied = getString(R.string.add_address_not_specfiied);


        var city = address.getArea().getCity().getName(language);
        var area = address.getArea().getName(language);
        var street = address.getStreet();
        var building = address.getBuilding();
        var floor = address.getFloor();
        var apartment = address.getApartment();
        var landmark = address.getNearLandmark();
        var notes = address.getNote();
        var primary = address.isPrimary();

        if (StringUtils.isBlank(street)) street = notSpecfied;
        if (StringUtils.isBlank(building)) building = notSpecfied;
        if (StringUtils.isBlank(floor)) floor = notSpecfied;
        if (StringUtils.isBlank(apartment)) apartment = notSpecfied;
        if (StringUtils.isBlank(landmark)) landmark = notSpecfied;
        if (StringUtils.isBlank(notes)) notes = notSpecfied;

        mBinding.actvAddAddressCity.setText(city);
        mBinding.actvAddAddressArea.setText(area);
        mBinding.etAddAddressStreet.setText(street);
        mBinding.etAddAddressBuilding.setText(building);
        mBinding.etAddAddressFloor.setText(floor);
        mBinding.etAddAddressApartment.setText(apartment);
        mBinding.etAddAddressLandmark.setText(landmark);
        mBinding.etAddAddressNotes.setText(notes);
        mBinding.cbAddAddressPrimary.setChecked(primary);
        Log.e(TAG,"selectionCity" + mSelectionCity);
     //   fetchAreaToAutoCompleteView(mSelectionCity);

        }


    private void setupAddAddressObserver()
        {
        var primary = mBinding.cbAddAddressPrimary.isChecked();
        mAddressViewModel.getAddress().observe(getViewLifecycleOwner(), addressResource ->
            {
            switch (addressResource.mStatus)
                {

                case SUCCESS -> {
                Log.e(TAG, "SUCCESS");
                var id = addressResource.getMData().getId();
                if (primary)
                    {
                    mAddressViewModel.setPrimaryAddress(id);
                    }
                messageSnackbar("Sucess");
                actionToManageAddress();
                hideProgress();

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
                Log.e(TAG, "SUCCESS + "  + address);

                initDataToFields(address);
                setCity(address.getArea().getCity().getNameEn());

                var lat = Double.parseDouble(address.getArea().getCity().getLat());
                var lang = Double.parseDouble(address.getArea().getCity().getLang());
                var latLng = new LatLng(lat, lang);
                setMarkerInfoAddress(latLng);
                hideProgress();
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


    private void setupCitiesObserver()
        {
        mAddressViewModel.getCities().observe(getViewLifecycleOwner(), citiesResource ->
            {
            switch (citiesResource.mStatus)
                {

                case SUCCESS -> {
                Log.e(TAG, "SUCCESS");
                Log.e(TAG, "SUCCESS = " + citiesResource.getMData());
                setCitiesCovered(citiesResource.getMData());
                setupCityAutoCompleteVIew();
                }

                case LOADING -> {
                Log.e(TAG, "LOADING");


                }
                case ERROR, NULL -> {

                Log.e(TAG, "ERROR");

                }

                }
            });
        }

    private void setupViewModel()
        {
        NavController navController = NavHostFragment.findNavController(this);
        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.nav_home_manage_address);
        mAddressViewModel = new ViewModelProvider(backStackEntry).get(AddressViewModel.class);

        }



    private void setupCLickAddAddress()
        {
        mBinding.btnAddAddress.setOnClickListener(v ->
            {
            addAddress();

            });
        }

    private void setupClickUpdateAddress()
        {
        mBinding.btnAddAddress.setOnClickListener(v ->
            {
            addAddress();

            });
        }

    private void actionToManageAddress()
        {
        var action
                = AddAddressFragmentDirections.actionNewAddressFragmentToNavHomeManageAddress();
        Navigation.findNavController(requireView()).navigate(action);
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

    private void ShowProgress()
        {
        mBinding.cpiAddAddress.setVisibility(View.VISIBLE);
       // mBinding.fAddAddressMap.setVisibility(View.GONE);
        mBinding.clAddAddressContainer.setVisibility(View.GONE);
        }

    private void hideProgress()
        {
    //    mBinding.fAddAddressMap.setVisibility(View.VISIBLE);
        mBinding.clAddAddressContainer.setVisibility(View.VISIBLE);
        mBinding.cpiAddAddress.setVisibility(View.GONE);

        }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap)
        {
        double latitudeFloat = 21.492500;
        double langFloat = 39.177570;
        var latLang = new LatLng(latitudeFloat, langFloat);
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


        if (argCheck == 1 || argCheck == 3)
            {

            mMap.setOnMapClickListener(this);
            mMap.setOnMarkerDragListener(this);
            setMarkerMap(latLang);
            /* try
                {

                List<Address> addresses = geocoder.getFromLocation(latitudeFloat, langFloat, 1);


                if (addresses.size() > 0)
                    {

                    Address address = addresses.get(0);
                    LatLng jaddah = new LatLng(address.getLatitude(), address.getLongitude());
                    MarkerOptions markerOptions = new MarkerOptions()
                            .position(jaddah)
                            .draggable(true)

                            .alpha(0.7f)
                            .zIndex(1.0f)
                            .title(address.getLocality());
                    mMap.addMarker(markerOptions);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(jaddah, 12));

                    }
                } catch (IOException e)
                {
                e.printStackTrace();
                } */


            }
        }

    private void setMarkerInfoAddress(LatLng latLng)
        {

        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng);

        mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));

        }

    private void setMarkerMap(@NonNull LatLng latLng)
        {

        try
            {
            mMap.clear();
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses.size() > 0)
                {
                Address address = addresses.get(0);
                String streetAddress = address.getAddressLine(0);
                Log.e(TAG, "streeAddress = " + streetAddress);
                mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(streetAddress)
                        .draggable(true)
                );

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));

                }
            } catch (IOException e)
            {
            e.printStackTrace();
            }

        }

    @Override
    public void onMapClick(@NonNull LatLng latLng)
        {
        setMarkerMap(latLng);
     /*    try
            {
            mMap.clear();
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses.size() > 0)
                {
                Address address = addresses.get(0);
                String streetAddress = address.getAddressLine(0);
                mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(streetAddress)
                        .draggable(true)
                );


                }
            } catch (IOException e)
            {
            e.printStackTrace();
            } */
        }

    @Override
    public void onMarkerDragStart(@NonNull Marker marker)
        {
        Log.d(TAG, "onMarkerDragStart: ");
        }

    @Override
    public void onMarkerDrag(@NonNull Marker marker)
        {
        Log.d(TAG, "onMarkerDrag: ");
        }

    @Override
    public void onMarkerDragEnd(@NonNull Marker marker)
        {
        Log.d(TAG, "onMarkerDragEnd: ");
        LatLng latLng = marker.getPosition();
        setMarkerMap(latLng);
     /*    try
            {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses.size() > 0)
                {
                Address address = addresses.get(0);
                String streetAddress = address.getAddressLine(0);
                marker.setTitle(streetAddress);
                }
            } catch (IOException e)
            {
            e.printStackTrace();
            } */
        }

    private void messageSnackbar(@NonNull String message)
        {

        Snackbar.make(mBinding.clAddAddressRoot, message, LENGTH_LONG)
                .setAnchorView(mBinding.btnAddAddress).show();

        }


    class HighLightArrayAdapter extends ArrayAdapter<CharSequence> {

    private int mSelectedIndex = -1;


    public void setSelection(int position) {
    mSelectedIndex =  position;
    notifyDataSetChanged();
    }

    public HighLightArrayAdapter(Context context, int resource, List objects) {
    super(context, resource, objects);
    }


    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
    View itemView =  super.getDropDownView(position, convertView, parent);

    if (position == mSelectedIndex) {
    itemView.setBackgroundColor(Color.RED);
    } else {
    itemView.setBackgroundColor(Color.TRANSPARENT);
    }

    return itemView;
    }
    }
    }