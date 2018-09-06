package com.github.mzule.activityrouter.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Samuel on 30/10/2016.
 */
@Retention(RetentionPolicy.CLASS)
public @interface Modules {
    String[] value();
}
