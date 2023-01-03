package com.example.a5asec.ui.view.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ClickViewModel extends ViewModel {
    private final MutableLiveData<Boolean> selectedItem = new MutableLiveData<>();

    public void selectItem(boolean select) {

        selectedItem.setValue(select);
    }

    public LiveData<Boolean> getSelectedItem() {
        return selectedItem;
    }
}
