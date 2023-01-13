package com.example.a5asec.ui.view.home;

import static com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_LONG;

import android.content.Context;
import android.graphics.Color;
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
import androidx.databinding.ObservableField;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.a5asec.R;
import com.example.a5asec.data.model.api.Address;
import com.example.a5asec.data.model.api.City;
import com.example.a5asec.databinding.FragmentAddAddressBinding;
import com.example.a5asec.ui.view.viewmodel.AddressViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class AddAddressFragment extends Fragment implements GoogleMap.OnMapClickListener, GoogleMap.OnMarkerDragListener
    {

    private static final String TAG = "AddAddressFragment";
    private static final int TAG_ADD = 1;
    private static final int TAG_INFO = 2;
    private static final int TAG_EDIT = 3;
    private static final String LANGUAGE_TAGS = AppCompatDelegate.getApplicationLocales().toLanguageTags();

    HighLightArrayAdapter mCityAdapter;
    HighLightArrayAdapter mAreaAdapter;
    private GoogleMap googleMap;
    private FragmentAddAddressBinding mBinding;
    private AddressViewModel mAddressViewModel;
    private Marker mMarker;
    private List<City> citiesCovered;
    private int mIdAreaSelection;
    private int argCheck;
    private LatLng latLng;
    private ObservableField<String> currentLatCity;
    private ObservableField<String> currentLangCity;
    private ObservableField<Address> mAddressObservableField;

    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_address, container, false);

        mBinding.mapAddAddress.onCreate(savedInstanceState);
        mBinding.mapAddAddress.onResume();
        setupMap();
        return mBinding.getRoot();

        }

    private void setupMap()
        {
        //  geocoder = new Geocoder(requireContext());
        mBinding.mapAddAddress.getMapAsync(this::getMapReady);

        }

    private void getMapReady(GoogleMap googleMap)
        {
        double latitudeDefault = 21.492500;
        double langDefault = 39.177570;
        var latLang = new LatLng(latitudeDefault, langDefault);
        this.googleMap = googleMap;
        this.googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


        setMarkerMap(Objects.requireNonNullElse(latLng, latLang));

        if (argCheck == 1 || argCheck == 3)
            {
            this.googleMap.setOnMapClickListener(this);
            this.googleMap.setOnMarkerDragListener(this);

            }
        }

    @Override
    public void onMapClick(@NonNull LatLng latLng)
        {
        clearMarker();
        setMarkerMap(latLng);
        }

    private void setMarkerMap(@NonNull LatLng latLng)
        {
        //  List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
        // if (!addresses.isEmpty())

        //Log.e(TAG, "latalng = " + latLng);
        // Address address = addresses.get(0);
        //  String streetAddress = address.getAddressLine(0);
        setUpMapIfNeeded();
        clearMarker();
        Log.e(TAG, " MarkerOptions, latlng = " + latLng);
        Log.e(TAG, " setMarkerMap, map = " + googleMap);
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .draggable(true);

        mMarker = googleMap.addMarker(markerOptions);

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));

        }

    private void clearMarker()
        {
        if (mMarker != null)
            {
            mMarker.remove();
            }
        }

    @Override
    public void onMarkerDragStart(@NonNull Marker marker)
        {
        Log.d(TAG, "onMarkerDragStart: ");
        }

    @Override
    public void onMarkerDrag(@NonNull Marker marker)
        {
        mMarker = marker;

        Log.d(TAG, "onMarkerDrag: ");
        }

    @Override
    public void onMarkerDragEnd(@NonNull Marker marker)
        {
        mMarker = marker;

        Log.d(TAG, "onMarkerDragEnd: ");


        }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
        {
        super.onViewCreated(view, savedInstanceState);
        argCheck = AddAddressFragmentArgs.fromBundle(getArguments()).getNavHomeAddressArgCheck();
        setupUI();

        }

    private void setupUI()
        {

        setupViewModel();

        switch (argCheck)
            {
            case TAG_ADD -> setupUiAddAddress();
            case TAG_INFO -> setupUiInfo();
            case TAG_EDIT -> setupUiEdit();
            }

        }

    private void setupViewModel()
        {
        NavController navController = NavHostFragment.findNavController(this);
        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.nav_home_manage_address);
        mAddressViewModel = new ViewModelProvider(backStackEntry).get(AddressViewModel.class);

        }


    //---------------------------------------setup Screen add address-------------------------------

    /**
     * this show add address screen.
     */
    private void setupUiAddAddress()
        {
        setupCitiesObserver();
        setupCLickAddAddress();
        }

    private void setupCitiesObserver()
        {
        mAddressViewModel.getCities().observe(getViewLifecycleOwner(), citiesResource ->
            {
            switch (citiesResource.mStatus)
                {

                case SUCCESS -> {
                Log.e(TAG, "SUCCESS");
                citiesCovered = citiesResource.getMData();
                setupCityAutoCompleteVIew();


                }

                case LOADING -> Log.e(TAG, "LOADING");
                case ERROR, NULL -> Log.e(TAG, "ERROR");

                }
            });
        }

    /**
     * this show's cities covered for client supported .
     */
    private void setupCityAutoCompleteVIew()
        {
        currentLatCity = new ObservableField<>();
        currentLangCity = new ObservableField<>();

        mCityAdapter = new HighLightArrayAdapter(getContext(), android.R.layout.simple_list_item_single_choice, getCities());
        mBinding.actvAddAddressCity.setAdapter(mCityAdapter);
        mBinding.actvAddAddressCity.setOnItemClickListener((parent, view, position, id) ->
            {
            mBinding.actvAddAddressCity.setListSelection(position);
            var currentSelectionCity = citiesCovered.get(position);
            var currentLat = currentSelectionCity.getLat();
            var currentLang = currentSelectionCity.getLang();
            currentLatCity.set(currentLat);
            currentLangCity.set(currentLang);
            fetchAreaToAutoCompleteView(currentSelectionCity);
            mCityAdapter.setSelection(position);

            });

        }

    /**
     * fetch areas to   in the city.
     *
     * @param city pass when user selected city
     */
    private void fetchAreaToAutoCompleteView(@NonNull City city)
        {
        var latitude = Double.parseDouble(city.getLat());
        var lang = Double.parseDouble(city.getLang());
        latLng = new LatLng(latitude, lang);

        //setMarkerMap(latLng);
        //  setAreasWithCity(city);
        setupAreaAutoCompleteVIew(city);

        }

    /**
     * show's areas supported in the city.
     */
    private void setupAreaAutoCompleteVIew(City city)
        {
        mBinding.actvAddAddressArea.setText("");
        mAreaAdapter = new HighLightArrayAdapter(getContext(),
                android.R.layout.simple_list_item_single_choice, getAreasToFetchInAreaAutoCompleteTextView(city));

        mBinding.actvAddAddressArea.setEnabled(true);
        mBinding.actvAddAddressArea.setAdapter(mAreaAdapter);
        mBinding.actvAddAddressArea.setOnItemClickListener((parent, view, position, id) ->
            {

            var latitude = Double.parseDouble(city.getAreas().get(position).getLat());
            var lang = Double.parseDouble(city.getAreas().get(position).getLang());
            LatLng latlng = new LatLng(latitude, lang);
            mIdAreaSelection = city.getAreas().get(position).getId();
            mAreaAdapter.setSelection(position);

            setMarkerMap(latlng);
            });

        }

    private void setUpMapIfNeeded()
        {
        if (googleMap == null)
            {
            setupMap();
            }


        }




    private void setupCLickAddAddress()
        {
        mBinding.btnAddAddress.setOnClickListener(v -> addAddress());
        }

    private void addAddress()
        {

        if (checkEmptyCityAndArea())
            {
            var latCity = currentLatCity.get();
            var langCity = currentLangCity.get();

            var idArea = mIdAreaSelection;

            var street = Objects.requireNonNull(mBinding.etAddAddressStreet.getText()).toString();
            var building = Objects.requireNonNull(mBinding.etAddAddressBuilding.getText()).toString();
            var floor = Objects.requireNonNull(mBinding.etAddAddressFloor.getText()).toString();
            var apartment = Objects.requireNonNull(mBinding.etAddAddressApartment.getText()).toString();
            var landmark = Objects.requireNonNull(mBinding.etAddAddressLandmark.getText()).toString();
            var notes = Objects.requireNonNull(mBinding.etAddAddressNotes.getText()).toString();


            Log.e(TAG, "TAG_ADD ok = " + TAG_ADD);
            var address = new com.example.a5asec.data.model.api.Address.CreateAddress(apartment,
                    street, idArea, building, floor, langCity, latCity, landmark, notes);
            mAddressViewModel.setCreateAddress(address);
            setupAddAddressObserver();

            } else
            {
            Log.e(TAG, "error:  add address");
            }
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

    private void setupAddAddressObserver()
        {
        var primary = mBinding.cbAddAddressPrimary.isChecked();
        mAddressViewModel.getAddress().observe(getViewLifecycleOwner(), addressResource ->
            {
            switch (addressResource.mStatus)
                {

                case SUCCESS -> {
                var id = addressResource.getMData().getId();
                if (primary)
                    {
                    mAddressViewModel.setPrimaryAddress(id);
                    }
                messageSnackbar("Success");
                actionToManageAddressScreen();

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

    private void actionToManageAddressScreen()
        {
        var action = AddAddressFragmentDirections.actionNewAddressFragmentToNavHomeManageAddress();
        Navigation.findNavController(requireView()).navigate(action);
        }

    //---------------------------------------setup Screen Info--------------------------------------


    /**
     * this show info address screen.
     */
    private void setupUiInfo()
        {
        disableEnabledFields();

        setupInfoAddressObserver();
        mBinding.btnAddAddress.setVisibility(View.GONE);
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

    private void setupInfoAddressObserver()
        {

        mAddressViewModel.getAddress().observe(getViewLifecycleOwner(), addressResource ->
            {
            switch (addressResource.mStatus)
                {

                case SUCCESS -> {
                var address = addressResource.getMData();

                Log.e(TAG, "SUCCESS + " + address);


                initDataToFields(address);

                var lat = Double.parseDouble(address.getArea().getLat());
                var lang = Double.parseDouble(address.getArea().getLang());
                latLng = new LatLng(lat, lang);

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

    private void initDataToFields(@NonNull com.example.a5asec.data.model.api.Address address)
        {
        //  String notSpecfied = getString(R.string.add_address_not_specfiied);


        var city = address.getArea().getCity().getName(LANGUAGE_TAGS);
        var area = address.getArea().getName(LANGUAGE_TAGS);
        var street = address.getStreet();
        var building = address.getBuilding();
        var floor = address.getFloor();
        var apartment = address.getApartment();
        var landmark = address.getNearLandmark();
        var notes = address.getNote();
        var primary = address.isPrimary();

/*         if (StringUtils.isBlank(street))         mBinding.etAddAddressStreet.setHint(notSpecfied);

        if (StringUtils.isBlank(building)) building = notSpecfied;
        if (StringUtils.isBlank(floor)) floor = notSpecfied;
        if (StringUtils.isBlank(apartment)) apartment = notSpecfied;
        if (StringUtils.isBlank(landmark)) landmark = notSpecfied;
        if (StringUtils.isBlank(notes)) notes = notSpecfied; */


        mBinding.actvAddAddressCity.setText(city);
        mBinding.actvAddAddressArea.setText(area);
        mBinding.etAddAddressStreet.setText(street);
        mBinding.etAddAddressBuilding.setText(building);
        mBinding.etAddAddressFloor.setText(floor);
        mBinding.etAddAddressApartment.setText(apartment);
        mBinding.etAddAddressLandmark.setText(landmark);
        mBinding.etAddAddressNotes.setText(notes);
        mBinding.cbAddAddressPrimary.setChecked(primary);
        //  Log.e(TAG, "selectionCity" + mSelectionCity);

        }


    //---------------------------------------setup Screen edit address------------------------------

    /**
     * this show edit address screen.
     */
    private void setupUiEdit()
        {

        setupEditAddressObserver();
        setupClickUpdateAddress();

        }


    private void setupEditAddressObserver()
        {

        mAddressViewModel.getAddress().observe(getViewLifecycleOwner(), addressResource ->
            {
            switch (addressResource.mStatus)
                {

                case SUCCESS -> {

                var address = addressResource.getMData();
                mAddressObservableField = new ObservableField<>(address);
                Log.e(TAG, "setupEditAddressObserver, SUCCESS =  " + address);

                var lat = Double.parseDouble(address.getArea().getLat());
                var lang = Double.parseDouble(address.getArea().getLang());
                latLng = new LatLng(lat, lang);

                fetchCitiesInUpdateAddressObserver();


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


    private void fetchCitiesInUpdateAddressObserver()
        {
        mAddressViewModel.getCities().observe(getViewLifecycleOwner(), citiesResource ->
            {
            switch (citiesResource.mStatus)
                {

                case SUCCESS -> {
                Log.e(TAG, "SUCCESS");
                citiesCovered = citiesResource.getMData();
                setupCityInUpdateAddressAutoCompleteView();


                var nameCity = mAddressObservableField.get().getArea().getCity().getName(LANGUAGE_TAGS);
                setCityInUpdateAddress(nameCity);

                initDataToFields(Objects.requireNonNull(mAddressObservableField.get()));


                }

                case LOADING -> Log.e(TAG, "LOADING");
                case ERROR, NULL -> Log.e(TAG, "ERROR");

                }
            });
        }

    /**
     * this show's cities covered for client supported .
     */
    private void setupCityInUpdateAddressAutoCompleteView()
        {
        currentLatCity = new ObservableField<>();
        currentLangCity = new ObservableField<>();

        mCityAdapter = new HighLightArrayAdapter(getContext(), android.R.layout.simple_list_item_single_choice, getCities());
        mBinding.actvAddAddressCity.setAdapter(mCityAdapter);
        mBinding.actvAddAddressCity.setOnItemClickListener((parent, view, position, id) ->
            {
            mBinding.actvAddAddressCity.setListSelection(position);
            var currentSelectionCity = citiesCovered.get(position);
            var currentLat = currentSelectionCity.getLat();
            var currentLang = currentSelectionCity.getLang();
            currentLatCity.set(currentLat);
            currentLangCity.set(currentLang);
            fetchAreaInUpdateAddressToAutoCompleteView(currentSelectionCity);
            mCityAdapter.setSelection(position);

            });
        }

    /**
     * fetch areas to   in the city.
     *
     * @param city pass when user selected city
     */
    private void fetchAreaInUpdateAddressToAutoCompleteView(@NonNull City city)
        {
        var latitude = Double.parseDouble(city.getLat());
        var lang = Double.parseDouble(city.getLang());
        latLng = new LatLng(latitude, lang);

       // setMarkerMap(latLng);

        setupAreaInUpdateAddressAutoCompleteVIew(city);


        }

    private void setupAreaInUpdateAddressAutoCompleteVIew(City city)
        {
        mAreaAdapter = new HighLightArrayAdapter(getContext(),
                android.R.layout.simple_list_item_single_choice, getAreasToFetchInAreaAutoCompleteTextView(city));

        mBinding.actvAddAddressArea.setEnabled(true);
        mBinding.actvAddAddressArea.setAdapter(mAreaAdapter);
        mBinding.actvAddAddressArea.setOnItemClickListener((parent, view, position, id) ->
            {
            var latitude = Double.parseDouble(city.getAreas().get(position).getLat());
            var lang = Double.parseDouble(city.getAreas().get(position).getLang());
            LatLng latlng = new LatLng(latitude, lang);
            mIdAreaSelection = city.getAreas().get(position).getId();
            mAreaAdapter.setSelection(position);

            setMarkerMap(latlng);
            });

        }

    private void getIdAreaWithNameArea(@NonNull  String area, @NonNull City city)
        {
        city.getAreas().forEach(currentCity ->
            {
            if (currentCity.getNameEn().equals(area))
                {
                mIdAreaSelection = currentCity.getId();
                Log.e(TAG, "getIdAreaWithNameArea = " + mIdAreaSelection);

                }

            });
        }
    private void setCityInUpdateAddress(String city)
        {
        Log.e(TAG, "city cover = " + citiesCovered);
        if (StringUtils.isNotBlank(city))
            {
            for (City currentCity : citiesCovered)
                {
                if (currentCity.getName(LANGUAGE_TAGS).equals(city))
                    {
                    //  mSelectionCity = currentCity;
                    fetchAreaInUpdateAddressToAutoCompleteView(currentCity);
                    var area = mAddressObservableField.get().getArea().getNameEn();
                    Log.e(TAG, "area = " + area);
                    getIdAreaWithNameArea(area, currentCity);
                    break;
                    }
                //  Log.e(TAG, " setCity selectionCity" + mSelectionCity);

                }
            }


        }


    @NonNull
    private List<String> getAreasToFetchInAreaAutoCompleteTextView(City city)
        {

        var nameAreas = new ArrayList<String>();
        city.getAreas().forEach(areas -> nameAreas.add(areas.getName(LANGUAGE_TAGS)));
        return nameAreas;

        }


    @NonNull
    private List<String> getCities()
        {

        var nameCity = new ArrayList<String>();
        citiesCovered.forEach(city -> nameCity.add(city.getName(LANGUAGE_TAGS)));
        return nameCity;
        }


    private void setupClickUpdateAddress()
        {
        mBinding.btnAddAddress.setOnClickListener(v -> updateAddress());
        }

    private void updateAddress()
        {
        if (checkEmptyCityAndArea())
            {
            var latCity = currentLatCity.get();
            var langCity = currentLangCity.get();

            var idArea = mIdAreaSelection;

            var street = Objects.requireNonNull(mBinding.etAddAddressStreet.getText()).toString();
            var building = Objects.requireNonNull(mBinding.etAddAddressBuilding.getText()).toString();
            var floor = Objects.requireNonNull(mBinding.etAddAddressFloor.getText()).toString();
            var apartment = Objects.requireNonNull(mBinding.etAddAddressApartment.getText()).toString();
            var landmark = Objects.requireNonNull(mBinding.etAddAddressLandmark.getText()).toString();
            var notes = Objects.requireNonNull(mBinding.etAddAddressNotes.getText()).toString();


            Log.e(TAG, "TAG_EDIT ok = " + TAG_EDIT);
            Log.e(TAG, "IDArea = " + idArea);

            var updateAddress = new Address.UpdateAddress(apartment,
                    street, idArea, building, floor, langCity, latCity, landmark, notes);
            mAddressViewModel.setUpdateAddress(updateAddress);
            setupUpdateAddressObserver();
            } else
            {
            Log.e(TAG, "false");

            }
        }


    private void setupUpdateAddressObserver()
        {
        var checkedIsPrimary = mBinding.cbAddAddressPrimary.isChecked();
        var oldIsPrimary = mAddressObservableField.get().isPrimary();
        mAddressViewModel.getUpdateAddress().observe(getViewLifecycleOwner(), addressResource ->
            {
            switch (addressResource.mStatus)
                {

                case SUCCESS -> {
                var id = mAddressObservableField.get().getId();
                if (checkedIsPrimary ^ oldIsPrimary)
                    {
                    setupUpdatePrimaryAddress(id);
                    Log.e(TAG, "setPrimaryAddress , success , update primary");
                    }else
                    {
                    Log.e(TAG, "setPrimaryAddress , no update primary");
                    messageSnackbar("Success update address");
                    actionToManageAddressScreen();
                    }



                }

                case LOADING -> {
                Log.e(TAG, "setupUpdateAddressObserver, LOADING");


                }
                case ERROR -> {
                messageSnackbar("ERROR  update address");

                Log.e(TAG, "setupUpdateAddressObserver ,ERROR" + addressResource.getMMessage());

                }

                }
            });
        }
    private void setupUpdatePrimaryAddress(int id)
        {

        mAddressViewModel.setPrimaryAddress(id).observe(getViewLifecycleOwner(), addressResource ->
            {
            switch (addressResource.mStatus)
                {

                case SUCCESS -> {

                    Log.e(TAG, "setupUpdatePrimaryAddress , success , update primary");
                    messageSnackbar("Success update primary address");
                    actionToManageAddressScreen();





                }

                case LOADING -> {
                Log.e(TAG, "setupUpdatePrimaryAddress, LOADING");


                }
                case ERROR -> {
                messageSnackbar("ERROR  update primary address");

                Log.e(TAG, "setupUpdatePrimaryAddress ,ERROR" + addressResource.getMMessage());

                }

                }
            });
        }

    private void showProgress()
        {
        mBinding.cpiAddAddress.setVisibility(View.VISIBLE);
        mBinding.mapAddAddress.setVisibility(View.GONE);
        mBinding.clAddAddressContainer.setVisibility(View.GONE);
        }

    private void hideProgress()
        {
        mBinding.mapAddAddress.setVisibility(View.VISIBLE);
        mBinding.clAddAddressContainer.setVisibility(View.VISIBLE);
        mBinding.cpiAddAddress.setVisibility(View.GONE);

        }

    private void messageSnackbar(@NonNull String message)
        {

        Snackbar.make(mBinding.clAddAddressRoot, message, LENGTH_LONG).setAnchorView(mBinding.btnAddAddress).show();

        }

    @Override
    public void onStart()
        {
        super.onStart();
        mBinding.mapAddAddress.onStart();
        }

    @Override
    public void onResume()
        {
        mBinding.mapAddAddress.onResume();
        super.onResume();
        }

    @Override
    public void onPause()
        {
        super.onPause();
        mBinding.mapAddAddress.onPause();
        }

    @Override
    public void onDestroy()
        {
        super.onDestroy();
        mBinding.mapAddAddress.onDestroy();
        mBinding = null;

        }

    @Override
    public void onLowMemory()
        {
        super.onLowMemory();
        mBinding.mapAddAddress.onLowMemory();
        mBinding = null;
        }


    static class HighLightArrayAdapter extends ArrayAdapter<CharSequence>
        {

        private int mSelectedIndex = -1;


        public HighLightArrayAdapter(Context context, int resource, List objects)
            {
            super(context, resource, objects);
            }

        public void setSelection(int position)
            {
            mSelectedIndex = position;
            notifyDataSetChanged();
            }

        @Override
        public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent)
            {
            View itemView = super.getDropDownView(position, convertView, parent);

            if (position == mSelectedIndex)
                {
                itemView.setBackgroundColor(Color.RED);
                } else
                {
                itemView.setBackgroundColor(Color.TRANSPARENT);
                }

            return itemView;
            }
        }
    }