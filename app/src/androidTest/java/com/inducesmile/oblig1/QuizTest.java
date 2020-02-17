package com.inducesmile.oblig1;

/* This test maneuver from MainActivity to QuizActivity and runs the Quiz. The task for this test
is to check whether the quizscore is correct or not. When the app runs for the first time there should
be a picture where the correct answer is Cartman. In this test, each question will be answered with the name
cartman. The score should therefore be 1 of number of questions.

 */


import android.app.Activity;

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

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
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
            //If the alertDialog appears, a username must be set
            //Set username should be something else if another language is set
            onView(withText("SET USERNAME")).perform(click());
            onView(withId(R.id.quiz)).perform(click());
        }
        QuizActivity activity = (QuizActivity) getActivityInstance();
        onView(withId(R.id.quiz_button)).perform(click());

        //loop through the quiz.
        for (int i = 0; i < MainActivity.quizData.size(); i++) {
            onView(withId(R.id.svar_editText)).perform(typeText("cartman"));
            onView(withId(R.id.svar_button)).perform(click());

            //The quizButton will be disabled after the last question. Without this if-sentence
            //the test will crash
            if (activity.findViewById(R.id.quiz_button).isEnabled()) {
                onView(withId(R.id.quiz_button)).perform(click());
            }
        }


        Truth.assertThat(activity.poeng).isEqualTo(1);


    }

    //This test crosses multiple activities. This method is used to get current activity
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
