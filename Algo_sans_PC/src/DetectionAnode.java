import java.io.*;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;

public class DetectionAnode {
    
    public void Detection(File fileDetection, File filecsv)throws Exception{
        String[][] BD; // importer de la BD
        //String[][] cp; // tableau final
        int[][] bd; // ligne trier pour 5 point consecutif et nombre de point consecutif
        int[] ligne; // tableau des lignes en 7m et 15m
        int[][] CP; // tableau des coups de pince
        
        int k = 0;
        int m = 0;
        int n = 0;
        Double tempo = 0.0; // variable temporaire pour etape 4

        double[][] A = {
            {  8,  9,  7.893 },
            {  7, 10,  8.954 },
            {  6, 11, 10.515 },
            {  5, 12, 11.076 },
            {  4, 13, 12.137 },
            {  3, 14, 13.198 },
            {  2, 15, 14.259 },
            {  1, 16,  15.32 },};

        /* etape 1
         * etape 2
         * 
         */

        int x = 0;
        String st;
        String[] mots = null;
        int size = 0; 

        //File file = new File("C:/Users/nguekj/Hiver2023/Code/Data/lecture/DETECTION_DATA_ANODES3.csv");
        try (BufferedReader br = new BufferedReader(new FileReader(filecsv))) {
            while ((st = br.readLine()) != null){
                size = size + 1;
            }
        }
        BD = new String[(size)][7]; // tableau 
        ligne = new int[(size)];

        try (BufferedReader br = new BufferedReader(new FileReader(filecsv))) {        

            while ((st = br.readLine()) != null){

                mots = st.split(";");
                for (int i = 0; i < BD[0].length; i ++){
                    BD[x][i] = mots[i].replace(',', '.');
                }    
                x = x +1;
                
            }
        }
        catch (Exception e)
        {
            System.out.print(e.getMessage());
        }

        /*
         * 
         * etape 3
         * 
         * 
         */
        
        /*
         * prend les points dans l'intervall des distance d'anonde et calcul le nombre de point 
         */
        m = 0;
        for (int i = 1; i < size; i ++){
            
            if ((Double.valueOf(BD[i][2]) > 7.393) & (Double.valueOf(BD[i][2]) < 15.82)) {              
                ligne[m] = i;
                m = m + 1;               
            }
        } 
        bd = new int[(m)][8];
        //cp = new String[bd.length][8];

        /*
         * 
         * etape 4
         * 
         */

        k = 0; // variable pour placer la ligne dans le tableau bd
        n = 0; // variable du nombre de point consecutif
        tempo = 0.0;
        //ligne[] est l'index de donnée qui est conforme dont la distance est dans notre intervalle 

        tempo = Double.valueOf(BD[ligne[0]][2]); 
 
        n = 0;
        
        //regarde si la premiere valeur distance ligne[0] qui est conforme
        //
        for (int i = 1; i < bd.length; i ++){
            
            //vient voir il y a cmb de point consecutif dans l'intervalle de + - 0.4 
            if ((Double.valueOf(BD[ligne[i]][2]) - 0.4) < tempo & (Double.valueOf(BD[ligne[i]][2]) + 0.4) > tempo & ligne[i] == ligne[i - 1] + 1){
                    n = n + 1;
                    tempo = Double.valueOf(BD[ligne[i]][2]);

            }
            else {
                //au moins 5 point consecutif dans le meme intervalle 
                if (n > 4){
                                            
                        bd[k][0] = ligne[i-1];  //garde la valeur
                        bd[k][1] = n;      //permet de trouver combien le coup de pince a durer
                        k = k + 1;
                        tempo = Double.valueOf(BD[ligne[i]][2]);
                        n = 1;              
                }
                tempo = Double.valueOf(BD[ligne[i]][2]);
                n = 1;    
            }
            
        } 
        CP = new int[k][2]; //data type should be float. cause distance have decimale

        /*
         * 
         * etape 5
         * 
         */

        n = 0; // variable du nombre de point consecutif 
        k = 0;
        int z = 0;

        for (int j = 0; j < CP.length; j ++){
        
            for (int i = 0; i < 10; i ++){
                //dans la ligne garder est ce qu'il y a des point consecutif
                if ((Double.valueOf(BD[bd[j][0]+i][2]) > 3.8) & (Double.valueOf(BD[bd[j][0]+i][2]) < 5.2)){

                    i = 10; //pour 50sec
                    
                    for (int l = 0; l < 10; l ++){
                        //5.2 et 6 retour a la position normal
                            //System.out.println("position : "+(bd[j][0]-bd[j][1]-l));
                            if (((Double.valueOf(BD[bd[j][0]-bd[j][1]-l][2]) > 5.2) & (Double.valueOf(BD[bd[j][0]-bd[j][1]-l][2]) < 6))){
                                
                                n = n + 1; //si il trouve rien
                            
                            }
                            // CP : il y a presence de coup de pince
                            if (CP[k - z][0] == (Double.valueOf(BD[bd[j][0]-bd[j][1]-l][5])) || n > 1){

                                l = 10;
                                CP[k][0] = bd[j][0];
                                CP[k][1] = bd[j][1];  
                                k = k + 1;
                                z = 1;
                            }
                        
                        

                    }

                }
                
            }                       
            
        }

        /*
         * etape 6
        */
        //String encoding = "UTF-8";
        boolean appendMose = false;
        FileWriter writer = new FileWriter(fileDetection,StandardCharsets.UTF_8,appendMose); //true for append mode
        if(!fileDetection.exists() || !appendMose){
                        writer.write("anode_number;");  
                        writer.write("location_name;");  
                        writer.write("groupe;");   
                        writer.write("scope_time;");  
                        writer.write("timestamp;");    
                        writer.write("line_number;");
                        writer.write("\n");
        }                 

        for (int j = 0; j < k; j ++){

            for (int i = 0; i < 8; i ++){
                //regarde si la distance est <|> que +|- 0.5305 de celle des anodes
                //System.out.println(BD[CP[j][0] - 1][2]); 
                if ((Double.valueOf(BD[CP[j][0] - 1][2]) > A[i][2] - 0.5305) & (Double.valueOf(BD[CP[j][0] - 1][2]) <= A[i][2] + 0.5305)){

                    writer.write(String.valueOf(A[i][0])+" - "+A[i][1]); // numero de l<anode
                        writer.write(";");                  
                    writer.write(String.valueOf(BD[CP[j][0]-1][4])); // nom de la location // MSE 
                        writer.write(";");
                    writer.write(BD[Integer.valueOf(CP[j][0])][6]); // groupe
                    writer.write(";");
                    writer.write(String.valueOf(CP[j][1] * 5) + "sec"); // nombre de temps passer a l<anode
                        writer.write(";");
                    writer.write(BD[Integer.valueOf(CP[j][0])][3]); // timestamp
                        writer.write(";");
                    writer.write(String.valueOf(CP[j][0])); // numero de la ligne
                    writer.write("\n");
                    
                    i = 8; //replace by break;
                }
                
            }

        }
        writer.close();



        /* 
        String encoding = "UTF-8";
        PrintWriter writer = new PrintWriter(fileDetection, encoding);

                        writer.print("anode_number;");  
                        writer.print("location_name;");  
                        writer.print("groupe;");   
                        writer.print("scope_time;");  
                        writer.print("timestamp;");    
                        writer.println("line_number;");
                         

        for (int j = 0; j < k; j ++){

            for (int i = 0; i < 8; i ++){
                //regarde si la distance est <|> que +|- 0.5305 de celle des anodes
                //System.out.println(BD[CP[j][0] - 1][2]); 
                if ((Double.valueOf(BD[CP[j][0] - 1][2]) > A[i][2] - 0.5305) & (Double.valueOf(BD[CP[j][0] - 1][2]) <= A[i][2] + 0.5305)){

                    writer.print(String.valueOf(A[i][0])+" - "+A[i][1]); // numero de l<anode
                        writer.print(";");                  
                    writer.print(String.valueOf(BD[CP[j][0]-1][4])); // nom de la location // MSE 
                        writer.print(";");
                    writer.print(BD[Integer.valueOf(CP[j][0])][6]); // groupe
                    writer.print(";");
                    writer.print(String.valueOf(CP[j][1] * 5) + "sec"); // nombre de temps passer a l<anode
                        writer.print(";");
                    writer.print(BD[Integer.valueOf(CP[j][0])][3]); // timestamp
                        writer.print(";");
                    writer.println(String.valueOf(CP[j][0])); // numero de la ligne
                    
                    i = 8; //replace by break;
                }
                
            }

        }
        writer.close();
        */
    }
    
    public Double parseIntOrNull(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
