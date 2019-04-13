package com.example.familymapclient;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import src.Model.Event;
import src.Model.Person;
import src.ServerProxy;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String authToken;
    private String serverPort;
    private String serverHost;
    private AllEvents allEvents;
    private AllEvents filteredAllEvents = new AllEvents();
    private AllPersons allPersons;
    private AllPersons filteredAllPersons = new AllPersons();
    private Person currentPerson;

    private Person user;

    private boolean showSpouseLines = true;
    private String spouseLinesColor = "RED";

    private boolean showLifeStoryLines = true;
    private String lifeStoryLinesColor = "YELLOW";

    private boolean showFamilyTreeLines = true;
    private String familyTreeLinesColor = "GREEN";

    private Drawable maleIcon;
    private Drawable femaleIcon;
    private boolean showAllMarkers = true;
    private String givenEventId;
    private ArrayList<String> eventTypes = new ArrayList<>();
    private ArrayList<String> updatedEventTypes;
    private HashMap<String, Float> eventTypeColors = new HashMap<>();

    private ArrayList<Marker> markers = new ArrayList<>();

    private String mapType = "normal";

    double fatherLineWidth = 50;
    double motherLineWidth = 50;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP) //yeah IDK about this
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        Iconify.with(new FontAwesomeModule());

        //************* TOP BAR
        Drawable searchButtonIcon = new IconDrawable(getApplicationContext(), FontAwesomeIcons.fa_search).colorRes(R.color.colorAccent).sizeDp(40);
        Drawable filterButtonIcon = new IconDrawable(getApplicationContext(), FontAwesomeIcons.fa_filter).colorRes(R.color.colorAccent).sizeDp(40);
        Drawable settingsButtonIcon = new IconDrawable(this, FontAwesomeIcons.fa_gear).colorRes(R.color.colorAccent).sizeDp(40);

        ((ImageView) findViewById(R.id.settingsButton)).setImageDrawable(settingsButtonIcon);
        ((ImageView) findViewById(R.id.searchButton)).setImageDrawable(searchButtonIcon);
        ((ImageView) findViewById(R.id.filterButton)).setImageDrawable(filterButtonIcon);

        Toolbar myToolbar = (android.widget.Toolbar) findViewById(R.id.my_toolbar);
        setActionBar(myToolbar);

        ImageButton searchButton = (ImageButton) findViewById(R.id.searchButton);
        ImageButton filterButton = (ImageButton) findViewById(R.id.filterButton);
        ImageButton settingsButton = (ImageButton) findViewById(R.id.settingsButton);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            authToken = (String) bundle.getCharSequence("AuthToken");
            serverHost = (String) bundle.getCharSequence("ServerHost");
            serverPort = (String) bundle.getCharSequence("ServerPort");
            givenEventId = (String) bundle.getCharSequence("givenEventId");

            if (bundle.getCharSequence("lifeStoryLinesColor") != null) {
                lifeStoryLinesColor = (String) Objects.requireNonNull(bundle.getCharSequence("lifeStoryLinesColor")).toString().toUpperCase();
                showLifeStoryLines = bundle.getBoolean("showLifeStoryLines");
            }
            if (bundle.getCharSequence("spouseLinesColor") != null) {
                spouseLinesColor = (String) Objects.requireNonNull(bundle.getCharSequence("spouseLinesColor")).toString().toUpperCase();

                showSpouseLines = bundle.getBoolean("showSpouseLines");
            }
            if (bundle.getCharSequence("familyTreeLinesColor") != null) {
                familyTreeLinesColor = (String) Objects.requireNonNull(bundle.getCharSequence("familyTreeLinesColor")).toString().toUpperCase();
                showFamilyTreeLines = bundle.getBoolean("showFamilyTreeLines");
            }

            if (bundle.getCharSequence("mapType") != null) {
                mapType = Objects.requireNonNull(bundle.getCharSequence("mapType")).toString();
            }

            showAllMarkers = bundle.getBoolean("showAllMarkers");
            updatedEventTypes = bundle.getStringArrayList("eventTypes");
            mapColorsToEvents();
        }
        user = (Person) getIntent().getSerializableExtra("user");
        allPersons = (AllPersons) getIntent().getSerializableExtra("allPersons");
        filteredAllEvents = (AllEvents) getIntent().getSerializableExtra("filteredAllEvents");
        filteredAllPersons = (AllPersons) getIntent().getSerializableExtra("filteredAllPersons");

        if (getIntent().getSerializableExtra("allEvents") != null) {
            allEvents = (AllEvents) getIntent().getSerializableExtra("allEvents");
        } else {
            allEvents = new AllEvents();
            allEvents.setAllEvents(new ArrayList<Event>());
        }

        for (Event event : allEvents.getAllEvents()) {
            if (eventTypes.isEmpty() || !eventTypes.contains(event.getEventType())) {
                eventTypes.add(event.getEventType());
            }
        }
        mapColorsToEvents();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MapsActivity.this,
                        "Search button clicked",
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MapsActivity.this, SearchActivity.class);

                Bundle bundie = new Bundle();
                bundie.putCharSequence("AuthToken", authToken);
                bundie.putCharSequence("ServerPort", serverPort);
                bundie.putCharSequence("ServerHost", serverHost);
                intent.putExtras(bundie);


                intent.putExtra("allEvents", filteredAllEvents);
                intent.putExtra("allPersons", allPersons);

                startActivity(intent);
            }
        });

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(MapsActivity.this,
                        "Filter button clicked",
                        Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MapsActivity.this, FilterActivity.class);

                Bundle bundie = new Bundle();

                intent.putExtra("user", user);
                intent.putExtra("allEvents", allEvents);
                intent.putExtra("allPersons", allPersons);

                authToken = (String) bundie.getCharSequence("AuthToken");
                serverHost = (String) bundie.getCharSequence("ServerHost");
                serverPort = (String) bundie.getCharSequence("ServerPort");

                bundie.putStringArrayList("eventTypes", eventTypes);
                intent.putExtras(bundie);

                startActivity(intent);
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MapsActivity.this,
                        "Settings button clicked",
                        Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MapsActivity.this, SettingsActivity2.class);

                Bundle bundie = new Bundle();

                bundie.putCharSequence("AuthToken", authToken);
                bundie.putCharSequence("ServerPort", serverPort);
                bundie.putStringArrayList("eventTypes", updatedEventTypes);
                bundie.putCharSequence("ServerHost", serverHost);
                intent.putExtra("user", user);
                intent.putExtra("allEvents", (Serializable) allEvents);
                intent.putExtra("filteredAllEvents", (Serializable) filteredAllEvents);
                intent.putExtra("allPersons", (Serializable) allPersons);
                intent.putExtras(bundie);

                startActivity(intent);
            }
        });

        //***************** BOTTOM BAR
        maleIcon = new IconDrawable(this, FontAwesomeIcons.fa_male).colorRes(R.color.orange).sizeDp(40);
        femaleIcon = new IconDrawable(this, FontAwesomeIcons.fa_female).colorRes(R.color.purple).sizeDp(40);
        Drawable defaultIcon = new IconDrawable(this, FontAwesomeIcons.fa_question).colorRes(R.color.yellow).sizeDp(40);


        ((ImageView) findViewById(R.id.bottom_icon)).setImageDrawable((defaultIcon));

        String defaultText = "Click any marker for event details";
        ((TextView) findViewById(R.id.person_name_bottom)).setText(defaultText);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        switch (mapType.toLowerCase()) {
            case "terrain":
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
            case "hybrid":
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
            case "satellite":
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            default:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
        }

        assert allEvents != null;
        if (allEvents.getAllEvents().isEmpty()) {
            allEvents = new AllEvents();
            AddEventDetails addEventDetails = new AddEventDetails();
            try {
                allEvents.setAllEvents(addEventDetails.execute(authToken).get());

                if (filteredAllEvents == null || filteredAllEvents.getAllEvents().isEmpty()) {
                    filteredAllEvents = allEvents;
                    filteredAllEvents.setAllEvents(allEvents.getAllEvents());
                }
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for (Event event : allEvents.getAllEvents()) {
                if (eventTypes.isEmpty() || !eventTypes.contains(event.getEventType())) {
                    eventTypes.add(event.getEventType());
                }
            }
            mapColorsToEvents();
        }
        if (allPersons == null || allPersons.getAllPersonsArray() == null) {
            allPersons = new AllPersons();
            GetAllPersons getAllPersons = new GetAllPersons();
            try {
                allPersons.setAllPersons(getAllPersons.execute(authToken).get());

                if (filteredAllPersons == null || filteredAllPersons.getAllPersonsArray() == null) {
                    filteredAllPersons = allPersons;
                    filteredAllPersons.setAllPersons(allPersons.getAllPersonsArray());
                }
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for (Event event : allEvents.getAllEvents()) {
            if (eventTypes.isEmpty() || !eventTypes.contains(event.getEventType())) {
                eventTypes.add(event.getEventType());
            }
        }
        mapColorsToEvents();

        AddMarkers();

        mMap.setOnMarkerClickListener( new GoogleMap.OnMarkerClickListener() {
               @Override
               public boolean onMarkerClick(Marker marker) {
                   //collect all the data from that marker...
                   Event eventForMarker = (Event) marker.getTag();

                   for (Person person : allPersons.getAllPersonsArray()) {
                       assert eventForMarker != null;
                       if (person.getPersonId().equals(eventForMarker.getPersonId())) {
                           currentPerson = person;
                           break;
                       }
                   }

                   String personNameText = currentPerson.getFirstName() + " " + currentPerson.getLastName();

                   if (currentPerson.getGender().equals("F")) {
                       ((ImageView) findViewById(R.id.bottom_icon)).setImageDrawable((femaleIcon));
                   } else if (currentPerson.getGender().equals("M")){
                       ((ImageView) findViewById(R.id.bottom_icon)).setImageDrawable((maleIcon));
                   }

                   assert eventForMarker != null;
                   String eventDetailsText = eventForMarker.getEventType() + ": " + eventForMarker.getCity() + ", " +
                           eventForMarker.getCountry() + " (" + eventForMarker.getYear() + ")";

                   ((TextView) findViewById(R.id.person_name_bottom)).setText(personNameText);
                   ((TextView) findViewById(R.id.event_details_bottom)).setText(eventDetailsText);

                   LinearLayout bottomBar = findViewById(R.id.linear_bottombar);
                   bottomBar.isClickable();

                   bottomBar.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {

                           Intent intent = new Intent(MapsActivity.this, PersonActivity.class);
                           Model.instance().setPersonId(currentPerson.getPersonId());
                           startActivity(intent);
                       }
                   });

                   return true;
               }});

    }

    // adds Event Marker - called from the on Create
    private class AddEventDetails extends AsyncTask<String, ArrayList<Event>, ArrayList<Event>> {
        @Override
        protected ArrayList<Event> doInBackground(String... strings) {
            ServerProxy serverProxy = new ServerProxy();
            serverProxy.setServerPort(serverPort);
            serverProxy.setServerHost(serverHost);

            allEvents = new AllEvents();
            allEvents.setAllEvents(serverProxy.getEvents(strings[0]).getEventsArray());
            return allEvents.getAllEvents();
        }

        @Override
        protected void onPostExecute(ArrayList<Event> events) {
            allEvents.setAllEvents(events);

            ServerProxy serverProxy = new ServerProxy();
            serverProxy.setServerHost(serverHost);
            serverProxy.setServerPort(serverPort);
        }
    }

    private void AddMarkers() {
        if (allPersons == null) {
            allPersons = new AllPersons();
            GetAllPersons getAllPersons = new GetAllPersons();
            getAllPersons.execute(authToken);
        }
        if (allEvents == null) {
            allEvents = new AllEvents();
            AddEventDetails addEventDetails = new AddEventDetails();
            addEventDetails.execute(authToken);
        }

            if (showAllMarkers) {
                for (Event event : allEvents.getAllEvents()) {
                    if (eventTypes.isEmpty() || !eventTypes.contains(event.getEventType())) {
                        eventTypes.add(event.getEventType());
                    }
                }
                mapColorsToEvents();

                doShowAllMarkers();
            }
            else {
                notShowAllMarkers();
        }
    }

    private void doShowAllMarkers() {
        for (Event currentEvent : allEvents.getAllEvents()) {

            for (Person person : allPersons.getAllPersonsArray()) {
                if (person.getPersonId().equals(currentEvent.getPersonId())) {
                    currentPerson = person;
                }
            }

//                mMap.addMarker(new MarkerOptions()
//                        .position(new LatLng(currentEvent.getLatitude(), currentEvent.getLongitude()))
//                        .title(currentEvent.getEventID())
//                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))).setTag(currentEvent);

            MarkerOptions markerOptions = new MarkerOptions()
                    .position(new LatLng(currentEvent.getLatitude(), currentEvent.getLongitude()))
                    .title(currentEvent.getEventID())
                    .icon(BitmapDescriptorFactory.defaultMarker(eventTypeColors.get(currentEvent.getEventType().toLowerCase())));

            Marker marker = mMap.addMarker(markerOptions);
            marker.setTag(currentEvent);

            markers.add(marker);

            if (currentEvent.getEventID().equals(givenEventId)) {
                moveCamera(currentEvent);

                Event eventForMarker = null;
                for (Event event : allEvents.getAllEvents()) {
                    if (event.getEventID().equals(givenEventId)) {
                        eventForMarker = event;
                        break;
                    }
                }

                for (Person person : allPersons.getAllPersonsArray()) {
                    if (person.getPersonId().equals(eventForMarker.getPersonId())) {
                        currentPerson = person;
                        break;
                    }
                }

                String personNameText = currentPerson.getFirstName() + " " + currentPerson.getLastName();

                if (currentPerson.getGender().equals("F")) {
                    ((ImageView) findViewById(R.id.bottom_icon)).setImageDrawable((femaleIcon));
                } else if (currentPerson.getGender().equals("M")) {
                    ((ImageView) findViewById(R.id.bottom_icon)).setImageDrawable((maleIcon));
                }

                String eventDetailsText = eventForMarker.getEventType() + ": " + eventForMarker.getCity() + ", " +
                        eventForMarker.getCountry() + " (" + eventForMarker.getYear() + ")";

                ((TextView) findViewById(R.id.person_name_bottom)).setText(personNameText);
                ((TextView) findViewById(R.id.event_details_bottom)).setText(eventDetailsText);

                LinearLayout bottomBar = findViewById(R.id.linear_bottombar);
                bottomBar.isClickable();

                bottomBar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(MapsActivity.this, PersonActivity.class);
                        Bundle bundie = new Bundle();

                        bundie.putCharSequence("personId", currentPerson.getPersonId());
                        bundie.putCharSequence("authToken", authToken);
                        bundie.putCharSequence("serverPort", serverPort);
                        bundie.putCharSequence("serverHost", serverHost);
                        intent.putExtra("allPersons", filteredAllPersons);
                        intent.putExtra("allEvents", filteredAllEvents); //questionable -- todo look at specs
                        intent.putExtras(bundie);
                        startActivity(intent);
                    }
                });
            }

            if (showSpouseLines && allPersons != null) {
                showSpouseLinesMethod(allEvents, currentEvent);
            }
            if (showLifeStoryLines && allPersons != null) {
                showLifeStoryMethod(allEvents, currentEvent);
            }
            if (showFamilyTreeLines && allPersons != null) {
                showFamilyTreeMethod(allEvents, currentEvent);
            }
        }
    }

    private void notShowAllMarkers() {
        System.out.println("right after notShowallMarkers");
        mMap.clear();

        if (filteredAllEvents == null) {
            System.out.println("filtered all events is null");
            filteredAllEvents = new AllEvents();
            filteredAllEvents.setAllEvents(new ArrayList<Event>());
        }

        if (filteredAllPersons == null) {
            System.out.println("filtered all persons is null");
            filteredAllPersons = new AllPersons();
            filteredAllPersons.setAllPersons(new ArrayList<Person>());
        }
        for (Event currentEvent : allEvents.getAllEvents()) {
            //System.out.println(updatedEventTypes);
            boolean good = false;
            if (updatedEventTypes == null) {
                updatedEventTypes = eventTypes;
            }
            for (String eventType : updatedEventTypes) {
                if (currentEvent.getEventType().toLowerCase().equals(eventType.toLowerCase())) {
                    good = true;
                    break;
                }
                else if (eventType.toLowerCase().equals("female")) {
                    for (Person person : allPersons.getAllPersonsArray()) {
                        if (person.getPersonId().equals(currentEvent.getEventID())) {
                            if (person.getGender().toUpperCase().equals("F")) {
                                good = true;
                                break;
                            }
                        }
                    }
                    if (good) {
                        break;
                    }
                }
                else if (eventType.toLowerCase().equals("male")) {
                    for (Person person : allPersons.getAllPersonsArray()) {
                        if (person.getPersonId().equals(currentEvent.getEventID())) {
                            if (person.getGender().toUpperCase().equals("M")) {
                                good = true;
                                break;
                            }
                        }
                    }
                    if (good) {
                        break;
                    }
                }
            }
            if (good) {
                filteredAllEvents.getAllEvents().add(currentEvent);
//                    mMap.addMarker(new MarkerOptions()
//                            .position(new LatLng(currentEvent.getLatitude(), currentEvent.getLongitude()))
//                            .title(currentEvent.getEventID())
//                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))).setTag(currentEvent);

                MarkerOptions markerOptions = new MarkerOptions()
                        .position(new LatLng(currentEvent.getLatitude(), currentEvent.getLongitude()))
                        .title(currentEvent.getEventID())
                        .icon(BitmapDescriptorFactory.defaultMarker(eventTypeColors.get(currentEvent.getEventType().toLowerCase())));

                Marker marker = mMap.addMarker(markerOptions);
                marker.setTag(currentEvent);

                markers.add(marker);

                if (currentEvent.getEventID().equals(givenEventId)) {
                    moveCamera(currentEvent);
                }

                if (showSpouseLines && allPersons != null) {
                    showSpouseLinesMethod(filteredAllEvents, currentEvent);
                }
                if (showLifeStoryLines && allPersons != null) {
                    showLifeStoryMethod(filteredAllEvents, currentEvent);
                }
                if (showFamilyTreeLines && allPersons != null) {
                    showFamilyTreeMethod(filteredAllEvents, currentEvent);
                }
            }
        }
        for (String eventType: updatedEventTypes) {
            System.out.println("for testing " + eventType);
            if (eventType.toLowerCase().equals("mother")) {
                mMap.clear();
                updateMotherEvents();
            } else if (eventType.toLowerCase().equals("father")) {
                mMap.clear();
                updateFatherEvents();
            }
        }
        for (Event currentEvent : filteredAllEvents.getAllEvents()) {
            if (showSpouseLines && filteredAllPersons != null) {
                showSpouseLinesMethod(filteredAllEvents, currentEvent);
            }
            if (showLifeStoryLines && filteredAllPersons != null) {
                showLifeStoryMethod(filteredAllEvents, currentEvent);
            }
            if (showFamilyTreeLines && filteredAllPersons != null) {
                showFamilyTreeMethod(filteredAllEvents, currentEvent);
            }
        }
    }

    private void recursiveParents(Person givenPerson) {
        for (Person person : allPersons.getAllPersonsArray()) {
            if (person.getPersonId().equals(givenPerson.getMother()) || person.getPersonId().equals(givenPerson.getFather())) {
                filteredAllPersons.getAllPersonsArray().add(person);
                recursiveParents(person);
            }
        }
    }

    private class GetSingleEvent extends AsyncTask<String, Event, Event> {
        @Override
        protected Event doInBackground(String...eventId) {
            ServerProxy serverProxy = new ServerProxy();
            serverProxy.setServerPort(serverPort);
            serverProxy.setServerHost(serverHost);

            Event singleEvent = serverProxy.getSingleEvent(eventId[0], authToken).getEvent();
            return singleEvent;
        }

        @Override
        protected void onPostExecute(Event event) {
            moveCamera(event);
        }
    }

    private void moveCamera(Event event) {
        if (event != null) {
            LatLng position = new LatLng(event.getLatitude(), event.getLongitude());
            //Build camera position
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(position)
                    .zoom(3).build();
            //Zoom in and animate the camera.
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    private void updateFatherEvents() {
        if (filteredAllEvents == null || filteredAllEvents.getAllEvents() == null || filteredAllEvents.getAllEvents().isEmpty()) {
            filteredAllEvents = allEvents;
        }

        if (filteredAllPersons == null) {
            filteredAllPersons = new AllPersons();
            if (filteredAllPersons.getAllPersonsArray() == null) {
                filteredAllPersons.setAllPersons(new ArrayList<Person>());
            }
        }

        for (Person person : allPersons.getAllPersonsArray()) {
            if (person != null && user != null && person.getPersonId().equals(user.getFather())) {
                filteredAllPersons.getAllPersonsArray().add(person);
                recursiveParents(person);
                break;
            }
        }

        AllEvents tempEvents = null;
        try {
            tempEvents = (AllEvents) filteredAllEvents.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        filteredAllEvents.setAllEvents(new ArrayList<Event>());
        if (tempEvents != null && filteredAllPersons != null && filteredAllPersons.getAllPersonsArray() != null) {
            for (Event theEvent : tempEvents.getAllEvents()) {
                for (Person thePerson : filteredAllPersons.getAllPersonsArray()) {
                    if (theEvent.getPersonId().equals(thePerson.getPersonId())) {
                        filteredAllEvents.getAllEvents().add(theEvent);

                        MarkerOptions markerOptions = new MarkerOptions()
                                .position(new LatLng(theEvent.getLatitude(), theEvent.getLongitude()))
                                .title(theEvent.getEventID())
                                .icon(BitmapDescriptorFactory.defaultMarker(eventTypeColors.get(theEvent.getEventType().toLowerCase())));

                        Marker marker = mMap.addMarker(markerOptions);
                        marker.setTag(theEvent);

                        markers.add(marker);
                    }

                }
            }
            System.out.println(filteredAllEvents.getAllEvents());
            System.out.println(filteredAllPersons.getAllPersonsArray());
        }
    }

    private void updateMotherEvents() {
        if (filteredAllEvents == null || filteredAllEvents.getAllEvents() == null || filteredAllEvents.getAllEvents().isEmpty()) {
            filteredAllEvents = allEvents;
        }

        if (filteredAllPersons == null) {
            filteredAllPersons = new AllPersons();
            if (filteredAllPersons.getAllPersonsArray() == null) {
                filteredAllPersons.setAllPersons(new ArrayList<Person>());
            }
        }

        for (Person person : allPersons.getAllPersonsArray()) {
            if (person != null && user != null && person.getPersonId().equals(user.getMother())) {
                filteredAllPersons.getAllPersonsArray().add(person);
                recursiveParents(person);
                break;
            }
        }

        AllEvents tempEvents = null;
        try {
            tempEvents = (AllEvents) filteredAllEvents.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        filteredAllEvents.setAllEvents(new ArrayList<Event>());
        if (tempEvents != null && filteredAllPersons != null && filteredAllPersons.getAllPersonsArray() != null) {
            for (Event theEvent : tempEvents.getAllEvents()) {
                for (Person thePerson : filteredAllPersons.getAllPersonsArray()) {
                    if (theEvent.getPersonId().equals(thePerson.getPersonId())) {
                        filteredAllEvents.getAllEvents().add(theEvent);

                        MarkerOptions markerOptions = new MarkerOptions()
                                .position(new LatLng(theEvent.getLatitude(), theEvent.getLongitude()))
                                .title(theEvent.getEventID())
                                .icon(BitmapDescriptorFactory.defaultMarker(eventTypeColors.get(theEvent.getEventType().toLowerCase())));

                        Marker marker = mMap.addMarker(markerOptions);
                        marker.setTag(theEvent);

                        markers.add(marker);
                    }

                }
            }
            System.out.println(filteredAllEvents.getAllEvents());
            System.out.println(filteredAllPersons.getAllPersonsArray());
        }
    }

    private class GetSinglePerson extends AsyncTask<String, Person, Person> {
        @Override
        protected Person doInBackground(String... strings) {
            ServerProxy serverProxy = new ServerProxy();
            serverProxy.setServerPort(serverPort);
            serverProxy.setServerHost(serverHost);

            currentPerson = serverProxy.getPerson(strings[0], authToken).getPerson();
            return currentPerson;
        }

        @Override
        protected void onPostExecute(Person person) {
            currentPerson = person;
        }
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

    private void showSpouseLinesMethod(AllEvents allEventsHere, Event currentEvent) {
        if (currentPerson != null) {
            Event earliestEventPerson = getEarliestEvent(currentPerson.getPersonId(), allEventsHere);
            if (currentEvent.getYear() == earliestEventPerson.getYear()) {

                for (Person person : allPersons.getAllPersonsArray()) {
                    if (currentPerson != null && person.getPersonId().equals(currentPerson.getSpouse())) {

                        Event earliestEventSpouse = getEarliestEvent(person.getPersonId(), allEventsHere);

                        mMap.addPolyline(new PolylineOptions()
                                .add(
                                        new LatLng(earliestEventPerson.getLatitude(), earliestEventPerson.getLongitude()),
                                        new LatLng(earliestEventSpouse.getLatitude(), earliestEventSpouse.getLongitude())
                                )
                                .width(12)
                                .geodesic(true)
                                .color(getColorNum(spouseLinesColor)));
                        break;
                    }
                }
            }
        }
    }

    private void showLifeStoryMethod(AllEvents allEventsHere, Event currentEvent) {
        for (Event event : allEventsHere.getAllEvents()) {
            if (event.getYear() > currentEvent.getYear() && currentEvent.getPersonId().equals(event.getPersonId())) {
                Polyline polyLine = mMap.addPolyline(new PolylineOptions()
                        .add(
                                new LatLng(currentEvent.getLatitude(), currentEvent.getLongitude()),
                                new LatLng(event.getLatitude(), event.getLongitude())
                        )
                        .width(12)
                        .geodesic(true)
                .color(getColorNum(lifeStoryLinesColor)));
            }
        }
    }

    private void showFamilyTreeMethod(AllEvents allEventsHere, Event currentEvent) {
        if (filteredAllPersons == null || filteredAllPersons.getAllPersonsArray() == null || filteredAllPersons.getAllPersonsArray().isEmpty()) {
            filteredAllPersons = (AllPersons) allPersons;
        }

        Event userEarliestEvent = getEarliestEvent(user.getPersonId(), allEventsHere);
        for (Person person : filteredAllPersons.getAllPersonsArray()) {
            if (user != null && person.getMother() != null) {
                if (person.getPersonId().equals(user.getMother())) {
                    Event earliestEventMother = getEarliestEvent(person.getPersonId(), allEventsHere);
                    Polyline polyLine = mMap.addPolyline(new PolylineOptions()
                            .add(
                                    new LatLng(userEarliestEvent.getLatitude(), userEarliestEvent.getLongitude()),
                                    new LatLng(earliestEventMother.getLatitude(), earliestEventMother.getLongitude())
                            )
                            .width((float) fatherLineWidth)
                            .geodesic(true)
                            .color(getColorNum(familyTreeLinesColor)));
                    motherLineWidth = motherLineWidth / 1.2;
                    findParentsRecursive(person, allEventsHere, motherLineWidth, earliestEventMother);
                }
            }
            if (user != null && person != null && person.getFather() != null) {
                if (person.getPersonId().equals(user.getFather())) {
                    Event earliestEventFather = getEarliestEvent(person.getPersonId(), allEventsHere);
                    Polyline polyLine = mMap.addPolyline(new PolylineOptions()
                            .add(
                                    new LatLng(userEarliestEvent.getLatitude(), userEarliestEvent.getLongitude()),
                                    new LatLng(earliestEventFather.getLatitude(), earliestEventFather.getLongitude())
                            )
                            .width((float) fatherLineWidth)
                            .geodesic(true)
                            .color(getColorNum(familyTreeLinesColor)));
                    fatherLineWidth = fatherLineWidth / 1.2;
                    findParentsRecursive(person, allEventsHere, fatherLineWidth, earliestEventFather);
                }
            }
        }
    }

    private void findParentsRecursive(Person person, AllEvents allEventsHere, double lineWidth, Event currentEvent) {
        for (Person parentPerson : filteredAllPersons.getAllPersonsArray()) {
            if (person != null && person.getMother() != null) {
                if (parentPerson.getPersonId().equals(person.getMother())) {
                    Event earliestEventMother = getEarliestEvent(person.getPersonId(), allEventsHere);
                    mMap.addPolyline(new PolylineOptions()
                            .add(
                                    new LatLng(currentEvent.getLatitude(), currentEvent.getLongitude()),
                                    new LatLng(earliestEventMother.getLatitude(), earliestEventMother.getLongitude())
                            )
                            .width((float) lineWidth)
                            .geodesic(true)
                            .color(getColorNum(familyTreeLinesColor)));
                    lineWidth = lineWidth / 1.5;
                    findParentsRecursive(parentPerson, allEventsHere, lineWidth, earliestEventMother);
                }
            }
            if (person != null && person.getFather() != null) {
                if (parentPerson.getPersonId().equals(person.getFather())) {
                    Event earliestEventFather = getEarliestEvent(person.getPersonId(), allEventsHere);
                    mMap.addPolyline(new PolylineOptions()
                            .add(
                                    new LatLng(currentEvent.getLatitude(), currentEvent.getLongitude()),
                                    new LatLng(earliestEventFather.getLatitude(), earliestEventFather.getLongitude())
                            )
                            .width((float) lineWidth)
                            .geodesic(true)
                            .color(getColorNum(familyTreeLinesColor)));
                    lineWidth = lineWidth / 1.5;
                    findParentsRecursive(parentPerson, allEventsHere, lineWidth, earliestEventFather);

                }
            }
        }
    }

    private Event getEarliestEvent(String personId, AllEvents allEventsHere) {
        int earliestEventYear = 9999;
        Event earliestEvent = null;

        for (Event event : allEventsHere.getAllEvents()) {
            if (event.getPersonId().equals(personId) && event.getYear() < earliestEventYear) {
                earliestEventYear = event.getYear();
                earliestEvent = event;
            }
        }
        return earliestEvent;
    }

    private int getColorNum(String colorName) {
        switch (colorName) {
            case "RED":
                return Color.RED;
            case "GREEN":
                return Color.GREEN;
            case "YELLOW":
                return Color.YELLOW;
            default:
                return Color.RED;
        }
    }

    private void mapColorsToEvents() {
        float[] hexColors = new float[]{0f, 320f, 100f, 200f, 20f, 140f, 60f, 280f, 220f, 300f, 70f, 200f, 90f, 180f, 130f, 150f, 240f, 310f, 250f, 42f, 315f, 118f, 342f};

        for (int i = 0; i < eventTypes.size(); i++) {
            eventTypeColors.put(eventTypes.get(i).toLowerCase(), hexColors[i]);
        }


    }
}

/*
TODO:
- life story line
- put everything in nice clean functions
- print off pass off
- try to pass off tomorrow first try
- also figure out how to test this
- preserve map type and settings
- event activity + up button
- order persons in chronological order
- resync returns back to maps activity
*/

