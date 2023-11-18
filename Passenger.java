import java.util.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

class Passenger implements Comparable<Passenger> {
    int endFloor;
    int startTime;

    public Passenger(int endFloor, int startTime) {
        this.endFloor = endFloor;
        this.startTime = startTime;
    }

    @Override
    public int compareTo(Passenger other) {
        return Integer.compare(this.endFloor, other.endFloor);
    }
}