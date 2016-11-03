package com.blueboxmicrosystems.fragments;

import android.accounts.Account;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.blueboxmicrosystems.fragments.FragmentOne;
import com.blueboxmicrosystems.fragments.FragmentTwo;
import com.blueboxmicrosystems.fragments.R;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        FragmentMain.OnFragmentInteractionListener,
        Accounts.OnFragmentInteractionListener,
        AddAccount.OnFragmentInteractionListener,
        FragmentOne.OnFragmentInteractionListener,
        FragmentTwo.OnFragmentInteractionListener {

    public String currentFrame;
    public Menu mMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        if (savedInstanceState == null) {
            Fragment fragment = null;
            Class fragmentClass = null;
            fragmentClass = FragmentMain.class;
            currentFrame = "FragmentMain";
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        this.setTitle("DashBoard");

    }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        mMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Fragment fragment = null;

        //noinspection SimplifiableIfStatement

        try {
            if (id == R.id.action_save) {
                if (currentFrame.equals("AddAccount")) {
                    fragment = (Fragment) (AddAccount.class).newInstance();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
                    ((AddAccount) fragment).save(this);
                    return true;
                }
            }

            if (id == R.id.action_cancel) {
                if (currentFrame.equals("AddAccount")) {
                    fragment = (Fragment) (AddAccount.class).newInstance();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
                    ((AddAccount) fragment).cancel(this);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            replaceFragments(FragmentMain.class);
        }
        if (id == R.id.nav_accounts) {
            replaceFragments(Accounts.class);
        }
        if (id == R.id.nav_income_categories) {
            replaceFragments(FragmentOne.class);
        }
        if (id == R.id.nav_expense_categories) {
            replaceFragments(FragmentTwo.class);
        }
        if (id == R.id.nav_settings) {
            replaceFragments(FragmentOne.class);
        }
        if (id == R.id.nav_about) {
            replaceFragments(FragmentTwo.class);
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
