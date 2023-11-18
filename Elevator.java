import java.util.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

// Elevator class
class Elevator {
    int currentFloor;
    int capacity;
    boolean goingUp;
    List<Passenger> passengers;

    public Elevator(int capacity) {
        this.currentFloor = 0;
        this.capacity = capacity;
        this.goingUp = true;
        passengers = new ArrayList<>();
    }

    // Method to load and unload passengers
    public void loadUnloadPassengers(Floor floor, int currentTime, List<Integer> travelTimes) {
        // Unload passengers
        Iterator<Passenger> it = passengers.iterator();
        while (it.hasNext()) {
            Passenger p = it.next();
            if (p.endFloor == floor.floorNumber) {
                travelTimes.add(currentTime - p.startTime);
                it.remove();
            }
        }

        // Load passengers
        Queue<Passenger> queue = goingUp ? floor.upQueue : floor.downQueue;
        passengers.addAll(floor.loadPassengers(queue, capacity - passengers.size()));
    }

    // Method to move the elevator
    public void move(List<Floor> floors) {
        if (goingUp && currentFloor < floors.size() - 1) {
            currentFloor++;
        } else if (!goingUp && currentFloor > 0) {
            currentFloor--;
        } else {
            goingUp = !goingUp;
        }
    }
}
