package src.RequestResponse;

import src.Model.User;

import java.util.ArrayList;

public class UsersRequest {
    private ArrayList<User> userArrayList;

    public ArrayList<User> getUserArrayList() {
        return userArrayList;
    }

    public void setUserArrayList(ArrayList<User> userArrayList) {
        this.userArrayList = userArrayList;
    }
}
