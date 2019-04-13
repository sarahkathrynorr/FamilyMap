package com.example.familymapclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.MenuItem;

import java.lang.reflect.Array;
import java.util.ArrayList;

import src.Model.Person;

public class FilterActivity extends SingleFragmentActivity {
    private ArrayList<String> eventTypes;
    FilterListFragment filterListFragment;


    @Override
    protected Fragment createFragment() {
        if (!Model.instance().getEventTypes().contains("Mother")) {
            Model.instance().getEventTypes().add("Mother");
            Model.instance().getEventTypes().add("Father");
            Model.instance().getEventTypes().add("Male");
            Model.instance().getEventTypes().add("Female");
        }

        filterListFragment = new FilterListFragment();

        return filterListFragment;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Model.instance().setEventTypes(filterListFragment.getFilterEvents());
        Model.instance().setShowAllMarkers(true);

        if(item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP |
            Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(intent);
        }
        return true;
    }
}
