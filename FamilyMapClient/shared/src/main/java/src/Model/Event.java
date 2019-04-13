package src.Model;

import java.io.Serializable;

/**
 * model class for Event object
 * includes getters and setters
 * contains eventID, descendant, personID, latitude, longitude, country, city, eventType, year(int)
 * except for year all are strings
 */
public class Event implements Serializable {
    /**
     * constructor for Event object type;
     *
     * @param eventID - String
     * @param descendant - String
     * @param personID - String
     * @param latitude - double
     * @param longitude - double
     * @param country - String
     * @param city - String
     * @param eventType - String
     * @param year - int
     */
    public Event(String eventID, String descendant, String personID, Double latitude, Double longitude, String country, String city, String eventType, int year) {
        this.eventID = eventID;
        this.personID = personID;
        this.descendant = descendant;
        this.city = city;
        this.country = country;
        this.eventType = eventType;
        this.latitude = latitude;
        this.longitude = longitude;
        this.year = year;
    }

    /**
     * eventID is of type string
     */
    private String eventID; //non empty string
    /**
     * descendant is of type string
     */
    private String descendant;
    /**
     * personID is of type string
     */
    private String personID;
    /**
     * latitude is of type string
     */
    private Double latitude;
    /**
     * longitude is of type string
     */
    private Double longitude;
    /**
     * country is of type string
     */
    private String country;
    /**
     * city is of type string
     */
    private String city;
    /**
     * eventType is of type string
     */
    private String eventType; //birth, baptism, marriage, etc...
    /**
     * year is of type int
     */
    private int year;

    public void setCity(String city) {
        this.city = city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public void setPersonId(String personID) {
        this.personID = personID;
    }

    public void setDescendant(String descendant) {
        this.descendant = descendant;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getYear() {
        return year;
    }

    public String getEventID() {
        return eventID;
    }

    public String getDescendant() {
        return descendant;
    }

    public String getPersonId() {
        return personID;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getEventType() {
        return eventType;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (o instanceof Event) {
            Event oEvent = (Event) o;
            return oEvent.getEventID().equals(getEventID()) &&
                    oEvent.getDescendant().equals(getDescendant()) &&
                    oEvent.getPersonId().equals(getPersonId()) &&
                    oEvent.getLatitude().equals(getLatitude()) &&
                    oEvent.getLongitude().equals(getLongitude()) &&
                    oEvent.getCountry().equals(getCountry()) &&
                    oEvent.getCity().equals(getCity()) &&
                    oEvent.getEventType().equals(getEventType()) &&
                    oEvent.getYear() == (getYear());
        }
        return false;
    }
}
