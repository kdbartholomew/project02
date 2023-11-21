import java.util.Properties;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
public class Passenger implements Comparable<Passenger> {
    public int endFloor; //BUGGGGGGGGGGGGG
    public int startTime; //is tick

    public Passenger(int endFloor, int startTime){
        this.endFloor = endFloor;
        this.startTime = startTime;

    }//end of passenger method
    public int compareTo(Passenger p){
        return Integer.compare(this.endFloor, p.endFloor);
    }

}//end of class passenger
