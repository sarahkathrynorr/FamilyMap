package src.Services;

import src.DAO.*;
import src.Model.*;
import src.RequestResponse.FillResponse;
import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

/**
 * services for filling in the tables with all the information given
 */
public class FillServices {
    private Locations locations;
    private LastNames lastNames;
    private FemaleNames femaleNames;
    private MaleNames maleNames;

    private String userName;

    private double baseLat = 46.2333;
    private double baseLong = -62.85;
    private String baseCountry = "Canada";
    private String baseCity = "Charlottetown";

    private int counter = 0;

    //default should be 4 generations
    private int generationAmount;

    private ArrayList<Person> newPersons = new ArrayList<>();
    private ArrayList<Event> newEvents = new ArrayList<>();


    public FillResponse fill(String inputUserName, int generationAmount) throws DataAccessException {
        String returnMessage;
        userName = inputUserName;
        this.generationAmount = generationAmount;

        if (generateDataFromJson()) {

            //start database
            Database myDb = new Database();
            myDb.openConnection();
            UserDatabaseAccess usersDAO = new UserDatabaseAccess(myDb.getConn());
            EventsDatabaseAccess eventsDAO = new EventsDatabaseAccess(myDb.getConn());
            PersonDatabaseAccess personsDAO = new PersonDatabaseAccess(myDb.getConn());

            User currentUser;
            try {
                currentUser = usersDAO.getUser(userName);
            } catch (DataAccessException e) {
                myDb.closeConnection(false);
                return new FillResponse(e.getMessage());
            }


            //clean out previous data associated with user
            if (currentUser != null) {
                try {
                    eventsDAO.removeAllEvents(userName);
                    personsDAO.removeAllPersons(userName);
                } catch (DataAccessException e) {
                    e.printStackTrace();
                    myDb.closeConnection(false);

                    return new FillResponse(e.getMessage());
                }

                //generate and fill in new data for user
                String country = baseCountry;
                double longitude = baseLong;
                double latitude = baseLat;
                String city = baseCity;

                int generation = 0;
                int defaultBirthYear = 1997;
                String[] parentsIDs = addGeneration(generation, currentUser.getLastName(), new Integer(defaultBirthYear));

                Person currentPerson = new Person(currentUser.getPersonId(), currentUser.getUsername(), currentUser.getFirstName(),
                        currentUser.getLastName(), currentUser.getGender(), parentsIDs[1], parentsIDs[0], null);

                String eventID = generateEventID();

                newEvents.add(new Event(eventID, currentUser.getUsername(), currentPerson.getPersonId(), latitude, longitude, country, city, "Birth", defaultBirthYear));

                newPersons.add(currentPerson);

                int amountEvents = newEvents.size();
                int amountPersons = newPersons.size();

                try {
                    for (Person person : newPersons) {
                        personsDAO.addPerson(person);
                    }

                    for (Event event : newEvents) {
                        eventsDAO.addEvent(event);
                    }
                } catch (DataAccessException e) {
                    e.printStackTrace();
                    myDb.closeConnection(false);

                    return new FillResponse(e.getMessage());
                }

                returnMessage = "Successfully added " + amountPersons + " persons and " + amountEvents + " events to the database.";

                myDb.closeConnection(true);

                return new FillResponse(returnMessage);

            } else {
                myDb.closeConnection(false);
                return new FillResponse("User not found in database");
            }
        } else {
            return new FillResponse("Error reading Json files");
        }
    }

    private boolean generateDataFromJson() {
        try {
            //get the desired Json
            Gson gson = new Gson();

            Reader reader = new FileReader("server/src/main/java/src/Json/locations.json");
            locations = gson.fromJson(reader, Locations.class);
            reader = new FileReader("server/src/main/java/src/Json/fnames.json");
            femaleNames = gson.fromJson(reader, FemaleNames.class);
            reader = new FileReader("server/src/main/java/src/Json/mnames.json");
            maleNames = gson.fromJson(reader, MaleNames.class);
            reader = new FileReader("server/src/main/java/src/Json/snames.json");
            lastNames = gson.fromJson(reader, LastNames.class);
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    //return the array with [MotherID, FatherID]
    private String[] addGeneration(int generation, String fLastName, int birthYear) {
        if (generation < generationAmount) {
            ++generation;

            /* FATHER PERSON */
            int fBirthYear = birthYear;
            fBirthYear -= 23;
            String fFirstName = generateMaleFirstName();
            //lastName of father is parameter
            String fPersonId = generatePersonID();
            String fGender = "M";
            String fDescendant = userName;



            String[] fParentIds = addGeneration(generation, fLastName, fBirthYear);

            String fMotherId = fParentIds[0];
            String fFatherId = fParentIds[1];

            /* MOTHER PERSON */
            int mBirthYear = birthYear;
            mBirthYear -= 21;
            String mFirstName = generateFemaleFirstName();
            String mLastName = generateLastName();
            String mPersonId = generatePersonID();
            String mGender = "F";
            String mDescendant = userName;

            String[] mParentIds = addGeneration(generation, mLastName, mBirthYear);

            String mMotherId = mParentIds[0];
            String mFatherId = mParentIds[1];


            /* CREATE PERSON OBJECTS */
            Person newFather = new Person(fPersonId, fDescendant, fFirstName, fLastName, fGender, fFatherId, fMotherId, mPersonId);
            addEvents(fPersonId, fBirthYear);

            Person newMother = new Person(mPersonId, mDescendant, mFirstName, mLastName, mGender, mFatherId, mMotherId, fPersonId);
            addEvents(mPersonId, mBirthYear);


            addMarriageEvent(fPersonId, mPersonId, (mBirthYear + 19), generation);
            newPersons.add(newFather);
            newPersons.add(newMother);

            //Send Back ID's to create
            return new String[]{mPersonId, fPersonId};
        }
        else {
            return new String[]{null, null};
        }
    }

    private void addMarriageEvent(String husbandPersonID, String wifePersonID, int year, int counter) {
        //MARRIAGE

        String country;
        String city;
        double latitude;
        double longitude;

        String eventID = generateEventID();
        if (counter%2 == 0) {
            Random random = new Random();
            int index = random.nextInt(locations.getData().length - 1);

            SingleLocation singleLocation = locations.getData()[index];

            country = singleLocation.getCountry();
            city = singleLocation.getCity();
            latitude = singleLocation.getLatitude();
            longitude = singleLocation.getLongitude();
        }
        else {
            country = baseCountry;
            city = baseCity;
            latitude = baseLat;
            longitude = baseLong;
        }

        String eventType = "Marriage";

        Event hEvent = new Event(eventID, userName, husbandPersonID, latitude, longitude, country, city, eventType, year);
        newEvents.add(hEvent);

        eventID = generateEventID();
        Event wEvent = new Event(eventID, userName, wifePersonID, latitude, longitude, country, city, eventType, year);
        newEvents.add(wEvent);
    }

    private String generateLastName() {
        Random random = new Random();
        int index = random.nextInt(lastNames.getData().length - 1);

        return lastNames.getData()[index];
    }

    private String generateFemaleFirstName() {

        Random random = new Random();
        int index = random.nextInt(femaleNames.getData().length - 1);

        return femaleNames.getData()[index];
    }

    private String generateMaleFirstName() {

        Random random = new Random();
        int index = random.nextInt(maleNames.getData().length - 1);

        return maleNames.getData()[index];
    }

    private String generatePersonID() {
        return UUID.randomUUID().toString();
    }

    private void addEvents(String personID, int birthYear) {

        String descendant = userName;
        //personID is a parameter

        //BIRTH
        String eventID = generateEventID();

        String country = baseCountry;
        double longitude = baseLong;
        double latitude = baseLat;
        String city = baseCity;
        if (counter%3 == 0) {
            Random random = new Random();
            int index = random.nextInt(locations.getData().length - 1);

            SingleLocation singleLocation = locations.getData()[index];

            country = singleLocation.getCountry();
            city = singleLocation.getCity();
            latitude = singleLocation.getLatitude();
            longitude = singleLocation.getLongitude();
        }
        counter++;

        String eventType = "Birth";
        int year = birthYear;

        Event event = new Event(eventID, descendant, personID, latitude, longitude, country, city, eventType, year);
        newEvents.add(event);

        baseLat = latitude;
        baseCity = city;
        baseCountry = country;
        baseLong = longitude;

        //BAPTISM
        eventID = generateEventID();
        if (counter%5 == 0) {
            Random random = new Random();
            int index = random.nextInt(locations.getData().length - 1);

            SingleLocation singleLocation = locations.getData()[index];

            country = singleLocation.getCountry();
            city = singleLocation.getCity();
            latitude = singleLocation.getLatitude();
            longitude = singleLocation.getLongitude();
        }
        counter++;

        eventType = "Baptism";
        year = birthYear + 8;

        event = new Event(eventID, descendant, personID, latitude, longitude, country, city, eventType, year);
        newEvents.add(event);

    }

    private String generateEventID() {
        return UUID.randomUUID().toString();
    }

}
