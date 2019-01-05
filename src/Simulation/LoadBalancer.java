package Simulation;

import java.io.BufferedInputStream;


import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;



public class LoadBalancer {
	
	int currentRequestToBeChekedInAllRequests;
	double currentTime; //the current global time of the whole simulation
	LinkedList<Request> requestsQueue; // the realistic queue
	LinkedList<Request> allRequests; // for statistics purposes, keeping info of all requests
	LinkedList<Server> servers;
	LinkedList<Server> BrokenServers;
	public double meanQueueLength;
	public double nextEventTime;

	
	public LoadBalancer() {
		currentRequestToBeChekedInAllRequests=0;
		currentTime = 0.0;
		requestsQueue = new LinkedList<Request>();
		allRequests = new LinkedList<Request>();
		servers = new LinkedList<Server>();
		BrokenServers = new LinkedList<Server>();
		meanQueueLength=0.0;
		nextEventTime=0.0;
		
		
		InitiateServers();
		InitiateRequests();
	}

	public static void main(String[] args) throws IOException {
		
		LoadBalancer loadBalancer=new LoadBalancer();
		int hoursCounter=-1;
		int flag;
		int conter=0;
		while(1==1)
		{
			conter++;
			loadBalancer.currentTime=loadBalancer.currentTime+loadBalancer.nextEventTime;
			loadBalancer.calculateMeanQueueLength();
			System.out.print(loadBalancer.servers);
			
			if(loadBalancer.BrokenServers.size()>0)
			{
				loadBalancer.Repair();
			}
			loadBalancer.checkRequestArrivedToLoadBalancer();
			loadBalancer.schedule();
			loadBalancer.process();
			if(Constants.BREAKDOWN_ACTIVATED)
			{
				loadBalancer.CheckForBreakDown();
				loadBalancer.BreakDown();
			}
			loadBalancer.nextEventTime=loadBalancer.checkNextEventTime();
			if(loadBalancer.nextEventTime == -1)
			{
				break;
			}
		}
		System.out.println(loadBalancer.allRequests);
		loadBalancer.doCalculation();
	}
	
	public void InitiateServers()
	{

			int i,counter=1;
			while(counter<=Constants.SERVERS_NUMBER){
				servers.add(new Server(counter));
				counter++;
			}	
	   }


	public void InitiateRequests()
	{ 
		int i,counter=1;
		int id,status;
		double progress,timer=0.0,arrivalTime,serviceTime;

		while(counter<=Constants.REUESTS_NUMBER)
		{
			if(counter!=1)
			{
				timer+=RandomVariates.generateExponentialDistribution(Constants.ARRIVAL_RATE);
			}
			arrivalTime = timer;
			serviceTime=RandomVariates.generateExponentialDistribution(Constants.SERVICE_RATE);
			
			allRequests.add(new Request(counter,arrivalTime,serviceTime));
			
			counter++;
			
		}
			
		
		
	}
	
	public void checkRequestArrivedToLoadBalancer()
	{
		int i,j,numberOfReuestsInTheSystem;
		
		for(i=currentRequestToBeChekedInAllRequests;i<allRequests.size();i++)
		{
			Request request1=allRequests.get(i);
			if(request1.arrivalTime == currentTime)
			{
				if(Constants.Limited_SYSTEM_CAPACITY)
				{
					numberOfReuestsInTheSystem=0;
					numberOfReuestsInTheSystem=requestsQueue.size();
					for(j=0;j<servers.size();j++)
					{
						if(servers.get(j).requestBeingExecuted!=null)
							numberOfReuestsInTheSystem++;
					}
					if(numberOfReuestsInTheSystem < Constants.SYSTEM_CAPACITY)
					{
						requestsQueue.add(request1);
					} 
					else 
					{
						request1.status = Constants.STATUS_DROPPED;
					}
				}
				else
				{
					requestsQueue.add(request1);
				}
				currentRequestToBeChekedInAllRequests=i+1;
			}
			else if(request1.arrivalTime > currentTime)
			{
				currentRequestToBeChekedInAllRequests=i;
				break;
			}
			
		}
	}
	public void schedule()
	{
		int i,j;
		
		for(j=0;j<servers.size();j++)
		{
			if(requestsQueue.size() != 0)
			{
			
			Server server1=servers.get(j);
			if(server1.status == Constants.STATUS_AVAILABLE){
				Request r = requestsQueue.poll();
				r.status=Constants.STATUS_RUNNUNG;
				r.serverId=server1.id;
				r.startPrcocessingTime=currentTime;
				server1.requestBeingExecuted=r;
				server1.status=Constants.STATUS_FULL;	
				
			}
			
			}
			else
				break;
		}
	
	}
	
	public void process()
	{
		int i,j;
		for(i=0;i<servers.size();i++)
		{
			Server server=	servers.get(i);
			
			if(server.requestBeingExecuted!=null) {
					if(server.requestBeingExecuted.startPrcocessingTime != currentTime)
					{
						server.requestBeingExecuted.progress+=nextEventTime;
						server.busyTime+=nextEventTime;
					}
					if(server.requestBeingExecuted.progress == server.requestBeingExecuted.serviceTime) 
					{
						server.requestCounter++;
						server.requestBeingExecuted.finishTime=currentTime;
						server.requestBeingExecuted.status=Constants.STATUS_FINISHED;
						server.requestBeingExecuted=null;
						server.status=Constants.STATUS_AVAILABLE;
						
						if(requestsQueue.size() != 0)
						{
						
							Request r = requestsQueue.poll();
							r.status=Constants.STATUS_RUNNUNG;
							r.serverId=server.id;
							r.startPrcocessingTime=currentTime;
							server.requestBeingExecuted=r;
							server.status=Constants.STATUS_FULL;	
							
						}
						
						
					}
				}
		}
		
	}
	public void printallrequests()
	{
		int i;
		for(i=0;i<allRequests.size();i++)
		{
			System.out.println(allRequests.get(i).toString());
		}
	}
	/*
	public void dropRequests()
	{
		int i,j;
		Server server1;
		Request request1;
		for(i=0;i<requestsQueue.size();i++)
		{
			request1=requestsQueue.get(i);
			if((currentTime.val-request1.arrivalTime)==90)
			{
				request1.status=Constants.STATUS_DROPPED;
				requestsQueue.remove(i);
			}
		}
		
	}*/
	public void doCalculation()
	{
		int i,counter=0,dropCount=0,waitingRequests=0;
		Request request1;
		Server server;
		double waitingTime=0.0,avgWaiting=0.0,avgWaitingOfThoseWhoWait=0.0;
		double meanQueueLength2=0.0,throughput=0.0,utilization=0.0,delay=0.0;
		
		for(i=0;i<allRequests.size();i++)
		{
			request1=allRequests.get(i);
			
			if(request1.status == Constants.STATUS_FINISHED)
			{
				waitingTime=request1.startPrcocessingTime-request1.arrivalTime;
				if(waitingTime!=0)
				{
					avgWaiting+=waitingTime;
					waitingRequests++;
				}
				counter++;
			}
			else if(request1.status==Constants.STATUS_DROPPED)
				dropCount++;
		}
		for(i=0;i<servers.size();i++)
		{
			server=servers.get(i);
			utilization+=server.busyTime/currentTime;
			throughput+=server.requestCounter/currentTime;
		}
		meanQueueLength/=currentTime;
		utilization/=servers.size();
		throughput/=servers.size();
		delay=meanQueueLength/throughput;
		
		if(waitingRequests==0)
		{
			avgWaitingOfThoseWhoWait=0.0;
		}
		else
		{
			avgWaitingOfThoseWhoWait=(avgWaiting*(1.0))/waitingRequests;
		}
		
		avgWaiting=(avgWaiting*(1.0))/counter;
		System.out.println("Average waiting time: "+avgWaiting);
		System.out.println(" Avgerage Waiting of those who wait: "+avgWaitingOfThoseWhoWait);
		System.out.println(" P0: "+(1.0-utilization));
		System.out.println(" Utilization: "+utilization);
		System.out.println(" Mean queue length: "+meanQueueLength);
		System.out.println(" Throughput "+throughput);
		System.out.println(" Response time: "+delay);
		System.out.println(" Number of dropped Requests: "+dropCount);
		System.out.println(" dropedRequsts/AllRequests: "+((1.0*dropCount)/allRequests.size()));
		System.out.println("currentTime "+ currentTime);
	}
	
	public void calculateMeanQueueLength()
	{
		int i,numberOfReuestsInTheSystem=0;
		numberOfReuestsInTheSystem+=requestsQueue.size();
		for(i=0;i<servers.size();i++)
		{
			if(servers.get(i).requestBeingExecuted != null)
				numberOfReuestsInTheSystem++;
		}
		meanQueueLength+=numberOfReuestsInTheSystem*nextEventTime;
	}
	
	/*public void countDroppedRequests()
	{
		int i,counter=0;
		for(i=0;i<allRequests.size();i++)
		{
			if(allRequests.get(i).status==Constants.STATUS_DROPPED)
				counter++;
		}
		System.out.print(" DroppedRequests: "+counter);
		System.out.println();
	}*/
	public double checkNextEventTime()
	{
		int i,counter=1;
		double minTime=2222222.0,t,flag=0;
		Request request1;
		if(currentRequestToBeChekedInAllRequests < allRequests.size())
		{ 	
			request1=allRequests.get(currentRequestToBeChekedInAllRequests);
			minTime=request1.arrivalTime-currentTime;
			flag=1;
			System.out.println("arrival "+minTime);
		}
		for(i=0;i<servers.size();i++)
		{
			if(servers.get(i).status == Constants.STATUS_FULL)
			{
				t= servers.get(i).requestBeingExecuted.serviceTime-servers.get(i).requestBeingExecuted.progress;
				System.out.println("t "+t);
				if(t < minTime)
				{
					flag=1;
					minTime=t;
				}
			}
		}
		if((Constants.BREAKDOWN_ACTIVATED)&&(flag==1))
		{
			for(i=0;i<servers.size();i++)
			{	if(Constants.BREAKDOWN_ACTIVATED)
				{
					if((servers.get(i).breakDownStatus)&&(servers.get(i).status!=Constants.STATUS_BREAKDOWN))
					{
						t=servers.get(i).breakDownTime-currentTime;
						if(t < minTime)
						{
							minTime=t;
						}
					}
				}
			}
			
			
			if(BrokenServers.size()!=0)
			{
				t=BrokenServers.get(0).finishRepairingTime-currentTime;
				if(t < minTime)
				{
					minTime=t;
				}
			}
		}
		if(minTime == 2222222.0)
			minTime=-1;
	//	System.out.println("minTime "+minTime);
		return minTime;
	}
	public void CheckForBreakDown()
	{
		int counter=0,i,min = 1,max,numberOfReuestsInTheSystem,value;
		Random num = new Random();
		
		for(i=0;i<servers.size();i++)
		{
			if(servers.get(i).breakDownStatus==false)
			{
				counter++;
			}
		}
		if(counter==1)
		{
			for(i=0;i<servers.size();i++)
			{
				if(servers.get(i).breakDownStatus==false)
				{
					servers.get(i).breakDownStatus=true;
					servers.get(i).breakDownTime=currentTime+RandomVariates.generateWeibullDistribution(Constants.MEAN_OF_FAILURE, Constants.BETA, Constants.V);
				}
			}
		}
		else if(counter>1)
		{
			max=counter;
			value=num.nextInt(max - min + 1) + min;
			counter=0;
			for(i=0;i<servers.size();i++)
			{
				if(servers.get(i).breakDownStatus==false)
				{
					counter++;
					if(counter==value)
					{
						servers.get(i).breakDownStatus=true;
						servers.get(i).breakDownTime=currentTime+RandomVariates.generateWeibullDistribution(Constants.MEAN_OF_FAILURE, Constants.BETA, Constants.V);
					}
				}
			}
		}
	}
	public void BreakDown()
	{
		int i,min = 0,max = 100,numberOfReuestsInTheSystem,j;
		Random num = new Random();
		Server brokenServer;
		Request request1;
		
		for(i=0;i<servers.size();i++)
		{
			if((servers.get(i).breakDownStatus==true)&&(servers.get(i).breakDownTime==currentTime)&&(servers.get(i).status!=Constants.STATUS_BREAKDOWN))
			{
				
				brokenServer=servers.get(i);
				brokenServer.status=Constants.STATUS_BREAKDOWN;
				
				brokenServer.breakDownTime=-1.0;
				
				BrokenServers.add(brokenServer);
				if(brokenServer.requestBeingExecuted!=null)
				{
					request1=brokenServer.requestBeingExecuted;
					brokenServer.requestBeingExecuted=null;
					brokenServer.repairingTime=RandomVariates.generateLogNormalDistribution(Constants.LOGNORMAL_MEAN, Constants.LOGNORMAL_VARIANCE);
					if(BrokenServers.size()==0)
					{
						brokenServer.finishRepairingTime=currentTime+brokenServer.repairingTime;
					}
					BrokenServers.add(brokenServer);
					request1.progress=0.0;
					request1.serverId=-1;
					request1.startPrcocessingTime=-1.0;
					if(Constants.Limited_SYSTEM_CAPACITY)
					{
						numberOfReuestsInTheSystem=requestsQueue.size();
						for(j=0;j<servers.size();j++)
						{
							if(servers.get(j).requestBeingExecuted!=null)
								numberOfReuestsInTheSystem++;
						}
						if(numberOfReuestsInTheSystem < (Constants.SYSTEM_CAPACITY-BrokenServers.size()))
						{
							requestsQueue.add(request1);
							request1.status= Constants.STATUS_WAITING_IN_LOADBALANCER;
						} 
						else {
							request1.status = Constants.STATUS_DROPPED;
						}
					}
					else
					{
						requestsQueue.add(request1);
						request1.status= Constants.STATUS_WAITING_IN_LOADBALANCER;
					}
				}		
			}
			
		}
	}
	public void Repair()
	{
		if(BrokenServers.get(0).finishRepairingTime==currentTime)
		{
			BrokenServers.get(0).status=Constants.STATUS_AVAILABLE;
			BrokenServers.get(0).breakDownStatus=false;
			BrokenServers.get(0).finishRepairingTime=-1.0;
			BrokenServers.poll();
			if(BrokenServers.size()!=0)
			{
				BrokenServers.get(0).finishRepairingTime=currentTime+BrokenServers.get(0).repairingTime;
			}
		}
	}
	
}
