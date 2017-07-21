package com.cfp.muaavin.helper;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 *
 */
public class ImageHelper {

    public static String getEncodedImage(String image)
    {
        try {
            image = URLEncoder.encode(image, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return image;
    }

    public static String getDecodedImage(String image)
    {
        try {
            image = URLDecoder.decode(image, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return image;
    }

}
