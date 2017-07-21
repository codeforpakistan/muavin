package com.cfp.muaavin.helper;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by KMajeed on 7/13/2017.
 */

public class Images {

    public Images(String path, boolean isSelected, String postId) {
        this.path = path;
        this.isSelected = isSelected;
        this.postId = postId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    String path;
    public boolean isSelected;

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    String postId;


}