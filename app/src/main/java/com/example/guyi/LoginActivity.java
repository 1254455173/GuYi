package com.example.guyi;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;


public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        closeAndroidPDialog();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        final Button login_btn = (Button) findViewById(R.id.login);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cx=login_btn.getWidth()/2;
                int cy=login_btn.getHeight()/2;
                float radius=login_btn.getWidth();
                Animator anim= ViewAnimationUtils.createCircularReveal(login_btn,cx,cy,radius,0);
                anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        //login_btn.setVisibility(View.INVISIBLE);
                    }
                });
                anim.start();
                //login_btn.setVisibility(View.VISIBLE);
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


       final TextView register_text= (TextView) findViewById(R.id.login_register);
        register_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cx=register_text.getWidth()/2;
                int cy=register_text.getHeight()/2;
                float radius=register_text.getWidth();
                Animator anim= ViewAnimationUtils.createCircularReveal(register_text,cx,cy,radius,0);
                anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        register_text.setVisibility(View.INVISIBLE);
                    }
                });
                anim.start();
                //login_btn.setVisibility(View.VISIBLE);
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        // 动态展示头像
        showUserPhoto();

    }
    private void closeAndroidPDialog(){
        try {
            Class aClass = Class.forName("android.content.pm.PackageParser$Package");
            Constructor declaredConstructor = aClass.getDeclaredConstructor(String.class);
            declaredConstructor.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Class cls = Class.forName("android.app.ActivityThread");
            Method declaredMethod = cls.getDeclaredMethod("currentActivityThread");
            declaredMethod.setAccessible(true);
            Object activityThread = declaredMethod.invoke(null);
            Field mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown");
            mHiddenApiWarningShown.setAccessible(true);
            mHiddenApiWarningShown.setBoolean(activityThread, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    protected void onResume(){
        super.onResume();
        showUserPhoto();

    }

    public void showUserPhoto(){  // 动态展示头像
        String imagePath;
        ImageView picture = (ImageView) findViewById(R.id.userPhoto);
        Log.d("LoginActivity ImagePath", RegisterActivity.IMAGE_PATH+"");
        if(RegisterActivity.IMAGE_PATH != null){
            imagePath = RegisterActivity.IMAGE_PATH;
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            picture.setImageBitmap(bitmap);
        }

    }
}
