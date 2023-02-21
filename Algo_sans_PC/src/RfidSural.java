import java.time.LocalTime;

public class RfidSural {
    protected String readerId;
    protected String tagId;
    protected String distance;
    protected String timestamp;
    
    public RfidSural(String rowcsv){
        String[] line = null;
        line = rowcsv.split(";");
        this.readerId = line[0];
        this.tagId = line[1];
        this.distance = line[2].replace(',', '.');
        this.timestamp = line[3];
    }
    public void removeAllPC(){

    }
    
}
