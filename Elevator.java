
//up and down vs stopUp and stopDown - stops are passengers in elevators, up & down are passengers in floors
//stop up and stop down are priority queues of passengers initialized in floor class// where do we populate stopupand stopdown? are they for people in the elevators
//up and down are of type defined by structure, defined in floor class// keeps track of generated and unloaded passengers
//are end times and passengers paired?
//line 103 in elev sim

//big fat fucking questions about load unload and travel logic

import java.util.*;
import java.io.FileReader;
import java.io.IOException;

public class Elevator{
    private int currentFloor;
    private int maxCapacity; //the max number of people the elevator can hold, determined by the property file

    private Deque<Integer> endTimes;

    private boolean isUp;
    private PriorityQueue<Passenger> stopUp;
    private PriorityQueue<Passenger> stopDown;

    //elevator gets a max capacity of passengers and a structure type
    //current floor is set to 0 , is up is initialized to true// elevators spawn at groundfloor
    public Elevator(int maxCapacity, String structures){
        this.currentFloor = 0;
        this.isUp = true;
        this.maxCapacity = maxCapacity;
        stopUp = new PriorityQueue<Passenger>();//creating a priority queue of passengers going up
        stopDown = new PriorityQueue<Passenger>(Collections.reverseOrder());//creating a priority queue of passengers going down??
        if(structures.equals("linked")){
            endTimes = new java.util.LinkedList<Integer>(); //creates a linked list of end times if linked
        }
        else{
            endTimes = new ArrayDeque<Integer>(); //creates an arraydeque of end times if array
        }
    }// end of elevator constructor



//loadUnload happens when elevator is stopped, takes current tick to calculate end time and current floor to determine whether to stop
    //first two if statements deal with unloading
        //let the off on correct floor
    //last in and else deal with loading
        //same direction, i have room in the elev, and
    public void loadUnload(int currentTick, Floor currentFloor){ //floor or int
        //if head of stop up (a priority queue of passengers going up) is not null and their end floor is the current floor
        // they get out and we calculate their end time and add it to endTimes
        if(stopUp.peek()!= null && stopUp.peek().endFloor == currentFloor.currentFloor){//unloading //current floor is int(inside elevator class,  end floor is also int (inside floor class)
            Passenger tempPass = stopUp.poll();//GET OUT
            endTimes.add(currentTick - tempPass.startTime);
        }
        //if head of stopDown( a priority queue of passengers going down) is not null and their end floor is current
        //then they get out and we calcluate their end time and add it to endTimes
        if(stopDown.peek()!= null && stopDown.peek().endFloor == currentFloor.currentFloor){//unloading
            Passenger tempPass = stopDown.poll();//GET OUT
            endTimes.add(currentTick - tempPass.startTime);
        }
        //WHAT IS HAPPENING? *see below
        //if we are going up, and while up (a priority queue of passengers going up) is empty
        //and if the head of up is not null and we are still going up
            //note: up and down exist on each floor
            //stop up and stopdown exist in each elevator
        //line 64 lowkey redundant but yolo
        if(isUp == true){
            while(!currentFloor.up.isEmpty()){//if we are still going up and the deque of people on the current floor who want to go up is not empty
                if(currentFloor.up.peekFirst()!= null && currentFloor.up.peekFirst().endFloor > currentFloor.currentFloor){// and their end floor is higher than our current floor
                    //check max capcity
                    //System.out.println("HII");
                    if(stopUp.size()==maxCapacity) { //check theres room to add them into elevator
                        break; //we stop loading if at max capacity!
                    }
                    stopUp.add(currentFloor.unloadUp()); //if we are nit at max capcity then we add them into the elevator heap of people going up
                }
            }
        }
        else{ //theyre going down
            while(!currentFloor.down.isEmpty()){//there are people going down
                if(currentFloor.down.peekFirst()!= null && currentFloor.down.peekFirst().endFloor < currentFloor.currentFloor){//and theyre going down relative to the current floor
                    if(stopDown.size()<maxCapacity) { //check theres room to add them into elevator
                        break; //we stop loading if at max capcacity
                    }
                    stopDown.add(currentFloor.unloadUp()); //then we add them into the elevator heap of people going down
                }
            }
        }

    }//end of loadUnload

    //travel logic:
    public void travel(Deque<Floor> floorDeque){ //int[] or Floor[]?
        if(!stopUp.isEmpty()) { //puts priority on people in elev
//keeps goin up until the up-queue is empty or at max capacity
            int i = 0; //help checking if weves going more than a range of 5 floors
            while ((getCurrentFloor(floorDeque, currentFloor).up.isEmpty() || this.stopUp.size() == maxCapacity) && (currentFloor < floorDeque.size() -1 && i < 5)) { //not stopping at current floor if theres no one waiting on that floor or if at capacity and if within range
                //go up

                currentFloor++;
                i++;
            }
        }
        else if(!stopDown.isEmpty()){//if theres people in the lev wanting to gown
            int i = 0;
            while((getCurrentFloor(floorDeque, currentFloor).down.isEmpty() || this.stopDown.size()==maxCapacity) && (currentFloor>0 && i < 5)){//not stopping if no one on current floor wants to go down or if full
                currentFloor--;
                i++;
            }
        }
        else{//elevator is empty; check floors and count who gets in
            int counter = 1;
            //while cuurent floors up or down is empty
            //if line to go up and down on this floor are both empty then go to neighboring floors and check
            while((getCurrentFloor(floorDeque, currentFloor).up.isEmpty() && getCurrentFloor(floorDeque, currentFloor).down.isEmpty()) && (currentFloor> 0 && currentFloor < floorDeque.size() && counter <= 5)){
                //bug
                //if one of the motherfuckers on the floor above is not empty find out which
                if(!getCurrentFloor(floorDeque, currentFloor+counter).up.isEmpty() || !getCurrentFloor(floorDeque, currentFloor+counter).down.isEmpty()){
                    if(!getCurrentFloor(floorDeque, currentFloor+counter).up.isEmpty()){
                        currentFloor = currentFloor+counter;
                        isUp=true;
                    }//priority to up
                    else{//down must be not empty
                        currentFloor = currentFloor+counter;
                        isUp =false;
                    }
                }
                else if (!getCurrentFloor(floorDeque, currentFloor-counter).up.isEmpty() || !getCurrentFloor(floorDeque, currentFloor-counter).down.isEmpty()){//check floor below
                    if(!getCurrentFloor(floorDeque, currentFloor-counter).up.isEmpty()){
                        currentFloor = currentFloor-counter;
                        isUp=true;
                    }//priority to up
                    else{//down must be not empty
                        currentFloor = currentFloor-counter;
                        isUp= false;
                    }
                }
            counter++;

            }//end of while
            }
        }
//ian said: go thru subsets of the code, pseudo code for travel,, elev movement for each tick...keep track of which direction
        //pseudocode for simulation
    //i am travel. i take a deque of floors
        //i want to take requests from passengers... i can be called(outside) or sent(inside)
        //i want to prioritize the most recent call
            //if the most recent call is greater than the floor im currently on then im going up
            //



    //   for the floors that are larger than the floor im already on  (going up)

        //for the floors that are smaller than the floor im already on    (going down)
        //if my elevator is not



    //}//end of travel
    // public requestStop(){

    // }//end of requestStop
    public Deque<Integer> getEndTimes(){
        return endTimes;
    }
    public Floor getCurrentFloor( Deque<Floor> floorDeque, int floorIndex){
        int i = 0;
        for(Floor floor : floorDeque) {
            if (i == floorIndex) {
                return floor;
            }
            i++;
        }
        throw new RuntimeException("couldnt find current floor");
    }

}//end of elevator class







//questions:
// report stop?
//red highlight in passenger class variables
//line 254







//questions:
// report stop?
//red highlight in passenger class variables
//line 254




