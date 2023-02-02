import java.io.*;
import java.nio.file.*;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.ArrayList;

public class App {


    public static void main(String[] args)throws Exception {
        
        //lecture du fichier
        String directory = "O:/Equipes/Électrolyse-(Secteur)/ABS/Stagiaire/Hiver 2023/données_csv";
        File file = new File(directory+"/RFID_SURAL_2023_02_02.csv");
        int size = 0;
        int x = 0;
        String[][] data;
        String line;
        String[] mots = null;
        int [] balise_pc = {2014,2011,2545,2074}; //
        
        //lecture
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
                 
                for (int i = 0; i < 4; i ++){
                    data[x][i] = mots[i].replace(',', '.');
                }    
                x = x +1;
                //data[x-1][8] = String.valueOf(x);
            }

            String[][] newData = new String[(size)][6];
            newData[0][0] = data[0][0]; //reader_uwb_id
            newData[0][1] = data[0][1]; //tag_id
            newData[0][2] = data[0][2]; //distance
            newData[0][3] = data[0][3]; //insert_timestamp
            newData[0][4] = "Pince a croute"; //insert_timestamp
            newData[0][5] = "index"; //insert_timestamp
            
            
            //transfere les colonne important et dont la distance est entre 19 et 1.88
            

            int k = 1;
            for (int j=1; j<size; j++){
                if ((Float.parseFloat(data[j][2])<19) && (Float.parseFloat(data[j][2])>1.88) ){
                    newData[k][0] = data[j][0]; //reader_uwb_id
                    newData[k][1] = data[j][1]; //tag_id
                    newData[k][2] = data[j][2]; //distance
                    newData[k][3] = data[j][3]; //insert_timestamp
                    k++;
                }
            }

            // remove all nul rows
            String[][] tempnewData = new String[k][6];
            System.out.println(k);
            for (int i =0; i<newData.length;i++)
            {
                for (int j=0; j<6 ;j++)
                {
                    if (newData[i][j]!=null )
                    {
                        tempnewData[i][j] =  newData[i][j];
                    }
                }
            }
            newData = tempnewData;
            
            


            //mettre a la fin
            //column d'index
            for (int i=1;i<newData.length;i++){
                newData[i][5] = String.valueOf(i-1);
            }

            //put 1|0 if there is pince a croute
            int pc_count = 10;
            int n = 1;
            boolean ok = true;
            for (int j=1; j < newData.length; j++){
                for (int i=0; i <balise_pc.length; i++)
                {
                    if (Integer.valueOf(newData[j][1])==balise_pc[i])
                    {
                        pc_count = 10;
                        newData[j][4] = String.valueOf(pc_count);
                        
                        break;
                    }
                    else{
                        //pc_count = pc_count-1;
                        if (ok){
                            pc_count -= 1; 
                            ok = false;
                            newData[j][4] = String.valueOf(pc_count);
                        }
                        
                        
                    }
                    
                
                }
                //pc_count -= 1;
                ok = true;
                
            }
            
            //filtre et prend pour newData[j][4] > 1


            //display few data
            for (int j=0; j<10; j++){
                for (int h=0; h<6; h++){
                    System.out.print(newData[j][h]+"  ");
                }
                System.out.print("\n");
            }
            write_csv(newData);
            
        }
        catch (Exception e)
        {
            System.out.print(e);
            e.printStackTrace();
        }
    }



    public static void write_csv(String[][] myArray){
        String directory = "O:/Equipes/Électrolyse-(Secteur)/ABS/Stagiaire/Hiver 2023/données_traitées";
        String fileName = directory+"/RFID_SURAL_2023_02_02.csv";
        String encoding = "UTF-8";
        try{

        PrintWriter writer = new PrintWriter(fileName, encoding);
        
        for (int j = 0; j < myArray.length; j ++){

            for (int i = 0; i < 6; i ++){
                
                writer.print(myArray[j][i]);
                writer.print(";");  
            }
            writer.print("\n");
        }

        writer.close();
        }
        catch (Exception e)
        {
            System.out.print(e);
            e.printStackTrace();
        }
   
    }

}