import java.io.*;
import java.nio.file.*;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.ArrayList;

public class App {

    public static int [] balise_pc = new int[20]; //read file to get that information
    
    
    public static void main(String[] args)throws Exception {
        ArrayList<int[]> balise_antenne = new ArrayList<>();
        //
        balise_antenne = readEquipement();
        analyse_csv(balise_antenne);
    }

    public static void analyse_csv(ArrayList<int[]> balise_antenneList)
    {
        //lecture du fichier
        String directory = "O:/Equipes/Électrolyse-(Secteur)/ABS/Stagiaire/Hiver 2023/données_csv";
        File file = new File(directory+"/RFID_SURAL_2023_02_02.csv");
        
        System.out.println(file.getName());
        int size = 0;
        int x = 0;
        String[][] data;
        String line;
        String[] mots = null;
        //int [] balise_pc = {2014,2011,2545,2074}; //read file to get that information
        
        //lecture
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            while ((line = br.readLine()) != null){
                size = size + 1;
            }
            //System.out.println(size);
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
            newData[0][4] = "Pince a croute"; //presence pince a croute
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
            //System.out.println("k "+k);
            for (int i =0; i<newData.length;i++)
            {
                for (int j=0; j<6 ;j++)
                {
                    if (newData[i][0]!=null )
                    {
                        tempnewData[i][j] =  newData[i][j];
                    }
                }
            }

            //newData = tempnewData; //removeNullCells(newData,k);
            //System.out.println("old k "+newData.length);
            newData = new String[k][6];
            newData = (String[][])resize(tempnewData,k);

            //System.out.println("new k "+newData.length);      
            
            //count pince a croute detect
            int pc_count = 10;
            boolean flag = true;
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
                        if (flag){
                            pc_count -= 1; 
                            flag = false;
                            newData[j][4] = String.valueOf(pc_count);
                        }
                        
                    }
                
                }
                //pc_count -= 1;
                flag = true;
            }
            
            //filtre et prend pour newData[j][4] > 1
            int one_counter = 1;
            //String[][] OnenewData = new String[newData.length][6];
            String[][] tempOnenewData = new String[newData.length][6];

            for (int i = 1; i<newData.length; i++){
                
                if(Integer.valueOf(newData[i][4]) >= 1)
                {
                    for(int j=0; j<6; j++)
                    {
                        //OnenewData[i][j] = newData[i][j];
                        tempOnenewData[one_counter][j] = newData[i][j];
                    }
                    one_counter++;
                }
            }
            for(int i=0; i<6; i++){
                tempOnenewData[0][i] = newData[0][i];
            }
            System.out.println("one_counter : "+one_counter);
            newData = new String[one_counter][6];
            System.out.println("newData one_counter : "+newData.length);
            newData = (String[][])resize(tempOnenewData,one_counter);

            
            //tout enlever sauf les paire antenne-balise
            int nextPaire = 1;
            String[][] tempPaireBalise_antenne = new String[newData.length][6];
            for (int i = 1; i<newData.length; i++){
                for(int n = 0; n<balise_antenneList.size(); n++){
                    if((Integer.valueOf(newData[i][0]) == balise_antenneList.get(n)[1]) && (Integer.valueOf(newData[i][1]) == balise_antenneList.get(n)[0]))
                    {
                        for(int j=0; j<6; j++)
                        {
                            tempPaireBalise_antenne[nextPaire][j] = newData[i][j];
                        }
                        nextPaire++;
                    }

                }
            }
            System.out.println("nombre de paire an-bal : "+nextPaire);
            for(int i=0; i<6; i++){
                tempPaireBalise_antenne[0][i] = newData[0][i];
            }
            newData = new String[nextPaire][6];
            newData = (String[][])resize(tempPaireBalise_antenne,nextPaire);
            System.out.println("taille apres filtre pairage resize : "+newData.length);
            
            /*
             * enlever la colonne pince a croute
            */
            String[][] removePCdata = new String[newData.length][5];
            for (int j=0; j<newData.length; j++){
                removePCdata[j][0] = newData[j][0]; //reader_uwb_id
                removePCdata[j][1] = newData[j][1]; //tag_id
                removePCdata[j][2] = newData[j][2]; //distance
                removePCdata[j][3] = newData[j][3]; //insert_timestamp
                removePCdata[j][4] = newData[j][5]; //index
            }
            newData = new String[removePCdata.length][5];
            //newData = (String[][])resize(removePCdata,removePCdata.length);
            //newData = removeNullCells(removePCdata,removePCdata.length);
            newData = removePCdata;

            //column d'index
            for (int i=1;i<newData.length;i++){
                newData[i][4] = String.valueOf(i-1);
            }
            //display few data
            for (int j=0; j<10; j++){
                for (int h=0; h<5; h++){
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
    public static String[][] removeNullCells(String[][] table, int taille){

        String[][] newtable = new String[taille][table[0].length];
            //System.out.println(taille);
        for (int i =0; i<taille;i++)
        {
            for (int j=0; j<table[0].length ;j++)
            {
                if (table[i][j]!=null )
                {
                    newtable[i][j] =  table[i][j];
                }
            }
        }
        //table = newtable;
        return newtable;
    }

    private static Object resize(Object oldArray, int newSize) {
        int oldSize = java.lang.reflect.Array.getLength(oldArray);
        Class<?> elementType = oldArray.getClass().getComponentType();
        Object newArray = 
             java.lang.reflect.Array.newInstance(elementType, newSize);
        int preserveLength = Math.min(oldSize, newSize);
        if (preserveLength > 0) {
           System.arraycopy(oldArray, 0, newArray, 0, preserveLength);
        }
        return newArray;
     }

    public static void write_csv(String[][] myArray){
        String directory = "O:/Equipes/Électrolyse-(Secteur)/ABS/Stagiaire/Hiver 2023/données_traitées";
        String fileName = directory+"/RFID_SURAL_2023_02_02.csv";
        String encoding = "UTF-8";
        try{

        PrintWriter writer = new PrintWriter(fileName, encoding);
        
        for (int j = 0; j < myArray.length; j ++){
            //if (myArray[j][0].equals("")) { break; }
            for (int i = 0; i < myArray[0].length; i ++){
                
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

    public static ArrayList<int[]> readEquipement(){
        String directory = "O:/Equipes/Électrolyse-(Secteur)/ABS/Stagiaire/Hiver 2023/Attribue";
        File file = new File(directory+"/Liste antene et balise UWB-csv.csv");
        int size = 0;
        String line;
        String[] mots ;
        ArrayList<int[]> _balise_antenne = new ArrayList<int[]>();
        String search = "APLE";
        String tiret = "-";
        String pont = "PONT";
        //lecture
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            while ((line = br.readLine()) != null){
                size = size + 1;
            }
            //System.out.println(size);
        }                  
        catch (Exception e) {
            System.out.println(e);
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {        

            ArrayList<String[]> content = new ArrayList<String[]>();
            while ((line = br.readLine()) != null){
                
                mots = line.split(";");
                content.add(mots);
      
            }

            int count = 0;
            //int [] temp = new int[2];
            for(int i=0; i<content.size();i++){
                if((content.get(i)[1].toLowerCase().indexOf(search.toLowerCase()) != -1) && (content.get(i)[2].toLowerCase().contains(tiret)==false))
                {
                    //System.out.println(Integer.valueOf(content.get(i)[2]));
                    balise_pc[count] = Integer.valueOf(content.get(i)[2]);
                    count++;
                }
                else if(content.get(i)[1].toLowerCase().indexOf(pont.toLowerCase()) != -1) {
                    
                    if ((content.get(i)[3].toLowerCase().contains(tiret)==false) && (content.get(i)[2] != "")){
                        int [] temp = new int[2];
                        temp[0] = Integer.valueOf(content.get(i)[2]);
                        temp[1] = Integer.valueOf(content.get(i)[3]);
                        System.out.println(temp[0]+" "+temp[1]);
                        _balise_antenne.add(temp);
                        
                    }
                }
                
            }
            return _balise_antenne;
        }
        
        catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
            return null;
        }

    }

}