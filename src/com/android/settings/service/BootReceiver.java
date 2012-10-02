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

package com.android.settings.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemProperties;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.settings.performance.ProcessorSettings;
import com.android.settings.performance.KernelSettings;
import com.android.settings.Utils;

import java.util.Arrays;
import java.util.List;

public class BootReceiver extends BroadcastReceiver {
    
    private static final String TAG = "BootReceiver";

    private static final String CPU_SETTINGS_PROP = "sys.cpufreq.restored";
    private static final String IOSCHED_SETTINGS_PROP = "sys.iosched.restored";
    private static final String KSM_SETTINGS_PROP = "sys.ksm.restored";

    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, BootService.class));
        if (SystemProperties.getBoolean(CPU_SETTINGS_PROP, false) == false
            && intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            SystemProperties.set(CPU_SETTINGS_PROP, "true");
            configureCPU(context);
        } else {
            SystemProperties.set(CPU_SETTINGS_PROP, "false");
        }
        
        if (SystemProperties.getBoolean(IOSCHED_SETTINGS_PROP, false) == false
            && intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            SystemProperties.set(IOSCHED_SETTINGS_PROP, "true");
            configureIOSched(context);
        } else {
            SystemProperties.set(IOSCHED_SETTINGS_PROP, "false");
        }
        if (Utils.fileExists(KernelSettings.KSM_RUN_FILE)) {
            if (SystemProperties.getBoolean(KSM_SETTINGS_PROP, false) == false
                && intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
                SystemProperties.set(KSM_SETTINGS_PROP, "true");
                configureKSM(context);
            } else {
                SystemProperties.set(KSM_SETTINGS_PROP, "false");
            }
        }
    }

    private void configureCPU(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        
        if (prefs.getBoolean(ProcessorSettings.SOB_PREF, false) == false) {
            Log.i(TAG, "Restore disabled by user preference.");
            return;
        }
        
        String governor = prefs.getString(ProcessorSettings.GOV_PREF, null);
        String minFrequency = prefs.getString(ProcessorSettings.FREQ_MIN_PREF, null);
        String maxFrequency = prefs.getString(ProcessorSettings.FREQ_MAX_PREF, null);
        String availableFrequenciesLine = Utils.fileReadOneLine(ProcessorSettings.FREQ_LIST_FILE);
        String availableGovernorsLine = Utils.fileReadOneLine(ProcessorSettings.GOV_LIST_FILE);
        boolean noSettings = ((availableGovernorsLine == null) || (governor == null)) &&
        ((availableFrequenciesLine == null) || ((minFrequency == null) && (maxFrequency == null)));
        List<String> frequencies = null;
        List<String> governors = null;
        
        if (noSettings) {
            Log.d(TAG, "No CPU settings saved. Nothing to restore.");
        } else {
            if (availableGovernorsLine != null){
                governors = Arrays.asList(availableGovernorsLine.split(" "));
            }
            if (availableFrequenciesLine != null){
                frequencies = Arrays.asList(availableFrequenciesLine.split(" "));
            }
            if (governor != null && governors != null && governors.contains(governor)) {
                Utils.fileWriteOneLine(ProcessorSettings.GOV_FILE, governor);
            }
            if (maxFrequency != null && frequencies != null && frequencies.contains(maxFrequency)) {
                Utils.fileWriteOneLine(ProcessorSettings.FREQ_MAX_FILE, maxFrequency);
            }
            if (minFrequency != null && frequencies != null && frequencies.contains(minFrequency)) {
                Utils.fileWriteOneLine(ProcessorSettings.FREQ_MIN_FILE, minFrequency);
            }
            Log.d(TAG, "CPU settings restored.");
        }
    }
    
    private void configureIOSched(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        
        if (prefs.getBoolean(KernelSettings.SOB_PREF, false) == false) {
            Log.i(TAG, "Restore disabled by user preference.");
            return;
        }
        
        String ioscheduler = prefs.getString(KernelSettings.IOSCHED_PREF, null);
        String availableIOSchedulersLine = Utils.fileReadOneLine(KernelSettings.IOSCHED_LIST_FILE);
        boolean noSettings = ((availableIOSchedulersLine == null) || (ioscheduler == null));
        List<String> ioschedulers = null;
        
        if (noSettings) {
            Log.d(TAG, "No I/O scheduler settings saved. Nothing to restore.");
        } else {
            if (availableIOSchedulersLine != null){
                ioschedulers = Arrays.asList(availableIOSchedulersLine.replace("[", "").replace("]", "").split(" "));
            }
            if (ioscheduler != null && ioschedulers != null && ioschedulers.contains(ioscheduler)) {
                Utils.fileWriteOneLine(KernelSettings.IOSCHED_LIST_FILE, ioscheduler);
            }
            Log.d(TAG, "I/O scheduler settings restored.");
        }
    }

    private void configureKSM(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        
        boolean ksm = prefs.getBoolean(KernelSettings.KSM_PREF, false);
        
        Utils.fileWriteOneLine(KernelSettings.KSM_RUN_FILE, ksm ? "1" : "0");
        Log.d(TAG, "KSM settings restored.");
    }
}