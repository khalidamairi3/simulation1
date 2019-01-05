package Simulation;

import java.math.BigInteger;

public class Constants {


		public static final double SERVICE_RATE = 5.0;
		public static final double ARRIVAL_RATE = 5.0;
		
		
		public static final double MEAN_OF_FAILURE=0.1;
		public static final double BETA=1.0;
		public static final double V=0.0;
		
		public static final double LOGNORMAL_MEAN=2.0;
		public static final double LOGNORMAL_VARIANCE=1.0;
		
		public static final int SYSTEM_CAPACITY = 500;
		
		public static final boolean BREAKDOWN_ACTIVATED = true;
		public static final boolean Limited_SYSTEM_CAPACITY = true;
		public static final int MAX_SIMULATION_TIME = 30; 	
	
		public static final int REUESTS_NUMBER = 5;						
		public static final int STATUS_WAITING_IN_LOADBALANCER = 1;
		public static final int STATUS_RUNNUNG = 2;
		public static final int STATUS_FINISHED = 3;
		public static final int STATUS_DROPPED = 4;
		
		
		public static final int SERVERS_NUMBER = 2;
		public static final int STATUS_AVAILABLE = 0;
		public static final int STATUS_FULL = 1;
		public static final int STATUS_BREAKDOWN = 2;
		public static final int STATUS_REPAIR = 3;
}

