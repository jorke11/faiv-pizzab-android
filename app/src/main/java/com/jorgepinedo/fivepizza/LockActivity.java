package com.jorgepinedo.fivepizza;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;

import java.util.List;

public class LockActivity extends AppCompatActivity {

    PatternLockView mPatternLockView;
    String pattern_string="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);

        mPatternLockView = (PatternLockView) findViewById(R.id.pattern_lock_view);
        mPatternLockView.addPatternLockListener(mPatternLockViewListener);

    }

    private PatternLockViewListener mPatternLockViewListener = new PatternLockViewListener() {
        @Override
        public void onStarted() {
            Log.d("JORKE", "Pattern drawing started");
        }

        @Override
        public void onProgress(List<PatternLockView.Dot> progressPattern) {
            Log.d("JORKE", "Pattern progress: " + PatternLockUtils.patternToString(mPatternLockView, progressPattern));
        }

        @Override
        public void onComplete(List<PatternLockView.Dot> pattern) {
            pattern_string = PatternLockUtils.patternToString(mPatternLockView, pattern);

            //if(pattern.equals(pattern)){
                startActivity(new Intent(LockActivity.this,AdminActivity.class));
            //}
            Log.d("JORKE", "Pattern complete: " + PatternLockUtils.patternToString(mPatternLockView, pattern));

        }

        @Override
        public void onCleared() {
            Log.d("JORKE", "Pattern has been cleared");
        }
    };
}
