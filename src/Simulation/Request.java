package Simulation;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;





public class Request {
	
	public int id;
	public double progress; //complete percentage
	public int status;// waitingInServer  running finished dropped
	public double arrivalTime;
	public double serviceTime;
	public double startPrcocessingTime;
	public double finishTime;
	public int serverId;
	
	
	public Request(int id,double arrivalTime,double serviceTime) {
		this.id=id;
		this.progress=0.0;
		this.status = Constants.STATUS_WAITING_IN_LOADBALANCER;
		this.arrivalTime=arrivalTime;
		this.serviceTime=serviceTime;
	}
	
	@Override
	public String toString() {
		//serviceTime instead of weight
		return "\n ________________ \n"+ 
		       "id         :" + id + "\n"+
			   "progress   : " +progress + "\n" +
			   "status 	   : " +status + "\n"+
			   "arrivalTime 	   : " +arrivalTime + "\n"+
			   "serviceTime 	   : " +serviceTime + "\n"+
			   "startPrcocessingTime 	   : " +startPrcocessingTime + "\n"+
			   "finishTime : " +finishTime+"\n"+
			   "Sever id   :"  + serverId + "\n"+
			   " ________________ \n"
			   ;
	}
}
