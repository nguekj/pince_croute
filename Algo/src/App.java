import java.io.*;
import java.nio.file.*;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.stream.Collectors;

public class App {


    public static void main(String[] args)throws Exception {
        
        //lecture du fichier
        String directory = "O:/Equipes/Électrolyse-(Secteur)/ABS/Stagiaire/Hiver 2023/données_csv";
        File file = new File(directory+"/RFID_SURAL_2023_02_01.csv");
        int size = 0;
        int x = 0;
        String[][] data;
        String line;
        String[] mots = null;
        int [] balise_pc = {2428}; //,2014,2011,2545,2074
        
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            while ((line = br.readLine()) != null){
                size = size + 1;
            }
            System.out.println(size);
        }                  
        catch (Exception e) {
            System.out.println(e);
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {        
            data = new String[(size)][9];
            //System.out.println(data.length);
            while ((line = br.readLine()) != null){
                
                mots = line.split(";");
                //System.out.println(mots);
                for (int i = 0; i < 9; i ++){
                    data[x][i] = mots[i];
                }    
                x = x +1;
                //data[x-1][8] = String.valueOf(x);
                
            }
            String[][] newData = new String[(size)][6];
            newData[0][0] = data[0][1]; //reader_uwb_id
            newData[0][1] = data[0][2]; //tag_id
            newData[0][2] = data[0][4]; //distance
            newData[0][3] = data[0][6]; //insert_timestamp
            newData[0][4] = "Pince à croute"; //insert_timestamp
            newData[0][5] = "index"; //insert_timestamp
            
            for (int i=1;i<newData.length;i++){
                newData[i][5] = String.valueOf(i-1);
            }
            for (int j=1; j<size; j++){
                if ((Float.parseFloat(data[j][4])<19) && (Float.parseFloat(data[j][4])>1.88) ){
                    newData[j][0] = data[j][1]; //reader_uwb_id
                    newData[j][1] = data[j][2]; //tag_id
                    newData[j][2] = data[j][4]; //distance
                    newData[j][3] = data[j][6]; //insert_timestamp
                    //newData[j][4] = "0";
                }

            }
            
            //put 1|0 if there is pince a croute
            for (int i=0; i<balise_pc.length; i++)
            {//System.out.print(balise_pc[i]+"\n");
                for (int j=1; j<newData.length; j++){
                    if (newData[j][1] != null)
                        if (Integer.valueOf(newData[j][1])==balise_pc[i] && newData[j][4]!=String.valueOf(1))
                        {
                            newData[j][4] = String.valueOf(1);
                        }
                        else{
                            newData[j][4] = String.valueOf(0);
                        }
                    
                }
            }
            
            //remove null rows
            //String[][] tempnewData = new String[(size)][4];
            /* 
            tempnewData = Arrays.stream(newData)
                     .filter(s -> (s != null))
                     .toArray(String[][]::new); 

            newData = tempnewData;
            System.out.print("taille new data : " + tempnewData.length);
            */
            //display
            for (int j=0; j<10; j++){
                for (int h=0; h<6; h++){
                    System.out.print(newData[j][h]+"  ");
                }
                System.out.print("\n");
            }
            
        }
        catch (Exception e)
        {
            System.out.print(e);
            e.printStackTrace();
        }
    }

}