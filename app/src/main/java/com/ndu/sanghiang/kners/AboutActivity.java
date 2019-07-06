package com.ndu.sanghiang.kners;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.github.javiersantos.appupdater.AppUpdater;
import com.github.javiersantos.appupdater.enums.Display;
import com.github.javiersantos.appupdater.enums.UpdateFrom;
import com.google.android.material.appbar.AppBarLayout;

import java.util.Calendar;
import java.util.Objects;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

import static com.ndu.sanghiang.kners.R.string.sum_about;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = findViewById(R.id.toolbar);
        //TextView textview = findViewById(R.id.textview_about);
        AppBarLayout appBarLayout = findViewById(R.id.appbar_about);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("About KNers App");
        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.drawable.about_nandur93)
                //.addItem(versionElement)
                //.addItem(adsElement)
                .addGroup("Connect with us")
                .addEmail("nandang.dhe@gmail.com")
                .addWebsite("https://nandur93.com")
                .addFacebook("nandur93")
                .addTwitter("NanDur93")
                .addYoutube("dhe96")
                //.addPlayStore("com.ideashower.readitlater.pro")
                .addGitHub("nandur93")
                .addInstagram("nandangduryat")
                .addItem(getCopyRightsElement())
                .setDescription(getString(sum_about))
                .create();
        appBarLayout.addView(aboutPage, 1);
    }


    Element getCopyRightsElement() {
        Element copyRightsElement = new Element();
        @SuppressLint({"StringFormatInvalid", "LocalSuppress"}) final String copyrights = String.format(getString(R.string.copy_right), Calendar.getInstance().get(Calendar.YEAR));
        copyRightsElement.setTitle(copyrights);
        copyRightsElement.setIconDrawable(R.drawable.ic_copyright_black_24dp);
        copyRightsElement.setIconTint(mehdi.sakout.aboutpage.R.color.about_item_icon_color);
        copyRightsElement.setIconNightTint(android.R.color.white);
        copyRightsElement.setGravity(Gravity.CENTER);
        copyRightsElement.setOnClickListener(v -> Toast.makeText(AboutActivity.this, copyrights, Toast.LENGTH_SHORT).show());
        return copyRightsElement;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.about_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_update) {
            cekForUpdate();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void cekForUpdate() {
        //update value
        new AppUpdater(AboutActivity.this)
                //.setUpdateFrom(UpdateFrom.GITHUB)
                //.setGitHubUserAndRepo("javiersantos", "AppUpdater")
                .setUpdateFrom(UpdateFrom.XML)
                .setUpdateXML("https://raw.githubusercontent.com/nandur93/KNers/master/update-changelog.xml")
                .setDisplay(Display.DIALOG)
                .setButtonDoNotShowAgain(null)
                .showAppUpdated(true)
                .start();
    }
}
