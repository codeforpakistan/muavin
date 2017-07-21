package com.cfp.muaavin.facebook;

import java.util.ArrayList;

/**
 *
 */
public interface UserInterface {

    void getReportedFriends(ArrayList<String> Friends, String dataType);
    void getBlockedUsers(ArrayList<String> FacebookUserIds, ArrayList<String> TwitterUserIds);
}
