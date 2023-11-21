import java.util.Properties;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Floor{
    //hold passengers in queues that either go up or down
    //floor lets go of passenger in queue waiting for elevator
    //generates and stores passengers on floors
    java.util.Deque<Passenger> up;
    Deque<Passenger> down;
    double passengerProbability;
    int currentFloor;

//floor constructor checks if linked or array structure and creates new up and down ordered structures containing passenegers
    public Floor(double passengerProbability, String structures, int currentFloor){// floor?
        this.passengerProbability = passengerProbability;
        this.currentFloor = currentFloor; //does this set to nulll? or the one in elev class?
        if(structures == "linked"){
            up = new java.util.LinkedList<Passenger>();
            down = new java.util.LinkedList<Passenger>();
        }
        else{
            up = new ArrayDeque<Passenger>();
            down = new ArrayDeque<Passenger>();

        }
    }

    //generatePassenger initialized an array of integers the same size as the number of floors
    //initializes a random value

    public void generatePassenger(int floors, int currentTick, int currentFloor){
        int[] arr = new int[floors-1];
        Random random = new Random();
        for(int i = 0; i < floors-1; i++){//populates array with floor numbers
            if(i != currentFloor) {
                arr[i] = i;
            }
        }

        int endFloor = arr[random.nextInt(0, arr.length)];//get random next floor
        Passenger pass = new Passenger(endFloor, currentTick);//pass object created of type passenger, contains end floor(randomly generated) and current tick
        if(endFloor > currentFloor){//if generated passengers end floor is greater than its current floor its going up
            up.add(pass);// so we add it to up deque (either array or linked list, depending on structures value in property file)
        }
        else{//if generated passengers end floor is greater than its current floor its going down
            down.add(pass);//so we add it to down deque (either array or linked list, depending on structures value in property file)
        }

    }//end of genpass
    public Passenger unloadUp(){//
        return up.remove();//removes passenger at head of  up deque
    }
    public Passenger unloadDown(){
        return down.remove();//removes passenger at head of down deque
    }//end of load

}//end of Floor class


