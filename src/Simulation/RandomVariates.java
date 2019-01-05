package Simulation;

import java.util.Random;

public class RandomVariates {
	public static double generateExponentialDistribution(double Rate){
		double number;
		double random;
		random=Math.random();
		while((random==0.0)||(random==1.0))
		{
			random=Math.random();
		}
		number=-(1/Rate)*(Math.log(random));
		return number;
	}
	
	
	public static double generateWeibullDistribution(double alpha,double beta,double v){
		double random;
		random=Math.random();
		while((random==0.0)||(random==1.0))
		{
			random=Math.random();
		}
		return (60.0*(v-alpha*Math.pow(Math.log(random),(1/beta))));
		
	}
	
	public static double generateLogNormalDistribution(double mean,double variance) {
		double mu,sigma,random,random2,z1,z2;
		random=Math.random();
		while((random==0.0)||(random==1.0))
		{
			random=Math.random();
		}
		random2=Math.random();
		while((random2==0.0)||(random2==1.0))
		{
			random2=Math.random();
		}
	
		mu=Math.log((mean*mean)/Math.sqrt(variance+(mean*mean)));
		sigma=Math.sqrt(Math.log(1+(variance/(mean*mean))));
		
		z1=(Math.pow(-2*Math.log(random),0.5)*Math.cos(2*Math.PI*random2));
		z2=(Math.pow(-2*Math.log(random),0.5)*Math.sin(2*Math.PI*random2));
		z1=mu+sigma*z1;
		z2=mu+sigma*z2;
		z1=Math.exp(z1);
		z2=Math.exp(z2);
		return (z1*60.0);
		
}
		
}
