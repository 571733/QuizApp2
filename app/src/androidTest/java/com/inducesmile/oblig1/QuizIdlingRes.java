package com.inducesmile.oblig1;

import androidx.test.espresso.IdlingResource;

public class QuizIdlingRes implements IdlingResource {
    private ResourceCallback callback;

    @Override
    public String getName() {
        return QuizIdlingRes.class.getName();
    }

    @Override
    public boolean isIdleNow() {
     if (MainActivity.isLoaded){
         callback.onTransitionToIdle();
         return true;
     }else{
         return false;
     }
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        this.callback = callback;
    }
}
