package assignment1;

import lejos.robotics.RegulatedMotor;
import lejos.hardware.motor.Motor;
import lejos.hardware.BrickFinder;
import lejos.hardware.Keys;
import lejos.hardware.ev3.EV3;
import lejos.robotics.SampleProvider;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.port.SensorPort;
import lejos.robotics.Color;
import lejos.hardware.sensor.EV3IRSensor;
import lejos.utility.Delay;
import java.lang.String;

public class grid_explorer {
	private static EV3ColorSensor color_left;
	private static EV3ColorSensor color_right;
	private static EV3IRSensor ir_sensor;
	
	public static void main(String[] args){
		
		//TODO - check for the block in front
		//TODO - keep red floor coordinates
		//TODO - implement the path finding algorithm
		
		RegulatedMotor leftMotor = Motor.B;
		RegulatedMotor rightMotor = Motor.D;
		EV3 ev3 = (EV3) BrickFinder.getLocal();
		TextLCD lcd = ev3.getTextLCD();
		Keys keys = ev3.getKeys();
		color_left = new EV3ColorSensor(SensorPort.S3);
		color_right = new EV3ColorSensor(SensorPort.S4);
		ir_sensor = new EV3IRSensor(SensorPort.S1);

		int[][] map = new int[6][4];
		int red_count = 0;
		int block_count = 0;

		SampleProvider distanceMode = ir_sensor.getDistanceMode();
		float value[] = new float[distanceMode.sampleSize()];
		

//		for(int i = 0; i<6; i++){
//			for (int j = 0; j<4; j++){
//				map[i][j] = 0;
//			}
//		}
		//if 1 -> visited, if 2-> red & visited, if 3->block
		
		
		//hard coding for going through the map spirally
		for(int i = 0; i < 5;i++){
			one_forward(leftMotor,rightMotor,color_left, color_right,ir_sensor);
		}
		rotate_left(leftMotor,rightMotor);	
		one_forward(leftMotor,rightMotor,color_left, color_right,ir_sensor);
		one_forward(leftMotor,rightMotor,color_left, color_right,ir_sensor);
		one_forward(leftMotor,rightMotor,color_left, color_right,ir_sensor);
		rotate_left(leftMotor,rightMotor);
		for(int i = 0; i < 5;i++){
			one_forward(leftMotor,rightMotor,color_left, color_right,ir_sensor);
		}
		rotate_left(leftMotor,rightMotor);	
		one_forward(leftMotor,rightMotor,color_left, color_right,ir_sensor);
		one_forward(leftMotor,rightMotor,color_left, color_right,ir_sensor);
		rotate_left(leftMotor,rightMotor);	
		for(int i = 0; i < 4;i++){
			one_forward(leftMotor,rightMotor,color_left, color_right,ir_sensor);
		}
		rotate_left(leftMotor,rightMotor);	
		one_forward(leftMotor,rightMotor,color_left, color_right,ir_sensor);
		rotate_left(leftMotor,rightMotor);	
		for(int i = 0; i < 3;i++){
			one_forward(leftMotor,rightMotor,color_left, color_right,ir_sensor);
		}
		
		leftMotor.stop(true);
		rightMotor.stop(true);
		leftMotor.close();
		rightMotor.close();
		
		return;
	}

	//moves forward 23cms(rough estimate) - commented the whole function in case we need it for later.
	
//	public static void one_forward(RegulatedMotor leftMotor, RegulatedMotor rightMotor){
//		leftMotor.setSpeed(300);
//		rightMotor.setSpeed(300);
//		leftMotor.forward();
//		rightMotor.forward();
//		try{
//			Thread.sleep(2250);
//		}catch(InterruptedException e){}
//	}
	
	public static void one_forward(RegulatedMotor leftMotor, RegulatedMotor rightMotor, EV3ColorSensor leftColor, EV3ColorSensor rightColor, EV3IRSensor IRsensor){

		EV3 ev3 = (EV3) BrickFinder.getLocal();
		TextLCD lcd = ev3.getTextLCD();
		Keys keys = ev3.getKeys();
		
		//constants for checking if the sensor has reached the black line
		int right_met = 0;
		int left_met = 0;
	
		//first, it just moves until the do/while condition is met
		rightMotor.setSpeed(400);
		leftMotor.setSpeed(400);
		rightMotor.forward();
		leftMotor.forward();
		
		//do while loop checks if black line is detected, every 10 ms.
		do{
			
			//result string containing the current color
			String str = "";
			
			//left color id and right color id
			int left_cid = leftColor.getColorID();
			int right_cid = rightColor.getColorID();
			
			//if left wheel has not run into black
			if(left_met == 0){
				switch(left_cid){
				
				//if black, then stop, give 30ms delay - this was implemented from trial and error.
				//from my thought, I expected the wheel to stop after the leftmotor.stop(true) is called
				//well it does stop; however, it did not wait for the right wheel to stop. 
				//so we gave 30ms delay, which was enough for the amount of angular error we had for the track. 
				//It'd be great if it can be improved to where it waits for another motor to stop
				//instead of waiting for an arbitrary number of 30ms.
				case Color.BLACK:
					leftMotor.stop(true);
					Delay.msDelay(30);
					left_met = 1;
					str+= "black";
					break;
				//if white, just save white to the result string
				case Color.WHITE:
					str += "white";
					break;
				//TODO case for red will have to be improvised
				default:
					break;
				}
			}
			//same algorithm from the left motor, but for the right motor.
			if(right_met == 0){
				switch(right_cid){
				case Color.BLACK:
					rightMotor.stop(true);
					Delay.msDelay(30);
					right_met = 1;
					str+= "black";
					break;
				case Color.WHITE:
					str += "white";
					break;
				default:
					break;
				}
			}
			
			//draws the result string, but it did not work when I tried last time. will have to try again - KL
			lcd.drawString(str,1,5);
			
			Delay.msDelay(10);
			
		}while(left_met == 0 && right_met == 0);
		
		
		//Once it stops at the black line, it moves forward a little bit to center itself in the grid cell
		leftMotor.setSpeed(400);
		rightMotor.setSpeed(400);
		leftMotor.forward();
		rightMotor.forward();
		try{
			Thread.sleep(1125);
		}catch(InterruptedException e){}
	}
	
	
	//function to rotate to the right side, 90 degrees
	public static void rotate_right(RegulatedMotor leftMotor, RegulatedMotor rightMotor){
		leftMotor.setSpeed(200);
		rightMotor.setSpeed(200);
		leftMotor.forward();
		rightMotor.backward();
		
		try{
			Thread.sleep(1155);
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
			Thread.sleep(1155);
		}catch(InterruptedException e){}
	}
	
	//function to brake
	public static void flt(RegulatedMotor leftMotor, RegulatedMotor rightMotor){
		leftMotor.flt();
		rightMotor.flt();
	}
	
	//function to stop
	public static void stop(RegulatedMotor leftMotor, RegulatedMotor rightMotor){
		leftMotor.stop(true);
		rightMotor.stop(true);
		try{
			Thread.sleep(1050);
		}catch(InterruptedException e){}
	}
	
	public static int search_map(int[][] map){
		for(int i = 0; i < map.length; i++){
			for(int j = 0; j < map[i].length; j++){
				return map[i][j];
			}
		}
		return 1;
	}
}
