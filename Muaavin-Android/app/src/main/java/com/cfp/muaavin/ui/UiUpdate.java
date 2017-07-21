package com.cfp.muaavin.ui;

import com.cfp.muaavin.facebook.User;

import java.util.ArrayList;

/**
 * Created by Tooba Saeed on 03/03/2017.
 */

public interface UiUpdate {

    public void updateUi(ArrayList<User> Users, ArrayList<String> InfringingUserIds,String dataType);

}
