
package com.milton.common.demo.fragment;

import com.milton.common.demo.activity.imageloader.FrescoActivity;
import com.milton.common.demo.activity.imageloader.HttpLoaderActivity;
import com.milton.common.demo.activity.imageloader.UniversalImageLoaderActivity;
import com.milton.common.demo.activity.imageloader.VolleyActivity;

public class Fragment2ImageLoader extends Fragment2Base {
    public Class[] getItemClass() {
        return new Class[] {
                HttpLoaderActivity.class,
                UniversalImageLoaderActivity.class,
                VolleyActivity.class,
                FrescoActivity.class,
        };
    }

    public String[] getItemNames() {
        return new String[] {
                "HttpLoaderActivity",
                "UniversalImageLoaderActivity",
                "VolleyActivity",
                "FrescoActivity",
        };
    }

}
