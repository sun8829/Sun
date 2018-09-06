package com.ody.ody.app;

import com.github.mzule.activityrouter.annotation.Modules;
import com.ody.library.base.BaseApplication;

import java.io.File;

import io.reactivex.functions.Function;

/**
 * Created by Samel on 2017/5/4.
 */
@Modules({"app", "login"})
public class OdyApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    Function function = new Function<File, String>() {

        @Override
        public String apply(File file) throws Exception {
            return null;
        }
    };
}
