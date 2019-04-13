package com.example.familymapclient;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import src.Model.Event;
import src.RequestResponse.EventResponse;
import src.RequestResponse.EventsResponse;
import src.RequestResponse.PersonResponse;
import src.RequestResponse.PersonsResponse;
import src.RequestResponse.RequestLogin;
import src.RequestResponse.ResponseLogin;
import src.RequestResponse.UserRequest;
import src.RequestResponse.UserResponse;
import src.ServerProxy;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ServerProxyTest {
    private ServerProxy serverProxy;
    private String authToken;
    private Event firstEvent;
    private String username = "test";
    private String password = "test";


    @Before
    public void doBefore() {
        serverProxy = new ServerProxy();
        serverProxy.setServerHost("10.24.197.26");  //TODO change if you need to!!
        serverProxy.setServerPort("8081");

        UserRequest userRequest = new UserRequest(username, password, "test", "test", "test", "f");
        authToken = serverProxy.register(userRequest).getAuthToken();
        //to fill the database for tests

        EventsResponse eventsResponse = serverProxy.getEvents(authToken);
        firstEvent = eventsResponse.getEventsArray().get(0);
    }

    //Get all events tests
   @Test
    public void testGetAllEventsGood() {
        EventsResponse eventsResponse = serverProxy.getEvents(authToken);
        assertNotNull(eventsResponse.getEventsArray());
        assertNull(eventsResponse.getMessage());
   }
   @Test
    public void testGetAllEventsBad() {
        //testing a bad authToken (a.k.a user doesn't exist)
       EventsResponse eventsResponse = serverProxy.getEvents("bad authToken");
       assertEquals("Authorization Token not found", eventsResponse.getMessage());
   }

   //Get all persons tests
   @Test
   public void testGetAllPersonsGood() {
       PersonsResponse personsResponse = serverProxy.getPersons(authToken);
       assertNotNull(personsResponse.getPersonsArray());
       assertNull(personsResponse.getMessage());
   }
   @Test
   public void testGetAllPersonsBad() {
       //testing a bad authToken (a.k.a user doesn't exist)
       PersonsResponse personsResponse = serverProxy.getPersons("bad authToken");
       assertEquals("Authorization Token not found", personsResponse.getMessage());
   }

   //Get single person tests
   @Test
   public void testGetEventGood() {
        EventResponse eventResponse = serverProxy.getSingleEvent(firstEvent.getEventID(), authToken);
        assertNotNull(eventResponse);
        assertNull(eventResponse.getMessage());
   }
   @Test
   public void testGetEventBad() {
        //testing an event that isn't associated with the user
       EventResponse eventResponse = serverProxy.getSingleEvent("not a good id", authToken);
       assertEquals("Event not not found in database for test", eventResponse.getMessage());
   }

   //Get single event tests
   @Test
   public void testGetPersonGood() {
       PersonResponse personResponse = serverProxy.getPerson(firstEvent.getPersonId(), authToken);
       assertNotNull(personResponse);
       assertNull(personResponse.getMessage());
   }
   @Test
   public void testGetPersonBad() {
       //testing an person that isn't associated with the user
       PersonResponse personResponse = serverProxy.getPerson("not a good id", authToken);
       assertNull(personResponse.getPerson());
   }

   //Login tests
   @Test
    public void testLoginGood() {
       RequestLogin requestLogin = new RequestLogin(username, password);
        ResponseLogin responseLogin = serverProxy.login(requestLogin);
        assertNotNull(requestLogin);
        assertNull(responseLogin.getMessage());
   }
   @Test
    public void testLoginBad() {
        //testing a bad username
       RequestLogin requestLogin = new RequestLogin("bad username", password);
       ResponseLogin responseLogin = serverProxy.login(requestLogin);
       assertEquals("Username or Password is incorrect", responseLogin.getMessage());
   }

   //Register tests
   @Test
    public void testRegisterGood() {
        UserRequest userRequest = new UserRequest("test1","test1","test1","test1","test1","m");
        UserResponse userResponse = serverProxy.register(userRequest);
        assertNotNull(userResponse);
        assertNull(userResponse.getMessage());

   }
   @Test
    public void testRegisterBad() {
        //testing a username that is already in the database
       UserRequest userRequest = new UserRequest(username,"test1","test1","test1","test1","m");
       UserResponse userResponse = serverProxy.register(userRequest);
       assertEquals("[SQLITE_CONSTRAINT]  Abort due to constraint violation (column Username is not unique)", userResponse.getMessage());

   }

   @Test
   public void testClearGood() {
        assertTrue(serverProxy.clear());
   }

   @After
    public void cleanUp() {
        serverProxy.clear();
   }
}
