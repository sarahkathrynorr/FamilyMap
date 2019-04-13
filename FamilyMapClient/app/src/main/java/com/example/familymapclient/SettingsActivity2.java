package com.example.familymapclient;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import src.Model.Event;
import src.Model.Person;
import src.ServerProxy;

public class SettingsActivity2 extends AppCompatActivity {
    private String serverHost;
    private String serverPort;
    private String authToken;
    private AllEvents filteredAllEvents;
    private AllEvents allEvents;
    private AllPersons allPersons;

    private String lifeStoryLinesColor;
    private String familyTreeLinesColor;
    private String spouseLinesColor;
    private String mapType;


    private Spinner lifeStoryDropdown;
    private Spinner spouseDropdown;
    private Spinner familyDropdown;
    private Spinner mapTypeDropdown;

    private ArrayList<String> updatedEventTypes;

    private boolean showLifeStoryLines;
    private boolean showFamilyTreeLines;
    private boolean showSpouseLines;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        serverHost = Model.instance().getServerHost();
        serverPort = Model.instance().getServerPort();
        authToken = Model.instance().getAuthToken();

        allEvents = Model.instance().getAllEvents();
        filteredAllEvents = Model.instance().getFilteredAllEvents();
        allPersons = Model.instance().getAllPersons();

        setContentView(R.layout.activity_settings2);

        String[] items = new String[]{"Green", "Yellow", "Red"};
        String[] mapTypes = new String[] {"Normal", "Hybrid", "Satellite", "Terrain"};

        //life story drop down
        lifeStoryDropdown = findViewById(R.id.lifeStoryLinesSpinner);
        ArrayAdapter<String> adapterLifeStory = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        lifeStoryDropdown.setAdapter(adapterLifeStory);

        //spouse drop down
        spouseDropdown = findViewById(R.id.SpouseLinesSpinner);
        ArrayAdapter<String> adapterSpouse = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        spouseDropdown.setAdapter(adapterSpouse);

        //family drop down
        familyDropdown = findViewById(R.id.FamilyTreeLinesSpinner);
        ArrayAdapter<String> adapterFamilyTree = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        familyDropdown.setAdapter(adapterFamilyTree);

        //map type drop down
        mapTypeDropdown = findViewById(R.id.MapTypeSpinner);
        ArrayAdapter<String> adapterMapType = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, mapTypes);
        mapTypeDropdown.setAdapter(adapterMapType);


        //RE-SYNC
        View resync = findViewById(R.id.ReSync);
        resync.setClickable(true);
        resync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean successSyncing = sync();
                if (successSyncing) {
                    Toast.makeText(SettingsActivity2.this,
                            "Successfully Synced",
                            Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(SettingsActivity2.this, MainActivity.class);

                    Model.instance().setShowAllMarkers(true);
                    Model.instance().setGivenEventId(null);

                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP |
                            Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
               }
            }
        });


        //LOGOUT
        View logout = findViewById(R.id.logout);
        logout.setClickable(true);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Model.instance().setAuthToken(null);

                Intent intent = new Intent(SettingsActivity2.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        spouseLinesColor = spouseDropdown.getSelectedItem().toString();
        Switch spouseSwitch = (Switch) findViewById(R.id.spouseSwitch);
        showSpouseLines = spouseSwitch.isChecked();

        lifeStoryLinesColor = lifeStoryDropdown.getSelectedItem().toString();
        Switch lifeStorySwitch = (Switch) findViewById(R.id.lifeStorySwitch);
        showLifeStoryLines = lifeStorySwitch.isChecked();

        familyTreeLinesColor = familyDropdown.getSelectedItem().toString();
        Switch familySwitch = (Switch) findViewById(R.id.familySwitch);
        showFamilyTreeLines = familySwitch.isChecked();

        mapType = mapTypeDropdown.getSelectedItem().toString();

        if(item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);

            Model.instance().setShowAllMarkers(true);
            Model.instance().setGivenEventId(null);

            Model.instance().setFamilyTreeLinesColor(familyTreeLinesColor);
            Model.instance().setShowFamilyTreeLines(showFamilyTreeLines);

            Model.instance().setSpouseLinesColor(spouseLinesColor);
            Model.instance().setShowLifeStoryLines(showSpouseLines);

            Model.instance().setLifeStoryLinesColor(lifeStoryLinesColor);
            Model.instance().setShowLifeStoryLines(showLifeStoryLines);

            Model.instance().setFilteredAllEvents(filteredAllEvents);

            Model.instance().setMapType(mapType);

            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP |
                    Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(intent);
        }
        return true;
    }

    private boolean sync() {
        GetAllPersons getAllPersons = new GetAllPersons();
        try {
            allPersons.setAllPersons(getAllPersons.execute(authToken).get());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        GetAllEvents getAllEvents = new GetAllEvents();
        try {
            filteredAllEvents.setAllEvents(getAllEvents.execute(authToken).get());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return true;
    }

    private class GetAllPersons extends AsyncTask<String, ArrayList<Person>, ArrayList<Person>> {
        @Override
        protected ArrayList<Person> doInBackground(String... strings) {
            ServerProxy serverProxy = new ServerProxy();
            serverProxy.setServerPort(serverPort);
            serverProxy.setServerHost(serverHost);

            allPersons.setAllPersons(serverProxy.getPersons(authToken).getPersonsArray());
            return allPersons.getAllPersonsArray();
        }

        @Override
        protected void onPostExecute(ArrayList<Person> persons) {
            allPersons.setAllPersons(persons);
        }
    }


    private class GetAllEvents extends AsyncTask<String, ArrayList<Event>, ArrayList<Event>> {
        @Override
        protected ArrayList<Event> doInBackground(String... strings) {
            ServerProxy serverProxy = new ServerProxy();
            serverProxy.setServerPort(serverPort);
            serverProxy.setServerHost(serverHost);

            return serverProxy.getEvents(strings[0]).getEventsArray();

        }

        @Override
        protected void onPostExecute(ArrayList<Event> events) {
            filteredAllEvents.setAllEvents(events);
        }
    }
}
