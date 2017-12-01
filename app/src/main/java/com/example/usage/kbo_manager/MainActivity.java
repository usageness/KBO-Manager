package com.example.usage.kbo_manager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView pharsedText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // 여기서부터 추가된 코드

        TextView mainText = (TextView) findViewById(R.id.mainText);
        TextView dateText = (TextView) findViewById(R.id.dateText);
        pharsedText = (TextView) findViewById(R.id.pharsedText);

        Calendar calendar = new GregorianCalendar(Locale.KOREA);
        int nMonth = calendar.get(Calendar.MONTH) + 1;
        int nDay = calendar.get(Calendar.DAY_OF_MONTH);

        String today = nMonth + "/" + nDay;
        mainText.setText("오늘의 경기");
        dateText.setText(today);

        exportData();
    }

    public void exportData() {
        String leagueURL = "http://www.koreabaseball.com/TeamRank/TeamRank.aspx";

        try {
            ConnectivityManager conManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = conManager.getActiveNetworkInfo();

            if(netInfo != null && netInfo.isConnected()) {
                new leagueRank().execute(leagueURL);
            }else {
                Toast toast = Toast.makeText(getApplicationContext(), "네트워크가 연결되지 않았음", Toast.LENGTH_LONG);
                toast.show();
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class leagueRank extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... arg0) {
            try {
                return (String)getData((String) arg0[0]);
            } catch (Exception e) {
                return "Network connect failed";
            }
        }

        protected void onPostExecute(String result) {
            pharsedText.setText(result);
        }

        private String getData(String leagueURL) {

            org.jsoup.nodes.Document doc = null;
            String leagueTable = "";
            int Check = 0;
            int CheckTeam = 0;
            int i = 0;

            try {
                doc = Jsoup.connect(leagueURL).get();
                String text = doc.select("table").text();
                StringBuffer tex = new StringBuffer(text);
                tex.insert(0, "******리그순위표******\n");

                for (i = 0; CheckTeam < 11; i++) {
                    if (tex.charAt(i) == ' ')
                        Check += 1;
                    if (Check == 12) {
                        CheckTeam += 1;
                        Check = 0;
                        tex = tex.insert(i + 1, "\n");
                    }
                }

                String rank = new String(tex.substring(0, i));
                StringBuffer winlose = new StringBuffer(tex.substring(i));

                winlose.insert(0, "\n\n******팀간승패표******");

                int j = 0;
                //Check = 0;
                CheckTeam = 0;

                for (Check = 0; CheckTeam < 1; j++) {
                    if (winlose.charAt(j) == ' ')
                        Check += 1;
                    if (Check == 22) {
                        winlose = winlose.insert(j + 1, "\n");
                        CheckTeam += 1;
                    }
                }

               //Check = 0;
                CheckTeam = 0;

                for (Check = 0; CheckTeam < 9; j++) {
                    if (winlose.charAt(j) == ' ')
                        Check += 1;
                    if (Check == 12) {
                        winlose = winlose.insert(j + 1, "\n");
                        Check = 0;
                        CheckTeam += 1;
                    }
                }


                leagueTable = rank + "\n" + winlose;

            } catch (IOException e) {
                e.printStackTrace();
            }

            return leagueTable;
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

