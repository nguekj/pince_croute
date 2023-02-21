import java.io.*;
//import java.util.ArrayList;
import java.time.LocalTime;

public class App {
    public static void main(String[] args) throws Exception {
        File fileOperation = new File("O:/Equipes/Électrolyse-(Secteur)/ABS/Stagiaire/Hiver 2023/opération_mse/pour_mes_test_2023_02_21.csv");
        File fileEquipement = new File("O:/Equipes/Électrolyse-(Secteur)/ABS/Stagiaire/Hiver 2023/Attribue/Liste antene et balise UWB-csv.csv");
        File fileRfidSural = new File("O:/Equipes/Électrolyse-(Secteur)/ABS/Stagiaire/Hiver 2023/données_csv/RFID_SURAL_2023_02_07.csv");
        /* 
        ReadFile rf = new ReadFile(fileEquipement, fileOperation,fileRfidSural);

        System.out.println(rf.getMSEOperationInfo().toString());
        System.out.println("------------------------------");
        System.out.println(rf.getAntenneLocation().get(1)[0].toString());
        System.out.println("------------------------------");
        */
        String str = "2023-02-21 12:22:10";
        LocalTime time = LocalTime.parse(str);
        time = time.minusMinutes(20) ;
        System.out.println(time);
    }

    
}
