package com.inducesmile.oblig1;


import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;

import androidx.test.annotation.UiThreadTest;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.intent.Intents;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import androidx.test.runner.lifecycle.Stage;

import com.google.common.truth.Truth;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collection;
import java.util.Iterator;

import static android.app.Activity.RESULT_OK;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

@RunWith(AndroidJUnit4.class)
public class AddTest {

    @Rule
    public ActivityTestRule<MainActivity> quizRule = new ActivityTestRule<>(MainActivity.class);

    private QuizIdlingRes idlingRes = new QuizIdlingRes();

    @Before
    public void onBefore() {
        Intents.init();

        Instrumentation.ActivityResult result = createImageStub();
        Intents.intending(hasAction(MediaStore.ACTION_IMAGE_CAPTURE)).respondWith(result);

    }

    @After
    public void onAfter() {
        Intents.release();
    }

    @Test
    public void quizTest() {
        IdlingRegistry.getInstance().register(idlingRes);
        int sizeBefore = MainActivity.quizData.size();
        try {
            onView(withId(R.id.add)).perform(click());
        } catch (NoMatchingViewException e) {
            onView(withText("Sett brukernavn")).perform(click());
            onView(withId(R.id.add)).perform(click());
        }
        AddActivity activity = (AddActivity) getActivityInstance();

        onView(withId(R.id.button_camera)).perform(click());
       onView(withId(R.id.save_editText)).perform(typeText("test"));
        onView(withId(R.id.button_Save)).perform(click());

        int sizeAfter = MainActivity.quizData.size();

        Truth.assertThat(sizeBefore+1).isEqualTo(sizeAfter);

    }

    private Activity getActivityInstance() {
        final Activity[] currentActivity = {null};

        getInstrumentation().runOnMainSync(new Runnable() {
            public void run() {
                Collection<Activity> resumedActivity = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED);
                Iterator<Activity> it = resumedActivity.iterator();
                currentActivity[0] = it.next();
            }
        });

        return currentActivity[0];
    }

    private Instrumentation.ActivityResult createImageStub(){
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", BitmapFactory.decodeResource(quizRule.getActivity().getResources(), R.drawable.barttest));
        Intent resultData = new Intent();
        resultData.putExtras(bundle);
        return new Instrumentation.ActivityResult(RESULT_OK, resultData);
    }

}
