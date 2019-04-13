package com.example.familymapclient;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import src.Model.Event;
import src.Model.Person;
import src.ServerProxy;

public class MapFragment extends Fragment {
    
    double fatherLineWidth = 50;
    double motherLineWidth = 50;
    private Drawable maleIcon;
    private Drawable femaleIcon;
    private GoogleMap mMap;

    private HashMap<String, Float> eventTypeColors = new HashMap<>();
    
    private View v;


    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_map, container, false);

        // Inflate the layout for this fragment

        setUpTopBarIcons();
        setUpBottomBar();

        Iconify.with(new FontAwesomeModule());


        FragmentManager fm = getChildFragmentManager();
        SupportMapFragment mapFragment = (SupportMapFragment) fm.findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;

                switch (Model.instance().getMapType().toLowerCase()) {
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

                createMarkerData();

                for (Event event : Model.instance().getAllEvents().getAllEvents()) {
                    if (Model.instance().getEventTypes().isEmpty() || !Model.instance().getEventTypes().contains(event.getEventType())) {
                        Model.instance().getEventTypes().add(event.getEventType());
                    }
                }
                mapColorsToEvents();

                AddMarkers();

                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(final Marker marker) {
                        //collect all the data from that marker...
                        Event eventForMarker = (Event) marker.getTag();

                        for (Person person : Model.instance().getAllPersons().getAllPersonsArray()) {
                            assert eventForMarker != null;
                            if (person.getPersonId().equals(eventForMarker.getPersonId())) {
                                Model.instance().setCurrentPerson(person);
                                break;
                            }
                        }

                        if (Model.instance().getFilteredAllEvents() == null || Model.instance().getFilteredAllEvents().getAllEvents() == null) {
                            Model.instance().setFilteredAllEvents(Model.instance().getAllEvents());
                        }

                        if (Model.instance().isShowAllMarkers() && Model.instance().getAllPersons() != null) {
                            showSpouseLinesMethod(Model.instance().getFilteredAllEvents(), (Event) marker.getTag());
                        }
                        if (Model.instance().isShowLifeStoryLines() && Model.instance().getAllPersons() != null) {
                            showLifeStoryMethod(Model.instance().getAllEvents(), (Event) marker.getTag());
                        }
                        if (Model.isShowFamilyTreeLines() && Model.instance().getAllPersons() != null) {
                            showFamilyTreeMethod(Model.instance().getAllEvents(), (Event) marker.getTag());
                        }

                        String personNameText = Model.instance().getCurrentPerson().getFirstName() + " " + Model.instance().getCurrentPerson().getLastName();

                        if (Model.instance().getCurrentPerson().getGender().toUpperCase().equals("F")) {
                            ((ImageView) v.findViewById(R.id.bottom_icon)).setImageDrawable((femaleIcon));
                        } else if (Model.instance().getCurrentPerson().getGender().toUpperCase().equals("M")) {
                            ((ImageView) v.findViewById(R.id.bottom_icon)).setImageDrawable((maleIcon));
                        }

                        assert eventForMarker != null;
                        String eventDetailsText = eventForMarker.getEventType() + ": " + eventForMarker.getCity() + ", " +
                                eventForMarker.getCountry() + " (" + eventForMarker.getYear() + ")";

                        ((TextView) v.findViewById(R.id.person_name_bottom)).setText(personNameText);
                        ((TextView) v.findViewById(R.id.event_details_bottom)).setText(eventDetailsText);

                        LinearLayout bottomBar = v.findViewById(R.id.linear_bottombar);
                        bottomBar.isClickable();

                        bottomBar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent intent = new Intent(getActivity(), PersonActivity.class);

                                Model.instance().setPersonId(((Event) marker.getTag()).getPersonId());

                                startActivity(intent);
                            }
                        });

                        return true;
                    }
                });
            }
        });

        
        return v;
    }

    private void createMarkerData() {
        assert Model.instance().getAllEvents() != null;
        if (Model.instance().getAllEvents() == null) {
            Model.instance().setAllEvents(new AllEvents());
            AddEventDetails addEventDetails = new AddEventDetails();
            try {
                AllEvents tempNewEvents = new AllEvents();
                tempNewEvents.setAllEvents(addEventDetails.execute(Model.instance().getAuthToken()).get());
                Model.instance().setAllEvents(tempNewEvents);

                if (Model.instance().getFilteredAllEvents() == null) {
                    Model.instance().setFilteredAllEvents(Model.instance().getAllEvents());
                    Model.instance().getFilteredAllEvents().setAllEvents(Model.instance().getAllEvents().getAllEvents());
                }
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for (Event event : Model.instance().getAllEvents().getAllEvents()) {
                if (Model.instance().getEventTypes().isEmpty() || !Model.instance().getEventTypes().contains(event.getEventType())) {
                    Model.instance().getEventTypes().add(event.getEventType());
                }
            }
            mapColorsToEvents();
        }
        if (Model.instance().getAllPersons() == null || Model.instance().getAllPersons().getAllPersonsArray() == null) {
            Model.instance().setAllPersons(new AllPersons());
            GetAllPersons getAllPersons = new GetAllPersons();
            try {
                Model.instance().getAllPersons().setAllPersons(getAllPersons.execute(Model.instance().getAuthToken()).get());

                if (Model.instance().getFilteredAllPersons() == null || Model.instance().getFilteredAllPersons().getAllPersonsArray() == null) {
                    Model.instance().setFilteredAllPersons(Model.instance().getAllPersons());
                    Model.instance().getFilteredAllPersons().setAllPersons(Model.instance().getAllPersons().getAllPersonsArray());
                }
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private void setUpBottomBar() {

        //***************** BOTTOM BAR
        maleIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_male).colorRes(R.color.orange).sizeDp(40);
        femaleIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_female).colorRes(R.color.purple).sizeDp(40);
        Drawable defaultIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_question).colorRes(R.color.yellow).sizeDp(40);

        ((ImageView) v.findViewById(R.id.bottom_icon)).setImageDrawable((defaultIcon));

        String defaultText = "Click any marker for event details";
        ((TextView) v.findViewById(R.id.person_name_bottom)).setText(defaultText);
    }

    private void setUpTopBarIcons() {
        //************* TOP BAR
        Drawable searchButtonIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_search).colorRes(R.color.colorAccent).sizeDp(40);
        Drawable filterButtonIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_filter).colorRes(R.color.colorAccent).sizeDp(40);
        Drawable settingsButtonIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_gear).colorRes(R.color.colorAccent).sizeDp(40);

        ((ImageView) v.findViewById(R.id.settingsButton)).setImageDrawable(settingsButtonIcon);
        ((ImageView) v.findViewById(R.id.searchButton)).setImageDrawable(searchButtonIcon);
        ((ImageView) v.findViewById(R.id.filterButton)).setImageDrawable(filterButtonIcon);

        ImageButton searchButton = (ImageButton) v.findViewById(R.id.searchButton);
        ImageButton filterButton = (ImageButton) v.findViewById(R.id.filterButton);
        ImageButton settingsButton = (ImageButton) v.findViewById(R.id.settingsButton);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
            }
        });

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), FilterActivity.class);
                startActivity(intent);
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), SettingsActivity2.class);
                startActivity(intent);
            }
        });
    }

    // adds Event Marker - called from the on Create
    private class AddEventDetails extends AsyncTask<String, ArrayList<Event>, ArrayList<Event>> {
        @Override
        protected ArrayList<Event> doInBackground(String... strings) {
            ServerProxy serverProxy = new ServerProxy();
            serverProxy.setServerPort(Model.instance().getServerPort());
            serverProxy.setServerHost(Model.instance().getServerHost());

            Model.instance().setAllEvents(new AllEvents());
            Model.instance().getAllEvents().setAllEvents(serverProxy.getEvents(strings[0]).getEventsArray());
            return Model.instance().getAllEvents().getAllEvents();
        }

        @Override
        protected void onPostExecute(ArrayList<Event> events) {
            Model.instance().getAllEvents().setAllEvents(events);

            ServerProxy serverProxy = new ServerProxy();
            serverProxy.setServerHost(Model.instance().getServerHost());
            serverProxy.setServerPort(Model.instance().getServerPort());
        }
    }

    private void AddMarkers() {
        if (Model.instance().getAllPersons() == null) {
            Model.instance().setAllPersons(new AllPersons());
            GetAllPersons getAllPersons = new GetAllPersons();
            getAllPersons.execute(Model.instance().getAuthToken());
        }
        if (Model.instance().getAllEvents() == null) {
            Model.instance().setAllEvents(new AllEvents());
            AddEventDetails addEventDetails = new AddEventDetails();
            addEventDetails.execute(Model.instance().getAuthToken());
        }

        if (Model.instance().isShowAllMarkers()) {
            for (Event event : Model.instance().getAllEvents().getAllEvents()) {
                if (Model.instance().getEventTypes().isEmpty() || !Model.instance().getEventTypes().contains(event.getEventType())) {
                    Model.instance().getEventTypes().add(event.getEventType());
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
        for (Event currentEvent : Model.instance().getAllEvents().getAllEvents()) {

            for (Person person : Model.instance().getAllPersons().getAllPersonsArray()) {
                if (person.getPersonId().equals(currentEvent.getPersonId())) {
                    Model.instance().setCurrentPerson(person);
                }
            }

            MarkerOptions markerOptions = new MarkerOptions()
                    .position(new LatLng(currentEvent.getLatitude(), currentEvent.getLongitude()))
                    .title(currentEvent.getEventID())
                    .icon(BitmapDescriptorFactory.defaultMarker(eventTypeColors.get(currentEvent.getEventType().toLowerCase())));

            Marker marker = mMap.addMarker(markerOptions);
            marker.setTag(currentEvent);

            Model.instance().getMarkers().add(marker);

            if (currentEvent.getEventID().equals(Model.instance().getGivenEventId())) {
                moveCamera(currentEvent);

                Event eventForMarker = null;
                for (Event event : Model.instance().getAllEvents().getAllEvents()) {
                    if (event.getEventID().equals(Model.instance().getGivenEventId())) {
                        eventForMarker = event;
                        break;
                    }
                }

                for (Person person : Model.instance().getAllPersons().getAllPersonsArray()) {
                    if (person.getPersonId().equals(eventForMarker.getPersonId())) {
                        Model.instance().setCurrentPerson(person);
                        break;
                    }
                }

                String personNameText = Model.instance().getCurrentPerson().getFirstName() + " " + Model.instance().getCurrentPerson().getLastName();

                if (Model.instance().getCurrentPerson().getGender().equals("F")) {
                    ((ImageView) v.findViewById(R.id.bottom_icon)).setImageDrawable((femaleIcon));
                } else if (Model.instance().getCurrentPerson().getGender().equals("M")) {
                    ((ImageView) v.findViewById(R.id.bottom_icon)).setImageDrawable((maleIcon));
                }

                String eventDetailsText = eventForMarker.getEventType() + ": " + eventForMarker.getCity() + ", " +
                        eventForMarker.getCountry() + " (" + eventForMarker.getYear() + ")";

                ((TextView) v.findViewById(R.id.person_name_bottom)).setText(personNameText);
                ((TextView) v.findViewById(R.id.event_details_bottom)).setText(eventDetailsText);

                LinearLayout bottomBar = v.findViewById(R.id.linear_bottombar);
                bottomBar.isClickable();

                bottomBar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(getActivity(), PersonActivity.class);

                        Model.instance().setPersonId(Model.instance().getCurrentPerson().getPersonId());
                        startActivity(intent);
                    }
                });
            }
        }
    }

    private void notShowAllMarkers() {
        mMap.clear();

        if (Model.instance().getFilteredAllEvents() == null) {
            Model.instance().setFilteredAllEvents(new AllEvents());
            Model.instance().getFilteredAllEvents().setAllEvents(new ArrayList<Event>());
        }

        if (Model.instance().getFilteredAllPersons() == null) {
            Model.instance().setFilteredAllPersons(new AllPersons());
            Model.instance().getFilteredAllPersons().setAllPersons(new ArrayList<Person>());
        }
        for (Event currentEvent : Model.instance().getAllEvents().getAllEvents()) {

            boolean good = false;
            if (Model.instance().getUpdatedEventTypes() == null) {
                Model.instance().setUpdatedEventTypes(Model.instance().getEventTypes());
            }
            for (String eventType : Model.instance().getUpdatedEventTypes()) {
                if (currentEvent.getEventType().toLowerCase().equals(eventType.toLowerCase())) {
                    good = true;
                    break;
                }
                else if (eventType.toLowerCase().equals("female")) {
                    for (Person person : Model.instance().getAllPersons().getAllPersonsArray()) {
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
                    for (Person person : Model.instance().getAllPersons().getAllPersonsArray()) {
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
                Model.instance().getFilteredAllEvents().getAllEvents().add(currentEvent);

                MarkerOptions markerOptions = new MarkerOptions()
                        .position(new LatLng(currentEvent.getLatitude(), currentEvent.getLongitude()))
                        .title(currentEvent.getEventID())
                        .icon(BitmapDescriptorFactory.defaultMarker(eventTypeColors.get(currentEvent.getEventType().toLowerCase())));

                Marker marker = mMap.addMarker(markerOptions);
                marker.setTag(currentEvent);

                Model.instance().getMarkers().add(marker);

                if (currentEvent.getEventID().equals(Model.instance().getGivenEventId())) {
                    moveCamera(currentEvent);
                }
            }
        }
        for (String eventType: Model.instance().getUpdatedEventTypes()) {
            if (eventType.toLowerCase().equals("mother")) {
                mMap.clear();
                updateMotherEvents();
            } else if (eventType.toLowerCase().equals("father")) {
                mMap.clear();
                updateFatherEvents();
            }
        }
    }

    public void filterByName(ArrayList<String> eventTypes, ArrayList<Event> filteredEvents, AllEvents allEvents) {
        for (String eventType : eventTypes) {
            for (Event event : allEvents.getAllEvents()) {
                if (event.getEventType().equals(eventType)) {
                    filteredEvents.add(event);
                }
            }
        }
    }

    public void filterByParents(ArrayList<String> eventTypes, ArrayList<Event> filteredEvents, AllPersons allPersons, AllEvents allEvents) {
        ArrayList<String> parentIds = new ArrayList<>();

        if (eventTypes.contains("Mother")) {
            parentIds.add(Model.instance().getUser().getPersonId());
            for (Person person : allPersons.getAllPersonsArray())
                if (Model.instance().getUser().getMother() != null) {
                    if (Model.instance().getUser().getMother().equals(person.getPersonId())) {
                        parentIds.add(person.getPersonId());
                        recursiveParentsfunction(person, allPersons, parentIds);
                        break;
                    }
                }
        } else if (eventTypes.contains("Father")) {
            parentIds.add(Model.instance().getUser().getPersonId());
            for (Person person : allPersons.getAllPersonsArray())
                if (Model.instance().getUser().getFather() != null) {
                    if (Model.instance().getUser().getFather().equals(person.getPersonId())) {
                        parentIds.add(person.getPersonId());
                        recursiveParentsfunction(person, allPersons, parentIds);
                        break;
                    }
                }
        }
        for (Event event : allEvents.getAllEvents()) {
            for (String personId : parentIds) {
                if (event.getPersonId().equals(personId)) {
                    filteredEvents.add(event);
                }
            }
        }
    }

    public void filterByGender(ArrayList<String> eventTypes, ArrayList<Event> filteredEvents, AllPersons allPersons, AllEvents allEvents) {
        if (eventTypes.contains("Female")) {
            for (Person person : allPersons.getAllPersonsArray()) {
                for (Event event : allEvents.getAllEvents()) {
                    if (event.getPersonId().equals(person.getPersonId()) && findPersonById(person.getPersonId(), allPersons).getGender().toUpperCase().equals("F")) {
                        filteredEvents.add(event);
                    }
                }
            }
        }

        if (eventTypes.contains("Male")) {
            for (Person person : allPersons.getAllPersonsArray()) {
                for (Event event : allEvents.getAllEvents()) {
                    if (event.getPersonId().equals(person.getPersonId()) && findPersonById(person.getPersonId(), allPersons).getGender().toUpperCase().equals("M")) {
                        filteredEvents.add(event);
                    }
                }
            }
        }
    }

    public ArrayList<Event> filterEvents(ArrayList<String> eventTypes, AllEvents allEvents, AllPersons allPersons) {
        ArrayList<Event> filteredEvents = new ArrayList<>();

        filterByName(eventTypes, filteredEvents, allEvents);
        filterByParents(eventTypes, filteredEvents, allPersons, allEvents);
        filterByGender(eventTypes, filteredEvents, allPersons, allEvents);

        return filteredEvents;
    }

    private void recursiveParentsfunction(Person person, AllPersons allPersons, ArrayList<String> parentIds) {
        if (person.getMother() != null && person.getFather() != null) {
            for (Person comparePerson : allPersons.getAllPersonsArray()) {
                if (comparePerson.getPersonId().equals(person.getFather()) || comparePerson.getPersonId().equals(person.getMother())) {
                    parentIds.add(person.getPersonId());
                    recursiveParentsfunction(person, allPersons, parentIds);
                    break;
                }
            }
        }
    }

    public ArrayList<String> findParents(String currentPersonId, AllPersons allPersons) {
        Person currentPerson = findPersonById(currentPersonId, allPersons);
        ArrayList<String> parentsIds = new ArrayList<>();
        for (Person person : allPersons.getAllPersonsArray()) {
            if (person.getPersonId().equals(currentPerson.getFather()) || person.getPersonId().equals(currentPerson.getMother())) {
                parentsIds.add(person.getPersonId());
            }
        }
        return parentsIds;
    }

    private Person findPersonById(String personId, AllPersons allPersons) {
        for (Person person: allPersons.getAllPersonsArray()) {
            if (person.getPersonId().equals(personId)) {
                return person;
            }
        }
        return null;
    }

    private void recursiveParents(Person givenPerson) {
        for (Person person : Model.instance().getAllPersons().getAllPersonsArray()) {
            if (person.getPersonId().equals(givenPerson.getMother()) || person.getPersonId().equals(givenPerson.getFather())) {
                Model.instance().getFilteredAllPersons().getAllPersonsArray().add(person);
                recursiveParents(person);
            }
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
        if (Model.instance().getFilteredAllEvents() == null || Model.instance().getFilteredAllEvents().getAllEvents() == null || Model.instance().getFilteredAllEvents().getAllEvents().isEmpty()) {
            Model.instance().setFilteredAllEvents(Model.instance().getAllEvents());
        }

        if (Model.instance().getFilteredAllPersons() == null) {
            Model.instance().setFilteredAllPersons(new AllPersons());
            if (Model.instance().getFilteredAllPersons().getAllPersonsArray() == null) {
                Model.instance().getFilteredAllPersons().setAllPersons(new ArrayList<Person>());
            }
        }

        for (Person person : Model.instance().getAllPersons().getAllPersonsArray()) {
            if (person != null && Model.instance().getUser() != null && person.getPersonId().equals(Model.instance().getUser().getFather())) {
                Model.instance().getFilteredAllPersons().getAllPersonsArray().add(person);
                recursiveParents(person);
                break;
            }
        }

        AllEvents tempEvents = null;
        try {
            tempEvents = (AllEvents) Model.instance().getFilteredAllEvents().clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        Model.instance().getFilteredAllEvents().setAllEvents(new ArrayList<Event>());
        if (tempEvents != null && Model.instance().getFilteredAllPersons() != null && Model.instance().getFilteredAllPersons().getAllPersonsArray() != null) {
            for (Event theEvent : tempEvents.getAllEvents()) {
                for (Person thePerson : Model.instance().getFilteredAllPersons().getAllPersonsArray()) {
                    if (theEvent.getPersonId().equals(thePerson.getPersonId())) {
                        Model.instance().getFilteredAllEvents().getAllEvents().add(theEvent);

                        MarkerOptions markerOptions = new MarkerOptions()
                                .position(new LatLng(theEvent.getLatitude(), theEvent.getLongitude()))
                                .title(theEvent.getEventID())
                                .icon(BitmapDescriptorFactory.defaultMarker(eventTypeColors.get(theEvent.getEventType().toLowerCase())));

                        Marker marker = mMap.addMarker(markerOptions);
                        marker.setTag(theEvent);

                        Model.instance().getMarkers().add(marker);
                    }

                }
            }
            System.out.println(Model.instance().getFilteredAllEvents().getAllEvents());
            System.out.println(Model.instance().getFilteredAllPersons().getAllPersonsArray());
        }
    }

    private void updateMotherEvents() {
        if (Model.instance().getFilteredAllEvents() == null || Model.instance().getFilteredAllEvents().getAllEvents() == null || Model.instance().getFilteredAllEvents().getAllEvents().isEmpty()) {
            Model.instance().setFilteredAllEvents(Model.instance().getAllEvents());
        }

        if (Model.instance().getFilteredAllPersons() == null) {
            Model.instance().setFilteredAllPersons(new AllPersons());
            if (Model.instance().getFilteredAllPersons().getAllPersonsArray() == null) {
                Model.instance().getFilteredAllPersons().setAllPersons(new ArrayList<Person>());
            }
        }

        for (Person person : Model.instance().getAllPersons().getAllPersonsArray()) {
            if (person != null && Model.instance().getUser() != null && person.getPersonId().equals(Model.instance().getUser().getMother())) {
                Model.instance().getFilteredAllPersons().getAllPersonsArray().add(person);
                recursiveParents(person);
                break;
            }
        }

        AllEvents tempEvents = null;
        try {
            tempEvents = (AllEvents) Model.instance().getFilteredAllEvents().clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        Model.instance().getFilteredAllEvents().setAllEvents(new ArrayList<Event>());
        if (tempEvents != null && Model.instance().getFilteredAllPersons() != null && Model.instance().getFilteredAllPersons().getAllPersonsArray() != null) {
            for (Event theEvent : tempEvents.getAllEvents()) {
                for (Person thePerson : Model.instance().getFilteredAllPersons().getAllPersonsArray()) {
                    if (theEvent.getPersonId().equals(thePerson.getPersonId())) {
                        Model.instance().getFilteredAllEvents().getAllEvents().add(theEvent);

                        MarkerOptions markerOptions = new MarkerOptions()
                                .position(new LatLng(theEvent.getLatitude(), theEvent.getLongitude()))
                                .title(theEvent.getEventID())
                                .icon(BitmapDescriptorFactory.defaultMarker(eventTypeColors.get(theEvent.getEventType().toLowerCase())));

                        Marker marker = mMap.addMarker(markerOptions);
                        marker.setTag(theEvent);

                        Model.instance().getMarkers().add(marker);
                    }

                }
            }
            System.out.println(Model.instance().getFilteredAllEvents().getAllEvents());
            System.out.println(Model.instance().getFilteredAllPersons().getAllPersonsArray());
        }
    }

    private class GetSinglePerson extends AsyncTask<String, Person, Person> {
        @Override
        protected Person doInBackground(String... strings) {
            ServerProxy serverProxy = new ServerProxy();
            serverProxy.setServerPort(Model.instance().getServerPort());
            serverProxy.setServerHost(Model.instance().getServerHost());

            Model.instance().setCurrentPerson(serverProxy.getPerson(strings[0], Model.instance().getAuthToken()).getPerson());
            return Model.instance().getCurrentPerson();
        }

        @Override
        protected void onPostExecute(Person person) {
            Model.instance().setCurrentPerson(person);
        }
    }

    private class GetAllPersons extends AsyncTask<String, ArrayList<Person>, ArrayList<Person>> {
        @Override
        protected ArrayList<Person> doInBackground(String... strings) {
            ServerProxy serverProxy = new ServerProxy();
            serverProxy.setServerPort(Model.instance().getServerPort());
            serverProxy.setServerHost(Model.instance().getServerHost());

            Model.instance().getAllPersons().setAllPersons(serverProxy.getPersons(Model.instance().getAuthToken()).getPersonsArray());
            return Model.instance().getAllPersons().getAllPersonsArray();
        }

        @Override
        protected void onPostExecute(ArrayList<Person> persons) {
            Model.instance().getAllPersons().setAllPersons(persons);
        }
    }

    private void showSpouseLinesMethod(AllEvents allEventsHere, Event currentEvent) {
        findSpouse(currentEvent.getPersonId(), Model.instance().getAllPersons());
        if (Model.instance().getCurrentPerson() != null) {
            Event earliestEventPerson = getEarliestEvent(Model.instance().getCurrentPerson().getPersonId(), allEventsHere);
            if (currentEvent.getYear() == earliestEventPerson.getYear()) {

                for (Person person : Model.instance().getAllPersons().getAllPersonsArray()) {
                    if (Model.instance().getCurrentPerson() != null && person.getPersonId().equals(Model.instance().getCurrentPerson().getSpouse())) {

                        Event earliestEventSpouse = getEarliestEvent(person.getPersonId(), allEventsHere);

                        mMap.addPolyline(new PolylineOptions()
                                .add(
                                        new LatLng(earliestEventPerson.getLatitude(), earliestEventPerson.getLongitude()),
                                        new LatLng(earliestEventSpouse.getLatitude(), earliestEventSpouse.getLongitude())
                                )
                                .width(12)
                                .geodesic(true)
                                .color(getColorNum(Model.instance().getSpouseLinesColor())));
                        break;
                    }
                }
            }
        }
    }

    public String findSpouse(String personId, AllPersons allPersons) {
        for (Person person : allPersons.getAllPersonsArray()) {
            if(person.getSpouse() != null) {
                if (person.getSpouse().equals(personId)) {
                    return person.getPersonId();
                }
            }
        }
        return null;
    }

    private void showLifeStoryMethod(AllEvents allEventsHere, Event currentEvent) {
        ArrayList<Event> personEvents = new ArrayList<>();
        for (Event event : allEventsHere.getAllEvents()) {
            if (event.getPersonId().equals(currentEvent.getPersonId())) {
                personEvents.add(event);
            }
        }

        Event tempEvent = getEarliestEvent(currentEvent.getPersonId(), allEventsHere);
        for (Event event : personEvents) {

        }

        for (Event event : personEvents) {
            if (event.getYear() > tempEvent.getYear() && currentEvent.getPersonId().equals(event.getPersonId())) {
                Polyline polyLine = mMap.addPolyline(new PolylineOptions()
                        .add(
                                new LatLng(currentEvent.getLatitude(), currentEvent.getLongitude()),
                                new LatLng(event.getLatitude(), event.getLongitude())
                        )
                        .width(12)
                        .geodesic(true)
                        .color(getColorNum(Model.instance().getLifeStoryLinesColor())));
                showLifeStoryMethod(allEventsHere, event);
            }
        }
    }

    private void showFamilyTreeMethod(AllEvents allEventsHere, Event currentEvent) {
        if (Model.instance().getFilteredAllPersons() == null || Model.instance().getFilteredAllPersons().getAllPersonsArray() == null || Model.instance().getFilteredAllPersons().getAllPersonsArray().isEmpty()) {
            Model.instance().setFilteredAllPersons((AllPersons) Model.instance().getAllPersons());
        }

        Event userEarliestEvent = getEarliestEvent(Model.instance().getUser().getPersonId(), allEventsHere);
        ArrayList<String> parents = findParents(currentEvent.getPersonId(), Model.instance().getAllPersons());
        for (Person person : Model.instance().getFilteredAllPersons().getAllPersonsArray()) {
            if (Model.instance().getUser() != null && person.getMother() != null) {
                if (person.getPersonId().equals(Model.instance().getUser().getMother())) {
                    Event earliestEventMother = getEarliestEvent(person.getPersonId(), allEventsHere);
                    Polyline polyLine = mMap.addPolyline(new PolylineOptions()
                            .add(
                                    new LatLng(userEarliestEvent.getLatitude(), userEarliestEvent.getLongitude()),
                                    new LatLng(earliestEventMother.getLatitude(), earliestEventMother.getLongitude())
                            )
                            .width((float) fatherLineWidth)
                            .geodesic(true)
                            .color(getColorNum(Model.instance().getFamilyTreeLinesColor())));
                    motherLineWidth = motherLineWidth / 1.2;
                    findParentsRecursive(person, allEventsHere, motherLineWidth, earliestEventMother);
                }
            }
            if (Model.getUser() != null && person != null && person.getFather() != null) {
                if (person.getPersonId().equals(Model.getUser().getFather())) {
                    Event earliestEventFather = getEarliestEvent(person.getPersonId(), allEventsHere);
                    Polyline polyLine = mMap.addPolyline(new PolylineOptions()
                            .add(
                                    new LatLng(userEarliestEvent.getLatitude(), userEarliestEvent.getLongitude()),
                                    new LatLng(earliestEventFather.getLatitude(), earliestEventFather.getLongitude())
                            )
                            .width((float) fatherLineWidth)
                            .geodesic(true)
                            .color(getColorNum(Model.instance().getFamilyTreeLinesColor())));
                    fatherLineWidth = fatherLineWidth / 1.2;
                    findParentsRecursive(person, allEventsHere, fatherLineWidth, earliestEventFather);
                }
            }
        }
    }

    private void findParentsRecursive(Person person, AllEvents allEventsHere, double lineWidth, Event currentEvent) {
        for (Person parentPerson : Model.instance().getFilteredAllPersons().getAllPersonsArray()) {
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
                            .color(getColorNum(Model.instance().getFamilyTreeLinesColor())));
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
                            .color(getColorNum(Model.instance().getFamilyTreeLinesColor())));
                    lineWidth = lineWidth / 1.5;
                    findParentsRecursive(parentPerson, allEventsHere, lineWidth, earliestEventFather);

                }
            }
        }
    }

    private Event getEarliestEvent(String personId, AllEvents allEventsHere) {
        int earliestEventYear = 9999;
        Event earliestEvent = null;

        if (allEventsHere != null) {
            for (Event event : allEventsHere.getAllEvents()) {
                if (event.getPersonId().equals(personId) && event.getYear() < earliestEventYear) {
                    earliestEventYear = event.getYear();
                    earliestEvent = event;
                }
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

        for (int i = 0; i < Model.instance().getEventTypes().size(); i++) {
            eventTypeColors.put(Model.instance().getEventTypes().get(i).toLowerCase(), hexColors[i]);
        }
    }
}