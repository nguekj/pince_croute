import java.io.*;
import java.util.ArrayList;

public class App {
    public static void main(String[] args) throws Exception {
        File filename = new File("O:/Equipes/Électrolyse-(Secteur)/ABS/Stagiaire/Hiver 2023/opération/template_date.csv");
        readMSE(filename);

    }

    public static void readMSE(File file){
        String line;
        ArrayList<mseEnAnode> mseInfo = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {        

            while ((line = br.readLine()) != null){
                
                mseEnAnode ms = new mseEnAnode(line);
                mseInfo.add(ms);
                System.out.println(ms.getStartTime().toString());
            }
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
}
