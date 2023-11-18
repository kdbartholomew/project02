import java.util.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

class Floor {
    Queue<Passenger> upQueue;
    Queue<Passenger> downQueue;
    int floorNumber;

    public Floor(int floorNumber) {
        this.floorNumber = floorNumber;
        upQueue = new LinkedList<>();
        downQueue = new LinkedList<>();
    }

    // Method to generate passengers
    public void generatePassenger(int totalFloors, double passengerProbability, int currentTime) {
        Random rand = new Random();
        if (rand.nextDouble() < passengerProbability) {
            int destinationFloor;
            do {
                destinationFloor = rand.nextInt(totalFloors);
            } while (destinationFloor == this.floorNumber);

            Passenger passenger = new Passenger(destinationFloor, currentTime);
            if (destinationFloor > this.floorNumber) {
                upQueue.add(passenger);
            } else {
                downQueue.add(passenger);
            }
        }
    }

    // Method to load passengers onto the elevator
    public List<Passenger> loadPassengers(Queue<Passenger> queue, int capacity) {
        List<Passenger> loadedPassengers = new ArrayList<>();
        while (!queue.isEmpty() && loadedPassengers.size() < capacity) {
            loadedPassengers.add(queue.poll());
        }
        return loadedPassengers;
    }
}