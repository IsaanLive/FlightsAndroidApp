package com.aseanmobile.airmobile;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.aseanmobile.airmobile.R;

public class Splash extends Activity {

	Class<?> activityClass;
	@SuppressWarnings("rawtypes")
	Class[] paramTypes = { Integer.TYPE, Integer.TYPE };

	Method overrideAnimation = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

		try {
			activityClass = Class.forName("android.app.Activity");
			overrideAnimation = activityClass.getDeclaredMethod(
					"overridePendingTransition", paramTypes);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			
			public void run() {
				Intent i = new Intent(Splash.this,
						Webview.class);
				startActivity(i);
				finish();
				if (overrideAnimation != null) {
					try {
						overrideAnimation.invoke(Splash.this, android.R.anim.fade_in,
								android.R.anim.fade_out);
					} catch (IllegalArgumentException e) {
						
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						
						e.printStackTrace();
					}
				}
			}
			//Here you need to change the microsec with your suggess, remember 3000 = 3 sec.
		}, 3000);
	}
}