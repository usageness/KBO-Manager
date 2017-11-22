package com.example.usage.kbo_manager;

import android.widget.TextView;
import org.jsoup.Jsoup;

import java.io.IOException;

/**
 * Created by Kim_YongRae on 2017-11-22.
 */

public class PharsingRank implements Runnable {
        @Override
        public void run() {
            MainActivity mainActivity = new MainActivity();
            TextView pharsedText = (TextView) mainActivity.findViewById(R.id.pharsedText);
            org.jsoup.nodes.Document doc = null;
            try {
                doc = Jsoup.connect("http://www.koreabaseball.com/TeamRank/TeamRank.aspx").get();
                String text = doc.select("table").text();
                StringBuffer tex = new StringBuffer(text);
                tex.insert(0, "******리그순위표******\n");

                int Check = 0;
                int CheckTeam = 0;
                int i = 0;

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
                Check = 0;
                CheckTeam = 0;

                for (Check = 0; CheckTeam < 1; j++) {
                    if (winlose.charAt(j) == ' ')
                        Check += 1;
                    if (Check == 22) {
                        winlose = winlose.insert(j + 1, "\n");
                        CheckTeam += 1;
                    }
                }

                Check = 0;
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

                pharsedText.setText(rank + "\n" + winlose);

            } catch (IOException e)

            {
                e.printStackTrace();
            }
        }
}