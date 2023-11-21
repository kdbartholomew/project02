import java.util.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.Deque;

public class ElevatorSimulation{

    private static String structures;
    private static int floors;
    private static double passengerProbability;
    private static int elevators;
    private static int elevatorCapacity;
    private static int duration;
    //static Floor[] floorArray = new Floor[floors];
    //static Elevator[] elevArray = new Elevator[elevators];//elevators or e.elevator
   //private List<Elevator> elevatorList;
   // private List<Floor> floorList;

    private static Deque<Floor> floorDeque;
    private static Deque<Elevator> elevDeque;
    private static int currentTick = 0; //is this a problem?
    private Random random;

    public ElevatorSimulation (String filename) {
        try{

            Properties p = new Properties();

            FileReader reader = new FileReader(filename);

            p.load(reader); //reads property file and then loads into p

            structures = p.getProperty("structures");
            floors = Integer.parseInt(p.getProperty("floors"));
            //floorDeque = new Floor[floors];
            passengerProbability = Double.parseDouble(p.getProperty("passengers"));
            elevators = Integer.parseInt(p.getProperty("elevators"));
           // elevArray = new Elevator[elevators];
            elevatorCapacity = Integer.parseInt(p.getProperty("elevatorCapacity"));
            duration = Integer.parseInt(p.getProperty("duration"));

            reader.close();
        }//end of try block
        catch (IOException e){
            System.err.println("error reading the property file" + e.getMessage());

        }//end of catch block
        //System.out.println("after catch"+ structures + floors + passengerProbability+ elevators+ elevatorCapacity+ duration);
        //elevatorList = new ArrayList<Elevator>();
        //floorList = new ArrayList<>();
        random = new Random();

//        // Initialize floors and elevators
//        for (int i = 0; i < floors; i++) {
//            floorList.add(new Floor(passengerProbability, structures, i));
//        }
//
//        for (int i = 0; i < elevators; i++) {
//             elevatorList.add(new Elevator(elevatorCapacity, structures));
//         }
    }

    public static void main(String[] args) {
        ElevatorSimulation e = new ElevatorSimulation(args[0]);
        //System.out.println(floors);

        if(structures.equals("linked")){
            floorDeque = new LinkedList<Floor>();
            elevDeque = new LinkedList<Elevator>();
        }
        else{
            floorDeque = new ArrayDeque<Floor>();
            elevDeque = new ArrayDeque<Elevator>();

        }
        //creates a deque of floors
        for(int f = 0; f < floors; f++){//floors or e.floor?
            Floor newFloor = new Floor(passengerProbability, structures, f);
            floorDeque.addLast(newFloor);
        }
        //creates a deque of elevators
        for(int el = 0; el < elevators; el++){
            Elevator newElev = new Elevator(elevatorCapacity, structures);
            elevDeque.addLast(newElev);
        }
//handles the simulation:
        //while the current tick is less than the duration of the simulation
            //for each floor in the deque of floors
                //generate a passenger maybe on that floor
        for(currentTick = 0; currentTick < duration; currentTick++){
            int currFloor = 0;
            for(Floor currentFloor : floorDeque){
                //sets an int value for current floor bc we need to pass this to generate passengers in the next line as an int
                currentFloor.generatePassenger(floors, currentTick, currFloor); //on each current floor i generate (maybe) a passenger
                currFloor++;
            }
            //and for each elevator , for each current floor(scope is just here, youre fine) load or unload and travel
            for(Elevator currentElev : elevDeque) {
                    for(Floor currentFloor : floorDeque){
                        currentElev.loadUnload(currentTick, currentFloor);//defined in elevator class as int
                        currentElev.travel(floorDeque); //does travel take current floor?
                    }
                }
        }
//calculating runtimes for simulation
        int smallest = duration +1;
        int biggest = 0;
        int numerator=0;
        int count=0;

        //i am merging the for loop into a for each loop... i think im ok to poll because at this point in the code i have already run the simulation
        Deque<Integer> elevTimesArr;
        for(Elevator currentElev : elevDeque){//variable scope, youre fine
            elevTimesArr  = elevDeque.poll().getEndTimes();//is this the correct deque method? i wanna get the current,, idk if i wanna remove
            count+=elevTimesArr.size();
            while(!elevTimesArr.isEmpty()){ //when would it ever be empty? BECAUSE we are using poll babe
                int currentTime = elevTimesArr.poll(); //poll gets and removes head
                numerator = numerator + currentTime;
                if(currentTime<smallest){//initially nothing will be less than smallest
                    smallest = currentTime;
                }
                if(currentTime>biggest){
                    biggest = currentTime;
                }
            }
        }
        int averageTime = numerator/count; //calculates average
        //prints runtimes
        System.out.println("smallest: "+ smallest);
        System.out.println("biggest: "+ biggest);
        System.out.println("average: "+ averageTime);


    }//end of main

}//end of ElevatorSimulation
