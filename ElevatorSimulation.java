import java.util.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

// Main class for the simulation
public class ElevatorSimulation {
    static int floors;
    static double passengerProbability;
    static int elevators;
    static int elevatorCapacity;
    static int duration;

    public static void main(String[] args) {
        // Load properties
        Properties properties = new Properties();
        try {
            FileReader reader = args.length > 0 ? new FileReader(args[0]) : null;
            if (reader != null) {
                properties.load(reader);
            }
        } catch (IOException e) {
            System.err.println("Error reading properties file: " + e.getMessage());
        }

        // Use default values if properties are missing
        floors = Integer.parseInt(properties.getProperty("floors", "32"));
        passengerProbability = Double.parseDouble(properties.getProperty("passengers", "0.03"));
        elevators = Integer.parseInt(properties.getProperty("elevators", "1"));
        elevatorCapacity = Integer.parseInt(properties.getProperty("elevatorCapacity", "10"));
        duration = Integer.parseInt(properties.getProperty("duration", "500"));

        // Initialize floors and elevators
        List<Floor> floorList = new ArrayList<>();
        for (int i = 0; i < floors; i++) {
            floorList.add(new Floor(i));
        }

        List<Elevator> elevatorList = new ArrayList<>();
        for (int i = 0; i < elevators; i++) {
            elevatorList.add(new Elevator(elevatorCapacity));
        }

        // Run the simulation
        List<Integer> travelTimes = new ArrayList<>();
        for (int currentTime = 0; currentTime < duration; currentTime++) {
            // Generate passengers
            for (Floor floor : floorList) {
                floor.generatePassenger(floors, passengerProbability, currentTime);
            }

            // Elevator operations
            for (Elevator elevator : elevatorList) {
                elevator.loadUnloadPassengers(floorList.get(elevator.currentFloor), currentTime, travelTimes);
                elevator.move(floorList);
            }
        }

        // Calculate and report results
        int totalTime = travelTimes.stream().mapToInt(Integer::intValue).sum();
        int averageTime = totalTime / travelTimes.size();
        int longestTime = Collections.max(travelTimes);
        int shortestTime = Collections.min(travelTimes);

        System.out.println("Average time: " + averageTime);
        System.out.println("Longest time: " + longestTime);
        System.out.println("Shortest time: " + shortestTime);
    }
}