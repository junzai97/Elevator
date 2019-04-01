/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package elevatorsim;

/**
 *
 * @author User
 */
public class Passenger {
    int requestTime,requestId,sFloor, dFloor,boardTime,leaveTime,utilizedTime;
    boolean goUp;
    
    public Passenger(int id, int time, int source, int dest){
        requestTime =  time;
        requestId = id;
        sFloor = source;
        dFloor = dest;
        boardTime = 0;
        leaveTime = 0;
        utilizedTime = 0;
        if(source < dest){
            goUp = true;
        }
        else if(source > dest){
            goUp = false;
        }
    }
}
