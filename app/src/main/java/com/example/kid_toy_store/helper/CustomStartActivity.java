package com.example.kid_toy_store.helper;

import android.content.Context;
import android.content.Intent;

public class CustomStartActivity {

    public static  <T> void customStartActivity(Context context, Class<T> activity){
        Intent intent = new Intent(context, activity);
        context.startActivity(intent);
    }
}
