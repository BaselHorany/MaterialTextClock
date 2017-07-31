/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.basel.materialtextclock;


import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.AttributeSet;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import static android.widget.RemoteViews.*;


@RemoteView
public class MaterialTextClock extends TextView {

    private String numformats,aColor;
    static Locale local;
    float aSize;
    private boolean mAttached;
    private Calendar mTime;
    private String mTimeZone;
    private CharSequence mFormat;
    SimpleDateFormat syf;
    SpannableStringBuilder sb;

    private final ContentObserver mFormatChangeObserver = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange) {
            onTimeChanged();
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            onTimeChanged();
        }
    };

    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mTimeZone == null && Intent.ACTION_TIMEZONE_CHANGED.equals(intent.getAction())) {
                final String timeZone = intent.getStringExtra("time-zone");
                createTime(timeZone);
            }
            onTimeChanged();
        }
    };

    private final Runnable mTicker = new Runnable() {
        public void run() {
            onTimeChanged();

            long now = SystemClock.uptimeMillis();
            long next = now + (1000 - now % 1000);

            getHandler().postAtTime(mTicker, next);
        }
    };

    @SuppressWarnings("UnusedDeclaration")
    public MaterialTextClock(Context context) {
        super(context);
        init();
    }

    @SuppressWarnings("UnusedDeclaration")
    public MaterialTextClock(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MaterialTextClock(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mFormat = "hh:mm a";
        aSize = 0.5f;
        aColor = "#000000";
        numformats = "en";
        if(numformats.equals("ar")){
            local = new Locale("ar");
        }else{
            local = Locale.ENGLISH;
        }
        syf = new SimpleDateFormat("hh:mm a", local);
        createTime(mTimeZone);
    }

    private void createTime(String timeZone) {
        if (timeZone != null) {
            mTime = Calendar.getInstance(TimeZone.getTimeZone(timeZone));
        } else {
            mTime = Calendar.getInstance();
        }

    }


    @RemotableViewMethod
    public void setLang(String lang) {
        numformats = lang;
        if(numformats.equals("ar")){
            local = new Locale("ar");
        }else{
            local = Locale.ENGLISH;
        }
        syf = new SimpleDateFormat("hh:mm a", local);
        onTimeChanged();
    }

    @RemotableViewMethod
    public void setSize(Float size) {
        aSize = size;
        onTimeChanged();
    }

    @RemotableViewMethod
    public void setColor(String color) {
        aColor = color;
        onTimeChanged();
    }

    public String getTimeZone() {
        return mTimeZone;
    }

    @RemotableViewMethod
    public void setTimeZone(String timeZone) {
        mTimeZone = timeZone;
        createTime(timeZone);
        onTimeChanged();
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (!mAttached) {
            mAttached = true;

            registerReceiver();
            registerObserver();

            createTime(mTimeZone);
            onTimeChanged();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if (mAttached) {
            unregisterReceiver();
            unregisterObserver();

            getHandler().removeCallbacks(mTicker);

            mAttached = false;
        }
    }

    private void registerReceiver() {
        final IntentFilter filter = new IntentFilter();

        filter.addAction(Intent.ACTION_TIME_TICK);
        filter.addAction(Intent.ACTION_TIME_CHANGED);
        filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);

        getContext().registerReceiver(mIntentReceiver, filter, null, getHandler());
    }

    private void registerObserver() {
        final ContentResolver resolver = getContext().getContentResolver();
        resolver.registerContentObserver(Settings.System.CONTENT_URI, true, mFormatChangeObserver);
    }

    private void unregisterReceiver() {
        getContext().unregisterReceiver(mIntentReceiver);
    }

    private void unregisterObserver() {
        final ContentResolver resolver = getContext().getContentResolver();
        resolver.unregisterContentObserver(mFormatChangeObserver);
    }

    private void onTimeChanged() {
        mTime.setTimeInMillis(System.currentTimeMillis());
        Date sd = new Date();
        if(numformats.equals("ar")){
            setText(DecorAr(syf.format(sd)));
        }else{
            setText(DecorEn(syf.format(sd)));
        }
    }


    public Spannable DecorAr(String value) {
        sb = new SpannableStringBuilder(value);
        sb.setSpan(new RelativeSizeSpan(aSize), 5, 7, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        sb.setSpan(new ForegroundColorSpan(Color.parseColor(aColor)), 5, 7, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return sb;
    }


    public Spannable DecorEn(String value) {
        sb = new SpannableStringBuilder(value);
        sb.setSpan(new RelativeSizeSpan(aSize), 5, 8, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        sb.setSpan(new ForegroundColorSpan(Color.parseColor(aColor)), 5, 8, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return sb;
    }
}
