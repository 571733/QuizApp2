package com.inducesmile.oblig1;


import android.app.Activity;
import android.view.View;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.core.internal.deps.guava.collect.Iterables;
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

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

@RunWith(AndroidJUnit4.class)
public class QuizTest {
    @Rule
    public ActivityTestRule<MainActivity> quizRule = new ActivityTestRule<>(MainActivity.class);

    private QuizIdlingRes idlingRes = new QuizIdlingRes();

    @Before
    public void onBefore() {
        Intents.init();

    }

    @After
    public void onAfter() {
        Intents.release();
    }

    @Test
    public void quizTest() {
        IdlingRegistry.getInstance().register(idlingRes);

        try {
            onView(withId(R.id.quiz)).perform(click());
        } catch (NoMatchingViewException e) {
            onView(withText("Sett brukernavn")).perform(click());
            onView(withId(R.id.quiz)).perform(click());
        }
        QuizActivity activity = (QuizActivity) getActivityInstance();
        onView(withId(R.id.quiz_button)).perform(click());
        for (int i = 0; i < MainActivity.quizData.size(); i++) {
            onView(withId(R.id.svar_editText)).perform(typeText("cartman"));
            onView(withId(R.id.svar_button)).perform(click());
            if (activity.findViewById(R.id.quiz_button).isEnabled()) {
                onView(withId(R.id.quiz_button)).perform(click());
            }
        }


        Truth.assertThat(activity.poeng).isEqualTo(1);


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
}
