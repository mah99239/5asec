package com.example.a5asec.ui.view.viewmodel;

import androidx.lifecycle.MutableLiveData;

import com.example.a5asec.data.model.api.Category;
import com.example.a5asec.data.repository.CategoryRepository;
import com.example.a5asec.utility.Resource;

import java.util.List;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class ItemCategoryViewModel extends CategoryViewModel
    {
    private static final String TAG = "ItemCategoryViewModel";
    public MutableLiveData<Resource<List<Category.ItemsEntity>>> category = new MutableLiveData<>();
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private CategoryRepository mCategoryRepository;

    public MutableLiveData<Resource<List<Category.ItemsEntity>>> category(int position)
        {
        return null;// super.category.getValue().;
        }

    public ItemCategoryViewModel()
        {
        fetchCategory();

        }

    public ItemCategoryViewModel(CategoryRepository categoryRepository)
        {
        mCategoryRepository = categoryRepository;
        fetchCategory();
        }



    private void fetchCategory()
        {




        }

    @Override
    protected void onCleared()
        {
        super.onCleared();
        compositeDisposable.clear();
        compositeDisposable.dispose();
        }


    }