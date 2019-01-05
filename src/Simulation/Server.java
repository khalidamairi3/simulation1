package Simulation;

import java.io.BufferedOutputStream;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Server {
	
	public int id;
	public int status;
	public int requestCounter;
	public Request requestBeingExecuted;
	public double busyTime;
	public double breakDownTime;
	public double repairingTime;
	public double finishRepairingTime;
	public boolean breakDownStatus;
	
	
	Server(int id){
		this.id=id;
		this.status=Constants.STATUS_AVAILABLE;
		this.requestCounter=0;
		this.requestBeingExecuted=null;
		this.busyTime=0.0;
		this.breakDownTime=-1.0;
		this.finishRepairingTime=-1.0;
		this.breakDownStatus=false;
	}
	
	@Override
	public String toString() {
		return "\n_______\n" +
			   "id               :"+ id + "\n" +
			   "status           :"+ status + "\n" +
			   "requestCounter           :"+ requestCounter + "\n" +
			   "busyTime           :"+ busyTime + "\n" +
			   "breakDownTime           :"+ breakDownTime + "\n" +
			   "repairingTime           :"+ repairingTime + "\n" +
			   "finishRepairingTime           :"+ finishRepairingTime + "\n" +
			   "_______\n";
	}
}
