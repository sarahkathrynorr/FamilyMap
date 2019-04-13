package com.example.familymapclient;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import src.Model.Event;
import src.Model.Person;
import src.ServerProxy;

public class PersonActivity extends AppCompatActivity {
    private String personId;
    private Person currentPerson;
    private AllPersons allPersons;
    private Person mom;
    private Person dad;
    private Person spouse;
    private ArrayList<Person> children = new ArrayList<>();
    private ArrayList<Person> theWholeFam = new ArrayList<>();
    private AllEvents allEvents;
    private ArrayList<Event> indiviualsEvents = new ArrayList<>();

    private Drawable maleIcon;
    private Drawable femaleIcon;
    private Drawable locationIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        Iconify.with(new FontAwesomeModule());

        maleIcon = new IconDrawable(this, FontAwesomeIcons.fa_male).colorRes(R.color.orange).sizeDp(20);
        femaleIcon = new IconDrawable(this, FontAwesomeIcons.fa_female).colorRes(R.color.purple).sizeDp(20);
        locationIcon = new IconDrawable(this, FontAwesomeIcons.fa_map_marker).colorRes(R.color.colorAccent).sizeDp(20);


        allPersons = Model.instance().getAllPersons();
        allEvents = Model.instance().getAllEvents();
        personId = Model.instance().getPersonId();

        GetSinglePerson getSinglePerson = new GetSinglePerson();
        try {
            currentPerson = getSinglePerson.execute(personId).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        setFamily();
        setEvents();

        final ExpandableListView expandableListView = findViewById(R.id.expandableListView);

        expandableListView.setAdapter(new ExpandableListAdapter(indiviualsEvents, theWholeFam));


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP |
                    Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();


    }

    private class ExpandableListAdapter extends BaseExpandableListAdapter {

        private static final int EVENT_GROUP_POSITION = 0;
        private static final int FAMILY_GROUP_POSITION = 1;

        private final ArrayList<Event> lifeEvents;
        private final ArrayList<Person> familyPersons;

        ExpandableListAdapter(ArrayList<Event> lifeEvents, ArrayList<Person> familyPersons) {
            this.lifeEvents = lifeEvents;
            this.familyPersons = familyPersons;
        }

        @Override
        public int getGroupCount() {
            return 2;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            switch (groupPosition) {
                case EVENT_GROUP_POSITION:
                    return lifeEvents.size();
                case FAMILY_GROUP_POSITION:
                    return familyPersons.size();
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public Object getGroup(int groupPosition) {
            switch (groupPosition) {
                case EVENT_GROUP_POSITION:
                    return getString(R.string.topItemStringEvent);
                case FAMILY_GROUP_POSITION:
                    return getString(R.string.topItemStringFam);
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            switch (groupPosition) {
                case EVENT_GROUP_POSITION:
                    return lifeEvents.get(childPosition);
                case FAMILY_GROUP_POSITION:
                    return familyPersons.get(childPosition);
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.expandable_list_item, parent, false);
            }

            TextView titleView = convertView.findViewById(R.id.topItem);

            switch (groupPosition) {
                case EVENT_GROUP_POSITION:
                    titleView.setText(R.string.topItemStringEvent);
                    break;
                case FAMILY_GROUP_POSITION:
                    titleView.setText(R.string.topItemStringFam);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View itemView;

            switch (groupPosition) {
                case EVENT_GROUP_POSITION:
                    itemView = layoutInflater.inflate(R.layout.expandable_list_item, parent, false);
                    try {
                        initializeEventView(itemView, childPosition);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                case FAMILY_GROUP_POSITION:
                    itemView = layoutInflater.inflate(R.layout.expandable_list_item, parent, false);
                    initializeFamilyView(itemView, childPosition);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }

            return itemView;
        }

        private void initializeEventView(View eventsView, final int childPosition) throws ExecutionException, InterruptedException {
            TextView eventView = eventsView.findViewById(R.id.topItem);
            eventView.setCompoundDrawables(locationIcon, null, null, null);

            String lifeEventTitle = lifeEvents.get(childPosition).getEventType() + ": " + lifeEvents.get(childPosition).getCity() +
                    ", " + lifeEvents.get(childPosition).getCountry() + " (" + lifeEvents.get(childPosition).getYear() + ")";
            eventView.setText(lifeEventTitle);

            TextView personNameView = eventsView.findViewById(R.id.bottomItem);

            Person thePerson = null;
            for (Person person : allPersons.getAllPersonsArray()) {
                if (person.getPersonId().equals(lifeEvents.get(childPosition).getPersonId())) {
                    thePerson = person;
                    break;
                }
            }
            if (thePerson != null) {
                String personName = thePerson.getFirstName() + " " + thePerson.getLastName();
                personNameView.setText(personName);
            }

            personNameView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PersonActivity.this, MainActivity.class);
                    Model.instance().setGivenEventId(lifeEvents.get(childPosition).getEventID());
                    startActivity(intent);
                }
            });
        }

        private void initializeFamilyView(View familyView, final int childPosition) {

            TextView name = familyView.findViewById(R.id.topItem);
            if (familyPersons.get(childPosition) != null) {
                String personName = familyPersons.get(childPosition).getFirstName() + " " + familyPersons.get(childPosition).getLastName();
                name.setText(personName);

                if (familyPersons.get(childPosition).getGender().toUpperCase().equals("M")) {
                    name.setCompoundDrawables(maleIcon, null, null, null);
                } else {
                    name.setCompoundDrawables(femaleIcon, null, null, null);
                }

                TextView relationship = familyView.findViewById(R.id.bottomItem);
                String relationshipText = null;
                if (mom != null && familyPersons.get(childPosition).getPersonId().equals(mom.getPersonId())) {
                    relationshipText = "Mother";
                } else if (dad != null && familyPersons.get(childPosition).getPersonId().equals(dad.getPersonId())) {
                    relationshipText = "Father";
                } else if (!children.isEmpty() && children.contains(familyPersons.get(childPosition))) {
                    relationshipText = "Child";
                } else if (spouse != null &&
                        familyPersons.get(childPosition).getPersonId().equals(spouse.getPersonId())) {
                    relationshipText = "Spouse";
                }
                relationship.setText(relationshipText);
            }

            familyView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PersonActivity.this, PersonActivity.class);
                    Bundle bundie = new Bundle();

                    Model.instance().setPersonId(familyPersons.get(childPosition).getPersonId());
                    intent.putExtra("allPersons", allPersons);
                    intent.putExtra("allEvents", allEvents);
                    intent.putExtras(bundie);
                    startActivity(intent);
                }
            });
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    private void putPersonDetails() {
        TextView firstName = findViewById(R.id.personFirstName);
        TextView lastName = findViewById(R.id.personLastName);
        TextView gender = findViewById(R.id.personGender);

        firstName.setText(currentPerson.getFirstName());
        lastName.setText(currentPerson.getLastName());
        if (currentPerson.getGender().toUpperCase().equals("M")) {
            gender.setText("Male");
        } else if (currentPerson.getGender().toUpperCase().equals("F")) {
            gender.setText("Female");
        }
    }

    private class GetSinglePerson extends AsyncTask<String, Person, Person> {
        @Override
        protected Person doInBackground(String... strings) {
            ServerProxy serverProxy = new ServerProxy();
            serverProxy.setServerPort(Model.instance().getServerPort());
            serverProxy.setServerHost(Model.instance().getServerHost());

            currentPerson = serverProxy.getPerson(strings[0], Model.instance().getAuthToken()).getPerson();
            return currentPerson;
        }

        @Override
        protected void onPostExecute(Person person) {
            if (currentPerson != null) {
                putPersonDetails();
            } else {
                Toast.makeText(PersonActivity.this,
                        //create a new activity with person detail
                        "No person :(",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setFamily() {
        findChildren(currentPerson.getPersonId(), allPersons);
        for (Person person : allPersons.getAllPersonsArray()) {
            if (person != null && currentPerson != null) {
                if (person.getPersonId().equals(currentPerson.getMother())) {
                    mom = person;
                } else if (person.getPersonId().equals(currentPerson.getFather())) {
                    dad = person;
                } else if (currentPerson.getSpouse() != null && person.getPersonId().equals(currentPerson.getSpouse())) {
                    spouse = person;
                } else if (person.getMother() != null && person.getFather() != null &&
                        (person.getMother().equals(currentPerson.getPersonId()) || person.getFather().equals(currentPerson.getPersonId()))) {
                    children.add(person);
                }
            }
        }

        theWholeFam.add(mom);
        theWholeFam.add(dad);
        theWholeFam.add(spouse);
        theWholeFam.addAll(children);
    }

    private void setEvents() {
        allEvents.setAllEvents(sortOrderOfEvents(allEvents.getAllEvents()));
        for (Event event : allEvents.getAllEvents()) {
            if (currentPerson != null) {
                if (event.getPersonId().equals(currentPerson.getPersonId())) {
                    indiviualsEvents.add(event);
                }
            }
        }
    }

    public ArrayList<String> findChildren(String currentPersonId, AllPersons allPersons) {
        ArrayList<String> children = new ArrayList<>();

        for (Person person : allPersons.getAllPersonsArray()) {
            if (person.getFather() != null) {
                if (person.getFather().equals(currentPersonId)) {
                    children.add(person.getPersonId());
                }
            }
            if (person.getMother() != null) {
                if (person.getMother().equals(currentPersonId)) {
                    children.add(person.getPersonId());
                }
            }
        }
        return children;
    }

    public ArrayList<Event> sortOrderOfEvents(ArrayList<Event> personEvents) {
        ArrayList<Event> orderedEvents = new ArrayList<>();

        while(personEvents.toArray().length > 0) {
            Event earliest = getEarliestEvent(personEvents);
            orderedEvents.add(earliest);
            personEvents.remove(earliest);
        }
        return orderedEvents;
    }

    private Event getEarliestEvent(ArrayList<Event> personEvents) {
        int startingEventYear = 9999;
        Event earliestEvent = null;
        for (Event event : personEvents) {
            if (event.getYear() < startingEventYear) {
                startingEventYear = event.getYear();
                earliestEvent = event;
            }
        }
        return earliestEvent;
    }
}