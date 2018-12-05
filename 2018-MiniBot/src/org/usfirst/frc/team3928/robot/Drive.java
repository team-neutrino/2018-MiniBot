package org.usfirst.frc.team3928.robot;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Ultrasonic;

public class Drive implements PIDOutput, Runnable
{
	private Talon left1;
	private Talon left2;
	private Talon right1;
	private Talon right2;
	private Ultrasonic sanic;
	private PIDController pid;
	
	public Drive()
	{
		
		right1 = new Talon(2);
		right2 = new Talon(3);
		left1 = new Talon(4);
		left2 = new Talon(1);
		sanic = new Ultrasonic(5, 6);
		pid = new PIDController(0.035, 0.00035, 0.01, sanic, this);
		pid.setAbsoluteTolerance(2);
		pid.setOutputRange(-0.5, 0.5);
		pid.setInputRange(15, 100);
		
		new Thread(this).start();
	}
	
	public void setLeft(double motorPower)
	{
		left1.set(motorPower);
		left2.set(motorPower);
	}
	
	public void setRight(double motorPower)
	{
		right1.set(-motorPower);
		right2.set(-motorPower);
	}

	@Override
	public void pidWrite(double output) {
		// TODO Auto-generated method stub
		setLeft(-output);
		setRight(-output);
		System.out.println("here " + output);
	}
	
	public void driveToDistance(double distance) {
		
		pid.setSetpoint(distance);
		pid.enable();
		while(!pid.onTarget()) {
			System.out.println("threr " + pid.getError());
			System.out.println("sanic " + sanic.getRangeInches());
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		pid.disable();
		
	}
	
	public double getUltrasonicValue()
	{
		return sanic.getRangeInches();
	}
	

	@Override
	public void run() 
	{
		long b = 0;
		long a = System.currentTimeMillis();
		while(true)
		{
			if (a - b > 5000)
			{
				System.out.println(getUltrasonicValue());
				b = a;
			}
			a = System.currentTimeMillis();
		}
	}
	
}
