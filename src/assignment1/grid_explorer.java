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
		RegulatedMotor leftMotor = Motor.B;
		RegulatedMotor rightMotor = Motor.D;
//		int[][] map = new int[6][4];
//		int red_count = 0;
//		int block_count = 0;
		EV3 ev3 = (EV3) BrickFinder.getLocal();
		TextLCD lcd = ev3.getTextLCD();
		Keys keys = ev3.getKeys();
		
		color_left = new EV3ColorSensor(SensorPort.S3);
		color_right = new EV3ColorSensor(SensorPort.S4);
		ir_sensor = new EV3IRSensor(SensorPort.S1);

		
//		SampleProvider distanceMode = ir_sensor.getDistanceMode();
//		float value[] = new float[distanceMode.sampleSize()];
		
//		do {
//			String str = "";
//			
//			int left_cid = color_left.getColorID();
//			int right_cid = color_right.getColorID();
//	
//			switch(left_cid)
//				{
//				case Color.BLACK:
//					str = "Color.BLACK = "+String.valueOf(left_cid);
//					break;
//				case Color.RED:
//					str = "Color.RED = "+String.valueOf(left_cid);
//					break;
//				case Color.WHITE:
//					str = "Color.WHITE = "+String.valueOf(left_cid);
//					break;
//				default:
//					str = "???";
//					break;
//				}
//			lcd.drawString(str, 1, 4);
//			keys.waitForAnyPress();
//			lcd.clear();
//		} while(keys.getButtons() != Keys.ID_ESCAPE);
		
//		for(int i = 0; i<6; i++){
//			for (int j = 0; j<4; j++){
//				map[i][j] = 0;
//			}
//		}
		//if 1 -> visited, if 2-> red & visited, if 3->block
		for(int i = 0; i < 5;i++){
			test_forward(leftMotor,rightMotor,color_left, color_right,ir_sensor);
		}
		rotate_left(leftMotor,rightMotor);	
		test_forward(leftMotor,rightMotor,color_left, color_right,ir_sensor);
		test_forward(leftMotor,rightMotor,color_left, color_right,ir_sensor);
		test_forward(leftMotor,rightMotor,color_left, color_right,ir_sensor);
		rotate_left(leftMotor,rightMotor);
		for(int i = 0; i < 5;i++){
			test_forward(leftMotor,rightMotor,color_left, color_right,ir_sensor);
		}
		rotate_left(leftMotor,rightMotor);	
		test_forward(leftMotor,rightMotor,color_left, color_right,ir_sensor);
		test_forward(leftMotor,rightMotor,color_left, color_right,ir_sensor);
		rotate_left(leftMotor,rightMotor);	
		for(int i = 0; i < 4;i++){
			test_forward(leftMotor,rightMotor,color_left, color_right,ir_sensor);
		}
		rotate_left(leftMotor,rightMotor);	
		test_forward(leftMotor,rightMotor,color_left, color_right,ir_sensor);
		rotate_left(leftMotor,rightMotor);	
		for(int i = 0; i < 3;i++){
			test_forward(leftMotor,rightMotor,color_left, color_right,ir_sensor);
		}
		
		leftMotor.stop(true);
		rightMotor.stop(true);
		leftMotor.close();
		rightMotor.close();
	}

	//moves forward 23cms(rough estimate)
	public static void one_forward(RegulatedMotor leftMotor, RegulatedMotor rightMotor){
		leftMotor.setSpeed(300);
		rightMotor.setSpeed(300);
		leftMotor.forward();
		rightMotor.forward();
		try{
			Thread.sleep(2250);
		}catch(InterruptedException e){}
	}
	
	public static void test_forward(RegulatedMotor leftMotor, RegulatedMotor rightMotor, EV3ColorSensor leftColor, EV3ColorSensor rightColor, EV3IRSensor IRsensor){
		int left_met = 0;
		EV3 ev3 = (EV3) BrickFinder.getLocal();
		TextLCD lcd = ev3.getTextLCD();
		Keys keys = ev3.getKeys();
		int right_met = 0;
		rightMotor.setSpeed(400);
		leftMotor.setSpeed(400);
		rightMotor.forward();
		leftMotor.forward();
		
		do{
			String str = "";
			int left_cid = leftColor.getColorID();
			int right_cid = rightColor.getColorID();
			if(left_met == 0){
				switch(left_cid){
				case Color.BLACK:
					leftMotor.stop(true);
					Delay.msDelay(30);
					left_met = 1;
					str+= "black";
					break;
				default:
					break;
				}
			}
			if(right_met == 0){
				switch(right_cid){
				case Color.BLACK:
					rightMotor.stop(true);
					Delay.msDelay(30);
					right_met = 1;
					str+= "black";
					break;
				default:
					break;
				}
			}

			lcd.drawString(str,1,10);
			
			Delay.msDelay(10);
			
		}while(left_met == 0 && right_met == 0);
		
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
				if(map[i][j] != 0){
					return 0;
				}
			}
		}
		return 1;
	}
}
