package assignment1;

import lejos.robotics.RegulatedMotor;
import lejos.hardware.motor.Motor;

public class grid_explorer {
	public static void main(String[] args){
		RegulatedMotor leftMotor = Motor.A;
		RegulatedMotor rightMotor = Motor.D;
		
		one_forward(leftMotor, rightMotor);
		stop(leftMotor, rightMotor);
		rotate_right(leftMotor, rightMotor);
		stop(leftMotor, rightMotor);
		one_forward(leftMotor, rightMotor);
		
	}

	//moves forward 23cms(rough estimate)
	public static void one_forward(RegulatedMotor leftMotor, RegulatedMotor rightMotor){
		leftMotor.setSpeed(300);
		rightMotor.setSpeed(300);
		leftMotor.forward();
		rightMotor.forward();
		
		try{
			Thread.sleep(1900);
		}catch(InterruptedException e){}
	}
	
	
	//function to rotate to the right side, 90 degrees
	public static void rotate_right(RegulatedMotor leftMotor, RegulatedMotor rightMotor){
		leftMotor.setSpeed(200);
		rightMotor.setSpeed(200);
		leftMotor.forward();
		rightMotor.backward();
		
		try{
			Thread.sleep(1050);
		}catch(InterruptedException e){}
	}
	
	//function to rotate to the right side, 90 degrees
	//TODO : have not been tested. need to test
	public static void rotate_left(RegulatedMotor leftMotor, RegulatedMotor rightMotor){
		leftMotor.setSpeed(200);
		rightMotor.setSpeed(200);
		leftMotor.backward();
		rightMotor.forward();
		
		try{
			Thread.sleep(1050);
		}catch(InterruptedException e){}
	}
	
	//function to stop
	public static void stop(RegulatedMotor leftMotor, RegulatedMotor rightMotor){
		leftMotor.stop();
		rightMotor.stop();
	}
}
