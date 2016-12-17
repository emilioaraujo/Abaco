package com.blueboxmicrosystems.abaco;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.blueboxmicrosystems.abaco.database.AbacoDatabaseHelper;

import java.util.Currency;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        AccountList.OnFragmentInteractionListener,
        AccountAdd.OnFragmentInteractionListener,
        TagList.OnFragmentInteractionListener,
        CategoryList.OnFragmentInteractionListener,
        TransactionList.OnFragmentInteractionListener,
        MainFragment.OnFragmentInteractionListener {

    public String currentFrame;
    public Menu mMenu;
    public static AbacoDatabaseHelper abacoDatabaseHelper;
    public static SQLiteDatabase abacoDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            Fragment fragment;
            Class fragmentClass = MainFragment.class;
            currentFrame = "MainFragment";
            try {
                fragment = (Fragment) fragmentClass.newInstance();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Abrimos la base de datos 'DBUsuarios' en modo escritura
        abacoDatabaseHelper = new AbacoDatabaseHelper(this, "AbacoDataBase", null, 26);
        abacoDataBase = abacoDatabaseHelper.getWritableDatabase();
    }


    public static Map getAvailableCurrencies() {
        Locale[] locales = Locale.getAvailableLocales();
        //
        // We use TreeMap so that the order of the data in the map sorted
        // based on the country name.
        //
        Map currencies = new TreeMap();
        for (Locale locale : locales) {
            try {
                currencies.put(locale.getDisplayCountry(), Currency.getInstance(locale));
            } catch (Exception e) {
                // when the locale is not supported
            }
        }
        return currencies;
    }


    //@Override
    //public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        //Toast.makeText(this,"Fecha: " + year + "-" + month + "-" + dayOfMonth,Toast.LENGTH_LONG).show();
    //}
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Confirm")
                .setMessage("Do you really want to close Abaco?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        MainActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        mMenu = menu;
        return true;
    }
*/
/*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        Fragment fragment;

        try {
            if (id == R.id.action_save) {
                if (currentFrame.equals("AccountAdd")) {
                    fragment = (AccountAdd.class).newInstance();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
                    ((AccountAdd) fragment).save(this);
                    return true;
                }
            }

            if (id == R.id.action_cancel) {
                if (currentFrame.equals("AccountAdd")) {
                    fragment = (AccountAdd.class).newInstance();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
                    ((AccountAdd) fragment).cancel(this);
                    return true;
                }
                if (currentFrame.equals("AccountList")) {
                    fragment = (AccountList.class).newInstance();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
                    ((AccountList) fragment).cancel(this);
                    return true;
                }
            }

            if (id == R.id.action_close) {
                this.onBackPressed();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onOptionsItemSelected(item);

    }

    */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            replaceFragments(MainFragment.class);
        }
        if (id == R.id.nav_accounts) {
            replaceFragments(AccountList.class);
        }
        if (id == R.id.nav_categories) {
            replaceFragments(CategoryList.class);
        }
        if (id == R.id.nav_tags) {
            replaceFragments(TagList.class);
        }
        if (id == R.id.nav_transactions) {
            replaceFragments(TransactionList.class);
        }
        if (id == R.id.nav_settings) {
            //replaceFragments(FragmentOne.class);
        }
        if (id == R.id.nav_share) {
            //replaceFragments(FragmentOne.class);
        }
        if (id == R.id.nav_about) {
            //replaceFragments(FragmentTwo.class);
        }

        return true;
    }

    public void replaceFragments(Class fragmentClass) {
        Log.d("", "Replacing fragment");
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
/*
        (mMenu.findItem(R.id.action_close)).setVisible(false);
        if (fragmentClass.toString().contains("Add")) {
            (mMenu.findItem(R.id.action_cancel)).setVisible(true);
            (mMenu.findItem(R.id.action_save)).setVisible(true);
        }else{
            (mMenu.findItem(R.id.action_cancel)).setVisible(true);
            (mMenu.findItem(R.id.action_save)).setVisible(false);
        }
        if (fragmentClass.toString().contains("Main")) {
            (mMenu.findItem(R.id.action_close)).setVisible(true);
            (mMenu.findItem(R.id.action_cancel)).setVisible(false);
        }
*/
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        currentFrame = fragmentClass.getSimpleName();


        //this.setTitle(drawer.getTag().toString());
        Log.d("", "currentFrame= " + currentFrame);
        Log.d("", "Fragment Replaced");
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Log.d("myApp", "onFragmentInteraction");
    }
}
