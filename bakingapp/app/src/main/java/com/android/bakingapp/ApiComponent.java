package com.android.bakingapp;

import com.android.bakingapp.data.Repository;
import com.android.bakingapp.main.MainFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Christos on 31/01/2018.
 */

@Singleton
@Component(modules = {AppModule.class, ApiModule.class})
public interface ApiComponent {

    Repository repository();

    void inject(MainFragment viewModel);

}
