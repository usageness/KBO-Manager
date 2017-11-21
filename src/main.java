import java.io.IOException;
 
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
 
public class main {
    public static void main(String args[]) throws Throwable {
        try {
            Document doc = Jsoup.connect("http://www.koreabaseball.com/TeamRank/TeamRank.aspx").get();
            //Elements contents = doc.select("div.aspNetHidden");
            //String text = contents.text();
            String text = doc.select("table").text();
            StringBuffer tex = new StringBuffer(text);
            tex.insert(0, "******리그순위표******\n");
            
            int Check = 0;
            int CheckTeam = 0;
            int i = 0;
            
            for(i = 0 ; CheckTeam < 11 ; i++)
            {
                if(tex.charAt(i) == ' ')
                    Check += 1;
                if(Check == 12) {
                    CheckTeam += 1;
                    Check = 0;
                    tex = tex.insert(i+1, "\n");
                }
            }
            
            String rank = new String(tex.substring(0,i));
            StringBuffer winlose = new StringBuffer(tex.substring(i));
            
            winlose.insert(0, "\n\n******팀간승패표******");
            
            int j = 0;
            Check = 0;
            CheckTeam = 0;
            
            for(Check = 0; CheckTeam < 1 ; j++)
            {
                if(winlose.charAt(j) == ' ')
                    Check += 1;
                if(Check == 22) {
                    winlose = winlose.insert(j+1, "\n");
                    CheckTeam += 1;
                }
            }
            
            Check = 0;
            CheckTeam = 0;
            
            for(Check = 0; CheckTeam < 9; j++) {
                if(winlose.charAt(j) == ' ')
                    Check += 1;
                if(Check == 12) {
                    winlose = winlose.insert(j+1, "\n");
                    Check = 0;
                    CheckTeam += 1;
                }
            }
                
            System.out.println(rank);
            System.out.println(winlose);
            //System.out.println(tex); // 12개마다 끊어야 함
            //System.out.println(doc);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}