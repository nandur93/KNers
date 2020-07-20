package com.ndu.sanghiang.kners;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import com.github.javiersantos.appupdater.AppUpdater;
import com.github.javiersantos.appupdater.enums.Display;
import com.github.javiersantos.appupdater.enums.UpdateFrom;


public class SettingsActivity extends PreferenceActivity {
    private static final String SCHEME = "package";
    private static final String APP_PKG_NAME_21 = "com.android.settings.ApplicationPkgName";
    private static final String APP_PKG_NAME_22 = "pkg";
    private static final String APP_DETAILS_PACKAGE_NAME = "com.android.settings";
    private static final String APP_DETAILS_CLASS_NAME = "com.android.settings.InstalledAppDetails";
    private SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        LinearLayout root = (LinearLayout) findViewById(android.R.id.list).getParent().getParent().getParent();
        Toolbar bar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.toolbar_settings, root, false);
        root.addView(bar, 0); // insert at top
        bar.setNavigationOnClickListener(v -> finish()
        );

        //update value
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        Preference prefCheckForUpdates = findPreference("prefCheckForUpdates");
        Preference prefSendFeedback = findPreference("prefSendFeedback");
        Preference prefVersionName = findPreference("version_name");

        prefCheckForUpdates.setOnPreferenceClickListener(preference -> {
            new AppUpdater(SettingsActivity.this)
                    //.setUpdateFrom(UpdateFrom.GITHUB)
                    //.setGitHubUserAndRepo("javiersantos", "AppUpdater")
                    .setUpdateFrom(UpdateFrom.XML)
                    .setUpdateXML("https://raw.githubusercontent.com/nandur93/KNers/master/update-changelog.xml")
                    .setDisplay(Display.DIALOG)
                    .setButtonDoNotShowAgain(null)
                    .showAppUpdated(true)
                    .start();
            return true;
        });

        prefSendFeedback.setOnPreferenceClickListener(preference -> {
            sendFeedback(this);
            return true;
        });

        prefVersionName.setOnPreferenceClickListener(preference -> {
            showInstalledAppDetails(this, getPackageName());
            return true;
        });

        //getVersionName
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String versName = pInfo.versionName;
            int versCode = pInfo.versionCode;
            prefVersionName.setSummary(versName);
            Log.d("MyApp", "Version Name : " + versName + "\n Version Code : " + versCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Log.d("MyApp", "PackageManager Catch : " + e.toString());
        }

        ListPreference list = (ListPreference) getPreferenceManager().findPreference("screen_orientation");
        list.setValue(sharedPrefs.getString("screen_orientation", "1"));
        list.setOnPreferenceChangeListener((preference, newValue) -> {
            SharedPreferences.Editor editor = sharedPrefs.edit();
            editor.putString("screen_orientation", newValue.toString());
            editor.apply();
            return true;
        });

        ListPreference listCharacter = (ListPreference) getPreferenceManager().findPreference("character_length");
        listCharacter.setValue(sharedPrefs.getString("character_length", "2"));
        listCharacter.setOnPreferenceChangeListener((preference, newValue) -> {
            SharedPreferences.Editor editor = sharedPrefs.edit();
            editor.putString("character_length", newValue.toString());
            editor.apply();
            return true;
        });

    }



    private static void showInstalledAppDetails(Context context, String packageName) {
        Intent intent = new Intent();
        final int apiLevel = Build.VERSION.SDK_INT;
        if (apiLevel >= 9) { // above 2.3
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts(SCHEME, packageName, null);
            intent.setData(uri);
        } else { // below 2.3
            final String appPkgName = (apiLevel == 8 ? APP_PKG_NAME_22
                    : APP_PKG_NAME_21);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName(APP_DETAILS_PACKAGE_NAME,
                    APP_DETAILS_CLASS_NAME);
            intent.putExtra(appPkgName, packageName);
        }
        context.startActivity(intent);

    }


    /**
     * Email client intent to send support mail
     * Appends the necessary device information to email body
     * useful when providing support
     */
    public static void sendFeedback(Context context) {
        String body = null;
        try {
            body = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            body = "\n\n-----------------------------\nTolong jangan hapus bagian ini\n Device OS: Android \n Device OS version: " +
                    Build.VERSION.RELEASE + "\n App Version: " + body + "\n Device Brand: " + Build.BRAND +
                    "\n Device Model: " + Build.MODEL + "\n Device Manufacturer: " + Build.MANUFACTURER;
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"nandang.dhe@gmail.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback Untuk KNers App");
        intent.putExtra(Intent.EXTRA_TEXT, body);
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.choose_email_client)));
    }
}