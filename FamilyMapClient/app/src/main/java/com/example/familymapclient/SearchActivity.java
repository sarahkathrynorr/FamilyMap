package com.example.familymapclient;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;
import java.util.List;

import src.Model.Event;
import src.Model.Person;

public class SearchActivity extends AppCompatActivity {

    AllPersons allPersons;
    AllEvents allEvents;
    ArrayList<Object> searchResults = new ArrayList<>();
    SearchView searchView;


    String authToken;
    String serverPort;
    String serverHost;


    private Drawable maleIcon;
    private Drawable femaleIcon;
    private Drawable locationIcon;


    //@Override
//    protected Fragment createFragment() {

        @Override //Unless previously overridden
        protected void onCreate (Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_search);

            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                authToken = (String) bundle.getCharSequence("AuthToken");
                serverHost = (String) bundle.getCharSequence("ServerHost");
                serverPort = (String) bundle.getCharSequence("ServerPort");
            }

            allPersons = (AllPersons) getIntent().getSerializableExtra("allPersons");
            allEvents = (AllEvents) getIntent().getSerializableExtra("allEvents");

            searchView = (SearchView) findViewById(R.id.search_view); // initiate a search view

            maleIcon = new IconDrawable(this, FontAwesomeIcons.fa_male).colorRes(R.color.orange).sizeDp(20);
            femaleIcon = new IconDrawable(this, FontAwesomeIcons.fa_female).colorRes(R.color.purple).sizeDp(20);
            locationIcon = new IconDrawable(this, FontAwesomeIcons.fa_map_marker).colorRes(R.color.colorAccent).sizeDp(20);


            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    if (query != null) {

                        updateSearchFilter(query, allEvents, allPersons); //questionable
                        //searchView.setQuery("", false);
                        //searchView.clearFocus();

                    }
                    else {
                        searchView.setQuery("", false);
                        searchView.clearFocus();
                        updateSearchFilter(query, allEvents, allPersons); //questionable

                    }
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                if (newText != null) {

                    updateSearchFilter(newText, allEvents, allPersons);
                }
                    return false;
                }
            });

            searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        //searchView.collapseActionView();
                        updateSearchFilter("", allEvents, allPersons);
                    }
                }
            });

            //CharSequence query = searchView.getQuery();
            //updateSearchFilter((String) query);


            //This comment is unnecessary, but will be left here until removed. #youneverknowwhodiditunlessyouremembered
            //return setFragment();
            //setFragment();
        }
    //}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP |
                    Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return true;
    }

    private void setFragment() {
        //set the view
        RecyclerView mResultRecyclerView = findViewById(R.id.search_activity)
                .findViewById(R.id.search_recycler_list);
        mResultRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        SearchAdapter searchAdapter = new SearchAdapter(searchResults);
        mResultRecyclerView.setAdapter(searchAdapter);
    }

    public void searchEvents(String query, AllEvents allEvents, ArrayList<Object> searchResults) {
        if (allEvents != null) {
            for (Event event : allEvents.getAllEvents()) {
                if (event.getCountry().toLowerCase().contains(query.toLowerCase())) {
                    if (!searchResults.contains(event)) {
                        searchResults.add(event);
                    }
                }
                if (event.getCity().toLowerCase().contains(query.toLowerCase())) {
                    if (!searchResults.contains(event)) {
                        searchResults.add(event);
                    }
                }
            }
        }
    }

    public void searchEventTypes(String query, AllEvents allEvents, ArrayList<Object> searchResults) {
        for (Event event : allEvents.getAllEvents()) {
            if (event.getEventType().contains(query)) {
                searchResults.add(event);
            }
        }
    }

    private void searchPeople(String query, AllPersons allPersons, ArrayList<Object> searchResults) {
        if (allPersons != null) {
            for (Person person : allPersons.getAllPersonsArray()) {
                if (person.getLastName().toLowerCase().contains(query.toLowerCase())) {
                    if (!searchResults.contains(person)) {
                        searchResults.add(person);
                    }
                }
                if (person.getFirstName().toLowerCase().contains(query.toLowerCase())) {
                    if (!searchResults.contains(person)) {
                        searchResults.add(person);
                    }
                }
            }
        }
    }

    public ArrayList<Object> updateSearchFilter(String query, AllEvents allEvents, AllPersons allPersons) {
        searchResults = new ArrayList<>();
        searchEvents(query, allEvents, searchResults);
        searchEventTypes(query, allEvents, searchResults);
        searchPeople(query, allPersons, searchResults);

        //setFragment();
        return searchResults;
    }

    private class SearchHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

            private final TextView mTitleTextView;
        private final TextView mDescriptionTextView;
        private Object mResult;
        //private final ImageView icon;

        SearchHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_search, parent, false));

            mTitleTextView = itemView.findViewById(R.id.topItemSearch);
            mDescriptionTextView = itemView.findViewById(R.id.bottomItemSearch);
        }

        void bind(final Object mResult) {
            this.mResult = mResult;
            if (mResult.getClass().equals(Event.class)) {
                //set icon to map icon
                Event mEvent = (Event) mResult;
                mTitleTextView.setCompoundDrawables(locationIcon, null, null, null);
                for (Event event : allEvents.getAllEvents()) {
                    if (event.equals(mEvent)) {

                        String description = event.getEventType() + ": " + event.getCity() + ", " + event.getCountry() +
                                " (" + event.getYear() + ")";
                        mTitleTextView.setText(description);

                        for (Person person : allPersons.getAllPersonsArray()) {
                            if (person.getPersonId().equals(event.getPersonId())) {
                                String name = person.getFirstName() + " " + person.getLastName();
                                mDescriptionTextView.setText(name);
                                break;
                            }
                        }
                        break;
                    }
                }
            } else {
                for (Person person : allPersons.getAllPersonsArray()) {
                    Person mPerson = (Person) mResult;
                    if (person.equals(mPerson)) {
                        if (person.getGender().toUpperCase().equals("M")) {
                            //set icon
                            mTitleTextView.setCompoundDrawables(maleIcon, null, null, null);

                        } else {
                            mTitleTextView.setCompoundDrawables(femaleIcon, null, null, null);
                            //set icon
                        }

                        String name = person.getFirstName() + " " + person.getLastName();
                        mTitleTextView.setText(name);
                    }
                }
            }
            itemView.setClickable(true);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("test");
                    if (mResult.getClass().equals(Event.class)) {

                        Event event = (Event) mResult;
                        Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                        Model.instance().setGivenEventId(event.getEventID());

                        startActivity(intent);
                    } else {
                        Person person = (Person) mResult;

                        Intent intent = new Intent(SearchActivity.this, PersonActivity.class);

                        Model.instance().setPersonId(person.getPersonId());
                        startActivity(intent);
                    }
                }
                });
        }

        @Override
        public void onClick(View v) {
            if (mResult.getClass().equals(Event.class)) {

                Event event = (Event) mResult;
                Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                Model.instance().setGivenEventId(event.getEventID());

                startActivity(intent);
            } else {
                Person person = (Person) mResult;

                Intent intent = new Intent(SearchActivity.this, PersonActivity.class);
                Model.instance().setPersonId(person.getPersonId());
                startActivity(intent);
            }
        }
    }

    private class SearchAdapter extends RecyclerView.Adapter<SearchHolder> {

        private final List<Object> mResults;

        SearchAdapter(List<Object> results) {
            mResults = results;
        }

        @Override
        public SearchHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(SearchActivity.this);
            return new SearchHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(SearchHolder holder, int position) {
            Object currentObj = mResults.get(position);
            holder.bind(currentObj);
        }

        @Override
        public int getItemCount() {
            return mResults.size();
        }
    }

}
