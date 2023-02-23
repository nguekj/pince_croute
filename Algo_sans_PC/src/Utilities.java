import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

abstract class Utilities {
    public static String MseOperationFileDate;

    public static LocalDateTime convertMseTimeToDateTime(String str ){
        
        String opDatetime = MseOperationFileDate+" "+str;

        LocalDateTime dt = LocalDateTime.parse(opDatetime,
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

    public boolean isInShiftNightRange(LocalDateTime inputDateTime, LocalDateTime startDateTime, LocalDateTime endDateTime){
        LocalDateTime opStart = LocalDateTime.parse(this.MseOperationFileDate+" "+"20:00:00",
        DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm:ss")).minusDays(1).minusMinutes(20);
        LocalDateTime opEnd = LocalDateTime.parse(this.MseOperationFileDate+" "+"08:00:00",
        DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm:ss"));

        if(inputDateTime.compareTo(opStart)>=0 && inputDateTime.compareTo(opEnd)<=0){
            if(inputDateTime.compareTo(startDateTime.minusDays(1))>=0 && inputDateTime.compareTo(endDateTime)<=0)
            {
                return true;
            }
        }

        return false;
    }

    public boolean isInShiftDayRange(LocalDateTime inputDateTime, LocalDateTime startDateTime, LocalDateTime endDateTime){
        LocalDateTime opStart = LocalDateTime.parse(this.MseOperationFileDate+" "+"08:00:00",
        DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm:ss")).minusMinutes(20);
        LocalDateTime opEnd = LocalDateTime.parse(this.MseOperationFileDate+" "+"20:00:00",
        DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm:ss"));

        if(inputDateTime.compareTo(opStart)>=0 && inputDateTime.compareTo(opEnd)<=0){
            if(inputDateTime.compareTo(startDateTime.minusDays(1))>=0 && inputDateTime.compareTo(endDateTime.minusDays(1))<=0)
            {
                return true;
            }
            
        }

        return false;
    }
}

class Util extends Utilities{

}
