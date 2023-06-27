package com.example.a5asec;

import static org.mockito.Mockito.verify;

import com.example.a5asec.data.local.db.dao.CategoryDao;
import com.example.a5asec.data.local.db.dao.LocalCategoryDataSource;
import com.example.a5asec.data.model.api.Category;
import com.example.a5asec.data.model.db.CategoryEntity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class LocalCategoryDataSourceTest {
    private LocalCategoryDataSource localCategoryDataSource;

    @Mock
    private CategoryDao categoryDao;

    @Before
    public void setup() {
        localCategoryDataSource = new LocalCategoryDataSource(categoryDao);
    }

    @Test
    public void testInsertCategoriesWithItemsAndServices() {
        // Create some test data
        List<CategoryEntity> testCategories = new ArrayList<>();
        testCategories.add(new CategoryEntity(1, "Test Category", "Test Description", "Test Image URL", 1, new ArrayList<>()));
        testCategories.add(new CategoryEntity(2, "Another Category", "Another Description", "Another Image URL", 1, new ArrayList<>()));

        // Call the method under test
        localCategoryDataSource.insertCategoriesWithItemsAndServices(testCategories);
        // Verify that the correct data was inserted into the database
        verify(categoryDao).insertCategoriesWithItemsAndServices(testCategories);
    }
}