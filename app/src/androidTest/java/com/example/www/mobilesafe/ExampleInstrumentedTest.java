package com.example.www.mobilesafe;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.example.www.db.dao.BlackNumberDao;
import com.example.www.db.domain.BlackNumberInfo;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.example.www.mobilesafe", appContext.getPackageName());

        BlackNumberDao instance = BlackNumberDao.getInstance(appContext);
        instance.insert("110", "1");
        instance.insert("111", "1");
        instance.insert("112", "1");

        instance.delete("111");

        instance.update("112", "2");

        List<BlackNumberInfo> all = instance.findAll();
        Log.i("ExampleInstrumentedTest", "useAppContext: " + all);
    }
}
