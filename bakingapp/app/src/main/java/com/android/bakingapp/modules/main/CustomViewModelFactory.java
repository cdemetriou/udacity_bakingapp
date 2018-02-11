package com.android.bakingapp.modules.main;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.android.bakingapp.data.Repository;

/**
 * Created by Christos on 01/02/2018.
 */


public class CustomViewModelFactory implements ViewModelProvider.Factory {


    Repository repository;

    public CustomViewModelFactory(Repository repository) {
        this.repository = repository;

    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainViewModel.class)) {
            return (T) new MainViewModel(repository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
