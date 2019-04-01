/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elevatorsim;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author User
 */
public class ElevatorSim {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        boolean up = true, moveable = true;
        backEnd elevator = new backEnd();
        Scanner input = new Scanner(System.in);
        System.out.println("Enter number of request: ");
        int n = input.nextInt();
        int srp = n;
        int floorTravelled = 0, passengerServed = 0,breakDownTime = 0; 
        elevator.setNumberOfRequest(n);
        for (int i = 0; i < n; i++) {
            elevator.request(input.nextInt(),input.nextInt(),input.nextInt(),input.nextInt());
        }
        
        while(elevator.getNumberOfRequest() > 0){
            
            //checking purpose
            //System.out.println("Number of Request: " + elevator.getNumberOfRequest());
            //System.out.println("SourceUp: " + elevator.sourceUp.toString());
            //System.out.println("SourceDown: " + elevator.sourceDown.toString());
            //if(!elevator.sourceDown.isEmpty())
            //System.out.println("SourceDown: " + elevator.sourceDown.get(0).sFloor);
            //System.out.println("Destination: " + elevator.destination.toString());
            //System.out.println("RequestList: " + elevator.requestList.toString());
            //System.out.println("up:" + up);
            //System.out.println("Current floor: " + elevator.getCurrentFloor() );
            //System.out.println("Time: " + elevator.getTime());
            
            
            elevator.checkRequest();
            //break down
            Random rand = new Random();
            int breakDown = rand.nextInt(1000);
            if(breakDown <= 10){
                System.out.println(elevator.getTime() + ": Elevator break down");
                for (int i = 0; i < 5; i++) {
                    elevator.addTime();   
                }
                breakDownTime++;
                continue;
            }
            //when time = 0 and first source floor is 0
            if(!elevator.sourceUp.isEmpty()){
                if(elevator.getTime() == 0 && elevator.getCurrentFloor() == elevator.sourceUp.get(0).sFloor){
                    elevator.doorOpen();
                    elevator.boardUp();
                    elevator.doorClose();
                    System.out.println(elevator.getTime() + ": Heading to floor " + (elevator.destination.get(0).dFloor == 0? "G" : elevator.destination.get(0).dFloor));
                    moveable = false;
                }
            }
            
            // when time = 0 and no request accepted yet
            if(elevator.getTime() == 0 && elevator.sourceDown.isEmpty() && elevator.sourceUp.isEmpty()){
                elevator.addTime();
            }
  
            //when reached source
            if(up && !elevator.sourceUp.isEmpty()) {
                //System.out.println("a");  //checking
                if(elevator.getCurrentFloor() == elevator.sourceUp.get(0).sFloor){
                    System.out.println(elevator.getTime() + ": Reached floor " + (elevator.getCurrentFloor() == 0? "G" : elevator.getCurrentFloor()));
                    elevator.doorOpen();
                    elevator.boardUp();
                    elevator.doorClose();
                    moveable = false;
                    if(!elevator.destination.isEmpty()){
                        System.out.println(elevator.getTime() + ": Heading to floor " + (elevator.destination.get(0).dFloor == 0? "G" :elevator.destination.get(0).dFloor));
                    }
                }
            }
            else if(!up && !elevator.sourceDown.isEmpty()){
                //System.out.println("b"); //checking
                if(!up && elevator.getCurrentFloor() == elevator.sourceDown.get(0).sFloor){
                    System.out.println(elevator.getTime() + ": Reached floor " + (elevator.getCurrentFloor() == 0? "G" : elevator.getCurrentFloor()));
                    elevator.doorOpen();
                    elevator.boardDown();
                    elevator.doorClose();
                    moveable = false;
                    if(!elevator.destination.isEmpty()){
                        System.out.println(elevator.getTime() + ": Heading to floor " + (elevator.destination.get(elevator.destination.size()-1).dFloor == 0? "G" : elevator.destination.get(elevator.destination.size()-1).dFloor ));
                    }
                }   
            }
            //when reach destination
            if(!elevator.destination.isEmpty()){    
                //System.out.println("c"); //checking
                if(up && elevator.getCurrentFloor() == elevator.destination.get(0).dFloor){
                    System.out.println(elevator.getTime() + ": Reached floor " + (elevator.getCurrentFloor() == 0? "G" : elevator.getCurrentFloor() ));
                    elevator.doorOpen();
                    elevator.leaveUp();
                    elevator.doorClose();
                    elevator.report.get(elevator.report.size()-1).utilizedTime = elevator.getTime();
                    passengerServed ++;
                    moveable = false;
                    if(!elevator.destination.isEmpty()){
                        System.out.println(elevator.getTime() + ": Heading to floor " + (elevator.destination.get(0).dFloor == 0? "G" : elevator.destination.get(0).dFloor));
                    }
                    elevator.reduceNumberOfRequest();
                }else if(!up && elevator.getCurrentFloor() == elevator.destination.get(elevator.destination.size()-1).dFloor){
                    System.out.println(elevator.getTime() + ": Reached floor " + (elevator.getCurrentFloor() == 0? "G" : elevator.getCurrentFloor()));
                    elevator.doorOpen();
                    elevator.leaveDown();
                    elevator.doorClose();
                    elevator.report.get(elevator.report.size()-1).utilizedTime = elevator.getTime();
                    passengerServed ++;
                    moveable = false;
                    elevator.reduceNumberOfRequest();
                    if(!elevator.destination.isEmpty()){
                        System.out.println(elevator.getTime() + ": Heading to floor " + (elevator.destination.get(elevator.destination.size()-1).dFloor == 0? "G" : elevator.destination.get(elevator.destination.size()-1).dFloor));
                    }
                }              
            }
            
            // moving
            if(moveable){
                if(up){
                        //System.out.println("MU");
                        elevator.up();
                        floorTravelled ++;
                    }else{
                        //System.out.println("");
                        elevator.down();
                        floorTravelled ++;
                }
            }
             //when to change direction
            if(up){
                //System.out.println("d"); //checking
                if(!elevator.sourceUp.isEmpty()){
                    if((elevator.sourceUp.get(0).sFloor < elevator.getCurrentFloor() && elevator.destination.isEmpty()) || elevator.getCurrentFloor() == 10)
                        up = false;
                }else if(elevator.sourceUp.isEmpty() && !elevator.sourceDown.isEmpty() && elevator.destination.isEmpty()){
                    if(elevator.getCurrentFloor() >= elevator.sourceDown.get(0).sFloor){
                        up = false;
                    }
                }
            }else{
                //System.out.println("e"); //checking
                if(!elevator.sourceDown.isEmpty()){
                    if((elevator.sourceDown.get(0).sFloor > elevator.getCurrentFloor() && elevator.destination.isEmpty()) || elevator.getCurrentFloor() == 0)
                        up = true;
                }else if(elevator.sourceDown.isEmpty() && !elevator.sourceUp.isEmpty() && elevator.destination.isEmpty()){
                    if(elevator.getCurrentFloor() <= elevator.sourceUp.get(0).sFloor){
                        up = true;
                    }
                }
            }
            
            // no request and destination
            if(elevator.sourceUp.isEmpty() && elevator.sourceDown.isEmpty() && elevator.destination.isEmpty()){
                elevator.addTime();
            }  
            
            moveable = true;
        }
        
        elevator.sortSmallestId();
        try{
            PrintWriter output = new PrintWriter(new FileOutputStream("log.txt")); 
            output.println(" ### Elevator Statistics ###");
            output.println(" Service request processed: " + srp);
            output.println(" Passengers served: " + passengerServed);
            output.println(" Total floors travelled: " + floorTravelled);
            output.println(" Total time taken: " + (elevator.getTime()-1));
            output.println("");
            if(breakDownTime > 0){
                output.println(" Amount of break down : " + breakDownTime);
                output.println(" Total time suffered break down: " + (breakDownTime*5));
            }
            output.println("");
            for (int i = 0; i < passengerServed; i++) {
                output.println(" ## Request ID - " + elevator.report.get(0).requestId + " ##");
                output.println(" Request time: " + elevator.report.get(0).requestTime);
                output.println(" Boarding time: " + elevator.report.get(0).boardTime);
                output.println(" Leaving time: " + elevator.report.get(0).leaveTime);
                output.println(" Total on board time: " + (elevator.report.get(0).leaveTime -  elevator.report.get(0).boardTime) );
                output.println(" Total time involved (From request time until leave the elevator): " + (elevator.report.get(0).leaveTime - elevator.report.get(0).requestTime));
                output.println(" Total time utilized: " + (elevator.report.get(0).utilizedTime - elevator.report.get(0).requestTime));
                output.println("");
                elevator.report.remove(0);
            }
            
            output.close();
            
        }catch(IOException ex){
            System.out.println("Problem with file output");
        }
        ReportFile a = new ReportFile();
        a.setVisible(true);

    }    
}
