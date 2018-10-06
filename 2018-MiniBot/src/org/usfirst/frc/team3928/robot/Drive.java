package org.usfirst.frc.team3928.robot;

import edu.wpi.first.wpilibj.Talon;

public class Drive 
{
	private Talon left1;
	private Talon left2;
	private Talon right1;
	private Talon right2;
	
	public Drive()
	{
		
		right1 = new Talon(2);
		right2 = new Talon(3);
		left1 = new Talon(1);
		left2 = new Talon(4);
		
	}
	
	public void setLeft(double motorPower)
	{
		left1.set(motorPower);
		left2.set(motorPower);
	}
	
	public void setRight(double motorPower)
	{
		right1.set(motorPower);
		right2.set(motorPower);
	}
	
	
}
