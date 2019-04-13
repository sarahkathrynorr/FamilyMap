package com.example.familymapclient;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class FilterListFragment extends Fragment {
    private RecyclerView mEventRecyclerView;
    private FilterAdapter mAdapter;

    private ArrayList<String> filterEvents = new ArrayList<>();

    private ArrayList<String> allEvents = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_filter_list, container, false);


        mEventRecyclerView = view
                .findViewById(R.id.filter_recycler_view);
        mEventRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        setAllEvents();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setAllEvents();
    }


    public ArrayList<String> getFilterEvents() {
        return filterEvents;
    }

    public void setAllEvents() {

        if (mAdapter == null) {
            if (!allEvents.contains("Mother")) {
                allEvents.add("Mother");
                allEvents.add("Father");
                allEvents.add("Male");
                allEvents.add("Female");
            }
            mAdapter = new FilterAdapter(allEvents);
            mEventRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }


    private class FilterHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private String mEvent;

        private final TextView mTitleTextView;
        private final TextView mDescriptionTextView;
        private final Switch aSwitch;

        FilterHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_filter, parent, false));

            mTitleTextView = itemView.findViewById(R.id.event_title);
            mDescriptionTextView = itemView.findViewById(R.id.event_description);
            aSwitch = itemView.findViewById(R.id.filter_item_switch);
        }

        void bind(final String event) {
            mEvent = event;
            switch (mEvent) {
                case "Mother": {
                    String string = "Mother's Side";
                    mTitleTextView.setText(string);
                    String description = "FILTER BY MOTHER'S SIDE OF FAMILY";
                    mDescriptionTextView.setText(description);
                    break;
                }
                case "Father": {
                    String string = "Father's Side";
                    mTitleTextView.setText(string);
                    String description = "FILTER BY FATHER'S SIDE OF FAMILY";
                    mDescriptionTextView.setText(description);
                    break;
                }
                default: {
                    String string = mEvent + " Events";
                    mTitleTextView.setText(string);
                    String description = "FILTER BY " + event.toUpperCase() + " EVENTS";
                    mDescriptionTextView.setText(description);
                    break;
                }
            }

            aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        filterEvents.add(event);

                    } else {
                        filterEvents.remove(event);
                    }
                }
            });
        }

        @Override
        public void onClick(View v) {
            //TODO do something here ??
        }
    }

    private class FilterAdapter extends RecyclerView.Adapter<FilterHolder> {

        private final List<String> mEvents;

        FilterAdapter(List<String> events) {
            mEvents = events;
        }

        @Override
        public FilterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new FilterHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(FilterHolder holder, int position) {
            String event = mEvents.get(position);
            holder.bind(event);

        }

        @Override
        public int getItemCount() {
            return mEvents.size();
        }
    }
}
