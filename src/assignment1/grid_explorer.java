package assignment1;

import lejos.robotics.Color;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3IRSensor;

public class grid_explorer {
	static final int OBJ_DIST = 10;
	static final char OBJECT = 'O';
	static final char RED_TILE = 'R';
	static final int NORTH = 0, EAST = 90, SOUTH = 180, WEST = 270;
	static final int TURN_DEG = 90;
	
	private static EV3ColorSensor color_sensor;
	private static EV3IRSensor ir_sensor;
	static char grid[][] = new char[6][4]; 
	static int dir = NORTH;
	static int x, y;
	
	public static void main(String[] args){
		RegulatedMotor leftMotor = Motor.A;
		RegulatedMotor rightMotor = Motor.D;
		color_sensor = new EV3ColorSensor(SensorPort.S2);
		ir_sensor = new EV3IRSensor(SensorPort.S1);

		
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
		
		// Update the robots location
		if (dir == NORTH)
			y += 1;
		else if (dir == SOUTH)
			y -= 1;
		else if (dir == EAST)
			x += 1;
		else
			x -= 1;
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
		
		dir += TURN_DEG;
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
		
		dir -= TURN_DEG;
	}
	
	//function to stop
	public static void stop(RegulatedMotor leftMotor, RegulatedMotor rightMotor){
		leftMotor.stop();
		rightMotor.stop();
	}
	
	// NOTE: color_sensor is a field. It can be passed as a parameter to be consistent with the movement methods
	// Updates grid if it detects a red tile
	public static void survey_color(){
		if (color_sensor.getColorID() == Color.RED)
			grid[x][y] = RED_TILE;
	}
	
	// NOTE: ir_sensor is a field. It can be passed as a parameter to be consistent with the movement methods
	// Updates grid if it detects an object
	public static void survey_obj(){
		SampleProvider distanceMode = ir_sensor.getDistanceMode();
		float value[] = new float[distanceMode.sampleSize()];
		distanceMode.fetchSample(value,  0);
		
		// Evaluate true only when the object is in an adjacent tile
		if (value[0] <= OBJ_DIST)
		{
			// Update the correct adjacent tile
			if (dir == NORTH)
				grid[x][y + 1] = OBJECT;
			else if (dir == SOUTH)
				grid[x][y - 1] = OBJECT;
			else if (dir == EAST)
				grid[x + 1][y] = OBJECT;
			else
				grid[x - 1][y] = OBJECT;
		}
	}
}
