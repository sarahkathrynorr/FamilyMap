package com.example.familymapclient;

import android.graphics.drawable.Drawable;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.HashMap;

import src.Model.Person;

public class Model {

    private static Model _instance;
    private static Person user;
    private static boolean showFamilyTreeLines;
    private String authToken;

    private GoogleMap mMap;
    private String serverPort;
    private String serverHost;
    private AllEvents allEvents;
    private AllEvents filteredAllEvents = new AllEvents();
    private AllPersons allPersons;
    private AllPersons filteredAllPersons = new AllPersons();
    private Person currentPerson;

    //private Person user;

    private boolean showSpouseLines = true;
    private String spouseLinesColor = "RED";

    private boolean showLifeStoryLines = true;
    private String lifeStoryLinesColor = "YELLOW";

    //private boolean showFamilyTreeLines = true;
    private String familyTreeLinesColor = "GREEN";


    private boolean showAllMarkers = true;
    private String givenEventId;
    private ArrayList<String> eventTypes = new ArrayList<>();
    private ArrayList<String> updatedEventTypes;

    private ArrayList<Marker> markers = new ArrayList<>();

    private String mapType = "normal";
    private String personId;


    public static Model instance() {
        if (_instance == null) {
            _instance = new Model();
        }
        return _instance;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public GoogleMap getmMap() {
        return mMap;
    }

    public void setmMap(GoogleMap mMap) {
        this.mMap = mMap;
    }

    public String getServerPort() {
        return serverPort;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }

    public String getServerHost() {
        return serverHost;
    }

    public void setServerHost(String serverHost) {
        this.serverHost = serverHost;
    }

    public AllEvents getAllEvents() {
        return allEvents;
    }

    public void setAllEvents(AllEvents allEvents) {
        this.allEvents = allEvents;
    }

    public AllEvents getFilteredAllEvents() {
        return filteredAllEvents;
    }

    public void setFilteredAllEvents(AllEvents filteredAllEvents) {
        this.filteredAllEvents = filteredAllEvents;
    }

    public AllPersons getAllPersons() {
        return allPersons;
    }

    public void setAllPersons(AllPersons allPersons) {
        this.allPersons = allPersons;
    }

    public AllPersons getFilteredAllPersons() {
        return filteredAllPersons;
    }

    public void setFilteredAllPersons(AllPersons filteredAllPersons) {
        this.filteredAllPersons = filteredAllPersons;
    }

    public Person getCurrentPerson() {
        return currentPerson;
    }

    public void setCurrentPerson(Person currentPerson) {
        this.currentPerson = currentPerson;
    }

    public static Person getUser() {
        return user;
    }

    public void setUser(Person user) {
        this.user = user;
    }

    public boolean isShowSpouseLines() {
        return showSpouseLines;
    }

    public void setShowSpouseLines(boolean showSpouseLines) {
        this.showSpouseLines = showSpouseLines;
    }

    public String getSpouseLinesColor() {
        return spouseLinesColor;
    }

    public void setSpouseLinesColor(String spouseLinesColor) {
        this.spouseLinesColor = spouseLinesColor;
    }

    public boolean isShowLifeStoryLines() {
        return showLifeStoryLines;
    }

    public void setShowLifeStoryLines(boolean showLifeStoryLines) {
        this.showLifeStoryLines = showLifeStoryLines;
    }

    public String getLifeStoryLinesColor() {
        return lifeStoryLinesColor;
    }

    public void setLifeStoryLinesColor(String lifeStoryLinesColor) {
        this.lifeStoryLinesColor = lifeStoryLinesColor;
    }

    public static boolean isShowFamilyTreeLines() {
        return showFamilyTreeLines;
    }

    public void setShowFamilyTreeLines(boolean showFamilyTreeLines) {
        this.showFamilyTreeLines = showFamilyTreeLines;
    }

    public String getFamilyTreeLinesColor() {
        return familyTreeLinesColor;
    }

    public void setFamilyTreeLinesColor(String familyTreeLinesColor) {
        this.familyTreeLinesColor = familyTreeLinesColor;
    }

    public boolean isShowAllMarkers() {
        return showAllMarkers;
    }

    public void setShowAllMarkers(boolean showAllMarkers) {
        this.showAllMarkers = showAllMarkers;
    }

    public String getGivenEventId() {
        return givenEventId;
    }

    public void setGivenEventId(String givenEventId) {
        this.givenEventId = givenEventId;
    }

    public ArrayList<String> getEventTypes() {
        return eventTypes;
    }

    public void setEventTypes(ArrayList<String> eventTypes) {
        this.eventTypes = eventTypes;
    }

    public ArrayList<String> getUpdatedEventTypes() {
        return updatedEventTypes;
    }

    public void setUpdatedEventTypes(ArrayList<String> updatedEventTypes) {
        this.updatedEventTypes = updatedEventTypes;
    }

    public String getMapType() {
        return mapType;
    }

    public void setMapType(String mapType) {
        this.mapType = mapType;
    }

    public ArrayList<Marker> getMarkers() {
        return markers;
    }

    public void setMarkers(ArrayList<Marker> markers) {
        this.markers = markers;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }
}
