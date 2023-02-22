import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

abstract class Utilities {
    
    public LocalDateTime convertMseTimeToDateTime(String str, String fileRfidSural ){
        String[] fileDate = fileRfidSural.split("RFID_SURAL_")[1].split(".csv");
        String rfidDatetime = fileDate[0].replace('_','-')+" "+str;
        LocalDateTime dt = LocalDateTime.parse(rfidDatetime,
        DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm:ss"));
        return dt;
    }

    public LocalDateTime convertStringToDateTime(String str){
        LocalDateTime dt = LocalDateTime.parse(str,
        DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm"));
        return dt;
    }
    
    public String convertDateTimeToString(LocalDateTime dt){
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedDate = dt.format(myFormatObj);
        return formattedDate;
    }
}

class Util extends Utilities{

}
