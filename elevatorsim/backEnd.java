/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elevatorsim;

import java.util.ArrayList;


/**
 *
 * @author User
 */
public class backEnd {
    private int current;
    private final int MIN_FLOOR = 0, MAX_FLOOR = 10;
    private int size,time,numberOfRequest;
    private boolean up = true;
    
    ArrayList<Passenger> sourceUp = new ArrayList<>();
    ArrayList<Passenger> sourceDown = new ArrayList<>();
    
    ArrayList<Passenger> destination = new ArrayList<>();
    
    ArrayList<Passenger> requestList = new ArrayList<>();
    
    ArrayList<Passenger> report = new ArrayList<>();
    
    
    
    public backEnd(){
        numberOfRequest = 0;
        current = MIN_FLOOR;
        time = 0;
    }
    
    public void setNumberOfRequest(int n){
        numberOfRequest = n;
    }
    
    public int getNumberOfRequest(){
        return numberOfRequest;
    }
    
    public void reduceNumberOfRequest(){
        numberOfRequest --;
    }
    
    public void doorOpen(){
        System.out.println(time + ": Door opening");
        for (int i = 0; i < 5; i++) {
            time++;
            checkRequest();
        }
        System.out.println(time + ": Door opened");
    }
    
    public void doorClose(){
        System.out.println(time + ": Door closing");
        for (int i = 0; i < 5; i++) {
            time++;
            checkRequest();
        }
        System.out.println(time + ": Door closed");
    }
    
    public void up(){
        current ++;
        checkRequest();
        time++;
    }
    
    public void down(){
        current --;
        time++;
        checkRequest();
    }
    
    public void boardUp(){
        for (int i = 0; i < 4; i++) {
            time++;
            checkRequest();
        }
        sourceUp.get(0).boardTime = time;
        destination.add(sourceUp.remove(0));
        sortSmallestDest(destination);
        System.out.println(time + ": 1 passenger(s) entered the elevator."); 
    }
    
    public void boardDown(){
        for (int i = 0; i < 4; i++) {
            time++;
            checkRequest();
        }
        sourceDown.get(0).boardTime = time;
        destination.add(sourceDown.remove(0));
        sortSmallestDest(destination);
        System.out.println(time + ": 1 passenger(s) entered the elevator."); 
    }

    
    public void leaveUp(){
        for (int i = 0; i < 4; i++) {
            time++;
            checkRequest();
        }
        destination.get(0).leaveTime = time;
        report.add(destination.remove(0));
        System.out.println(time + ": 1 passenger(s) left the elevator.");
    }
    
    public void leaveDown(){
        for (int i = 0; i < 4; i++) {
            time++;
            checkRequest();
        }
        destination.get(destination.size()-1).leaveTime = time;
        report.add(destination.remove(destination.size()-1));
        System.out.println(time + ": 1 passenger(s) left the elevator.");
    }
    
    public void checkRequest(){
        
        sortSmallestTime();
        if(requestList.isEmpty()){
            
        }
        else if(time >= requestList.get(0).requestTime){
            System.out.println(time + ": Service request (Request ID: "+ requestList.get(0).requestId + ") received from floor " + (requestList.get(0).sFloor == 0? "G":requestList.get(0).sFloor) + " to floor " + (requestList.get(0).dFloor == 0? "G" : requestList.get(0).dFloor));
            if(requestList.get(0).goUp){
                sourceUp.add(requestList.get(0));
                sortSmallestSource(sourceUp);
            }else{
                sourceDown.add(requestList.get(0));
                sortLargestSource(sourceDown);
            }
            requestList.remove(0); 
        }   
    }
     public void request(int id, int time, int sFloor, int dFloor){
        requestList.add(new Passenger(id, time, sFloor, dFloor));      
     }
     
//    public boolean noRequest(){
//        boolean exit = false;
//        if(requestList.isEmpty() && destination.isEmpty() && source.isEmpty()){
//            exit = true;
//        }
//        return exit;
//    }
//    
//    public int checkSource(int n ){ //can add more parameter to continue where it stop b4
//        int i = 0;
//        while(source.get(i).sFloor != n ){
//            i++;
//        }
//        return i;
//    }
//    
//    public int checkDest(int n){
//        int i = 0;
//        while(destination.get(i).dFloor != n){
//            i++;
//        }
//        return i;
//    }
//    
//    public boolean goingUp(){
//        //error here
//        if(current.floor == 11 || destination.isEmpty() /*|| current.floor > source.get(largestSource()).sFloor*/ ){
//            up = false;
//        }
//        else if(current.floor == 0 || destination.isEmpty() /*|| current.floor < source.get(source.size()-1).sFloor*/){
//            up = true;
//        }
//        return up;
//    }
//    
    public void sortSmallestTime(){
        
        for(int i = 0; i < requestList.size() - 1; i++ ){
            Passenger min = requestList.get(i);
            int minIndex = i;
            for (int j = i+1; j < requestList.size(); j++) {
                if(min.requestTime > requestList.get(j).requestTime){
                    min = requestList.get(j);
                    minIndex = j;
                }    
            }
            if(minIndex != i){
                requestList.set(minIndex,requestList.get(i));
                requestList.set(i,min);
            }
        }
    }
    
    public void sortSmallestSource(ArrayList<Passenger> arr){
        
        for(int i = 0; i < arr.size() - 1; i++ ){
            Passenger min = arr.get(i);
            int minIndex = i;
            for (int j = i+1; j < arr.size(); j++) {
                if(min.sFloor > arr.get(j).sFloor){
                    min = arr.get(j);
                    minIndex = j;
                }    
            }
            if(minIndex != i){
                arr.set(minIndex,arr.get(i));
                arr.set(i,min);
            }
        }
    }
        
    public void sortLargestSource(ArrayList<Passenger> arr){
     
        for (int i = 0; i < arr.size() - 1; i++) {
            Passenger max = arr.get(i);
            int maxIndex = i;
            for (int j = i+1; j < arr.size(); j++) {
                if(max.sFloor < arr.get(j).sFloor){
                    max = arr.get(j);
                    maxIndex = j;
                }
            }
            if(maxIndex != i){
                arr.set(maxIndex, arr.get(i));
                arr.set(i, max);
            }      
        }
    }   
    
    
     public void sortSmallestDest(ArrayList<Passenger> arr){
        
        for(int i = 0; i < arr.size() - 1; i++ ){
            Passenger min = arr.get(i);
            int minIndex = i;
            for (int j = i+1; j < arr.size(); j++) {
                if(min.dFloor > arr.get(j).dFloor){
                    min = arr.get(j);
                    minIndex = j;
                }    
            }
            if(minIndex != i){
                arr.set(minIndex,arr.get(i));
                arr.set(i,min);
            }
        }
    }       
    
    public void sortSmallestId(){ 
        for(int i = 0; i < report.size() - 1; i++ ){
            Passenger min = report.get(i);
            int minIndex = i;
            for (int j = i+1; j < report.size(); j++) {
                if(min.requestId > report.get(j).requestId){
                    min = report.get(j);
                    minIndex = j;
                }    
            }
            if(minIndex != i){
                report.set(minIndex,report.get(i));
                report.set(i,min);
            }
        }
    }
         
     public int getTime(){
         return time;
     }
     
     public int getCurrentFloor(){
         return current;
     }
     
     public void addTime(){
         time++;
         checkRequest();
     }
     public void setUp(boolean a){
         up = a;
     }
     public boolean getUp(){
         return up;
     }
}
