/*
 * Copyright (C) 2012 The LiquidSmooth Project
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

package com.android.settings.liquid;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.util.Log;
import android.util.TypedValue;

import java.net.URISyntaxException;

import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.R;
import com.android.settings.util.ShortcutPickerHelper;
import com.android.settings.widgets.NavBarItemPreference;

public class NavRingTargets extends SettingsPreferenceFragment 
        implements ShortcutPickerHelper.OnPickListener, OnPreferenceChangeListener {

    public static final int NAVRING_ONE = 1;
    public static final int NAVRING_TWO = 2;
    public static final int NAVRING_THREE = 3;
    public static final int NAVRING_FOUR = 4;
    public static final int NAVRING_FIVE = 5;

    private ShortcutPickerHelper mPicker;
    private Preference mPreference;
    private String mString;

    private int mNavRingAmount;

    NavBarItemPreference mRing1;
    NavBarItemPreference mRing2;
    NavBarItemPreference mRing3;
    NavBarItemPreference mRing4;
    NavBarItemPreference mRing5;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.navbar_ring_cat);
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.prefs_navring);

        PreferenceScreen prefs = getPreferenceScreen();

        mPicker = new ShortcutPickerHelper(this, this);

        String target3 = Settings.System.getString(mContext.getContentResolver(), Settings.System.SYSTEMUI_NAVRING_3);
        if (target3 == null || target3.equals("")) {
            Settings.System.putString(mContext.getContentResolver(), Settings.System.SYSTEMUI_NAVRING_3, "assist");
        }

        mRing1 = (NavBarItemPreference) findPreference("interface_navring_1_release");
        mRing1.setOnPreferenceChangeListener(this);
        mRing1.setSummary(getProperSummary(mRing1));
        mRing1.setIcon(resize(getNavbarIconImage(mRing1)));

        mRing2 = (NavBarItemPreference) findPreference("interface_navring_2_release");
        mRing2.setOnPreferenceChangeListener(this);
        mRing2.setSummary(getProperSummary(mRing2));
        mRing2.setIcon(resize(getNavbarIconImage(mRing2)));

        mRing3 = (NavBarItemPreference) findPreference("interface_navring_3_release");
        mRing3.setOnPreferenceChangeListener(this);
        mRing3.setSummary(getProperSummary(mRing3));
        mRing3.setIcon(resize(getNavbarIconImage(mRing3)));

        mRing4 = (NavBarItemPreference) findPreference("interface_navring_4_release");
        mRing4.setOnPreferenceChangeListener(this);
        mRing4.setSummary(getProperSummary(mRing4));
        mRing4.setIcon(resize(getNavbarIconImage(mRing4)));

        mRing5 = (NavBarItemPreference) findPreference("interface_navring_5_release");
        mRing5.setOnPreferenceChangeListener(this);
        mRing5.setSummary(getProperSummary(mRing5));
        mRing5.setIcon(resize(getNavbarIconImage(mRing5)));

        mNavRingAmount = Settings.System.getInt(mContext.getContentResolver(),
                         Settings.System.SYSTEMUI_NAVRING_AMOUNT, 1);

        switch (mNavRingAmount) {
        case NAVRING_ONE:
            prefs.removePreference(mRing2);
            prefs.removePreference(mRing3);
            prefs.removePreference(mRing4);
            prefs.removePreference(mRing5);
        case NAVRING_TWO:
            prefs.removePreference(mRing3);
            prefs.removePreference(mRing4);
            prefs.removePreference(mRing5);
        case NAVRING_THREE:
            prefs.removePreference(mRing4);
            prefs.removePreference(mRing5);
        case NAVRING_FOUR:
            prefs.removePreference(mRing5);
        default:
            //leave them all
        }

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        boolean result = false;

        if (preference == mRing1) {
            mPreference = preference;
            mString = Settings.System.SYSTEMUI_NAVRING_1;
            if (newValue.equals("app")) {
             mPicker.pickShortcut();
            } else {
            result = Settings.System.putString(getContentResolver(),
                       Settings.System.SYSTEMUI_NAVRING_1, (String) newValue);
            mRing1.setSummary(getProperSummary(mRing1));
            mRing1.setIcon(resize(getNavbarIconImage(mRing1)));
            }
        } else if (preference == mRing2) {
            mPreference = preference;
            mString = Settings.System.SYSTEMUI_NAVRING_2;
            if (newValue.equals("app")) {
             mPicker.pickShortcut();
            } else {
            result = Settings.System.putString(getContentResolver(),
                       Settings.System.SYSTEMUI_NAVRING_2, (String) newValue);
            mRing2.setSummary(getProperSummary(mRing2));
            mRing2.setIcon(resize(getNavbarIconImage(mRing2)));
            }
        } else if (preference == mRing3) {
            mPreference = preference;
            mString = Settings.System.SYSTEMUI_NAVRING_3;
            if (newValue.equals("app")) {
             mPicker.pickShortcut();
            } else {
            result = Settings.System.putString(getContentResolver(),
                       Settings.System.SYSTEMUI_NAVRING_3, (String) newValue);
            mRing3.setSummary(getProperSummary(mRing3));
            mRing3.setIcon(resize(getNavbarIconImage(mRing3)));
            }
        } else if (preference == mRing4) {
            mPreference = preference;
            mString = Settings.System.SYSTEMUI_NAVRING_4;
            if (newValue.equals("app")) {
             mPicker.pickShortcut();
            } else {
            result = Settings.System.putString(getContentResolver(),
                       Settings.System.SYSTEMUI_NAVRING_4, (String) newValue);
            mRing4.setSummary(getProperSummary(mRing4));
            mRing4.setIcon(resize(getNavbarIconImage(mRing4)));
            }
        } else if (preference == mRing5) {
            mPreference = preference;
            mString = Settings.System.SYSTEMUI_NAVRING_5;
            if (newValue.equals("app")) {
             mPicker.pickShortcut();
            } else {
            result = Settings.System.putString(getContentResolver(),
                       Settings.System.SYSTEMUI_NAVRING_5, (String) newValue);
            mRing5.setSummary(getProperSummary(mRing5));
            mRing5.setIcon(resize(getNavbarIconImage(mRing5)));
            }
        }
        return result;
    }

    public void shortcutPicked(String uri, String friendlyName, Bitmap bmp, boolean isApplication) {
          mPreference.setSummary(friendlyName);
          Settings.System.putString(getContentResolver(), mString, (String) uri);
          mPreference.setIcon(resize(getNavbarIconImage(mPreference)));
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == ShortcutPickerHelper.REQUEST_PICK_SHORTCUT
                    || requestCode == ShortcutPickerHelper.REQUEST_PICK_APPLICATION
                    || requestCode == ShortcutPickerHelper.REQUEST_CREATE_SHORTCUT) {
                mPicker.onActivityResult(requestCode, resultCode, data);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String getProperSummary(Preference preference) {
        if (preference == mRing1) {
            mString = Settings.System.SYSTEMUI_NAVRING_1;
        } else if (preference == mRing2) {
            mString = Settings.System.SYSTEMUI_NAVRING_2;
        } else if (preference == mRing3) {
            mString = Settings.System.SYSTEMUI_NAVRING_3;
        } else if (preference == mRing4) {
            mString = Settings.System.SYSTEMUI_NAVRING_4;
        } else if (preference == mRing5) {
            mString = Settings.System.SYSTEMUI_NAVRING_5;
        }

        String uri = Settings.System.getString(getActivity().getContentResolver(),mString);
        String empty = "none";

        if (uri == null)
            return empty;

        if (uri.equals("none")) {
                return getResources().getString(R.string.none);
        } else if (uri.equals("screenshot")) {
                return getResources().getString(R.string.interface_softkeys_screenshot_title);
        } else if (uri.equals("ime_switcher")) {
                return getResources().getString(R.string.interface_softkeys_ime_switcher_title);
        } else if (uri.equals("ring_vib")) {
                return getResources().getString(R.string.interface_softkeys_ring_vib_title);
        } else if (uri.equals("ring_silent")) {
                return getResources().getString(R.string.interface_softkeys_ring_silent_title);
        } else if (uri.equals("ring_vib_silent")) {
                return getResources().getString(R.string.interface_softkeys_ring_vib_silent_title);
        } else if (uri.equals("killcurrent")) {
                return getResources().getString(R.string.interface_softkeys_kill_process_title);
        } else if (uri.equals("screenoff")) {
                return getResources().getString(R.string.interface_softkeys_screenoff_title);
        } else if (uri.equals("assist")) {
                return getResources().getString(R.string.interface_navring_assist_title);
        } else {
                return mPicker.getFriendlyNameForUri(uri);
        }
   }

    private Drawable getNavbarIconImage(Preference preference) {
        if (preference == mRing1) {
            mString = Settings.System.SYSTEMUI_NAVRING_1;
        } else if (preference == mRing2) {
            mString = Settings.System.SYSTEMUI_NAVRING_2;
        } else if (preference == mRing3) {
            mString = Settings.System.SYSTEMUI_NAVRING_3;
        } else if (preference == mRing4) {
            mString = Settings.System.SYSTEMUI_NAVRING_4;
        } else if (preference == mRing5) {
            mString = Settings.System.SYSTEMUI_NAVRING_5;
        }

        String uri = Settings.System.getString(getActivity().getContentResolver(),mString);

        if (uri == null)
            return getResources().getDrawable(R.drawable.ic_sysbar_null);


            if (uri.equals("none")) {
                return getResources().getDrawable(R.drawable.ic_sysbar_null);
            } else if (uri.equals("screenshot")) {
                return getResources().getDrawable(R.drawable.ic_navbar_screenshot);
            } else if (uri.equals("ime_switcher")) {
                return getResources().getDrawable(R.drawable.ic_sysbar_ime_switcher);
            } else if (uri.equals("ring_vib")) {
                return getResources().getDrawable(R.drawable.ic_navbar_vib);
            } else if (uri.equals("ring_silent")) {
                return getResources().getDrawable(R.drawable.ic_navbar_silent);
            } else if (uri.equals("ring_vib_silent")) {
                return getResources().getDrawable(R.drawable.ic_navbar_ring_vib_silent);
            } else if (uri.equals("killcurrent")) {
                return getResources().getDrawable(R.drawable.ic_navbar_killtask);
            } else if (uri.equals("screenoff")) {
                return getResources().getDrawable(R.drawable.ic_navbar_power);
            } else if (uri.equals("assist")) {
                return getResources().getDrawable(R.drawable.ic_navbar_googlenow);
            } else {
                try {
                   return mContext.getPackageManager().getActivityIcon(Intent.parseUri(uri, 0));
                } catch (NameNotFoundException e) {
                   e.printStackTrace();
                } catch (URISyntaxException e) {
                   e.printStackTrace();
                }
            }
        return getResources().getDrawable(R.drawable.ic_sysbar_null);
     }

    private Drawable resize(Drawable image) {
        int size = 50;
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, getResources()
                .getDisplayMetrics());

        Bitmap d = ((BitmapDrawable) image).getBitmap();
        Bitmap bitmapOrig = Bitmap.createScaledBitmap(d, px, px, false);
        return new BitmapDrawable(mContext.getResources(), bitmapOrig);
    }
}
