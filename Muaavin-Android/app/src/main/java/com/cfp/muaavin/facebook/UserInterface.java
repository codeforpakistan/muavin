package com.cfp.muaavin.facebook;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 */
public interface UserInterface {

    void getReportedFriends(ArrayList<String> Friends, String dataType);
    void getBlockedUsers(ArrayList<String> FacebookUserIds, ArrayList<String> TwitterUserIds, HashMap<String, String> fbblockDates, HashMap<String,String> twblockDates);
}
