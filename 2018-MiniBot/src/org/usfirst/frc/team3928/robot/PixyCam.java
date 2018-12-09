package org.usfirst.frc.team3928.robot;

import edu.wpi.first.wpilibj.SerialPort;

/**
 * Class for the PixyCam that reads data form the serial port and parses data of the 
 * object being tracked.
 * 
 * @author FRC Neutrino
 *
 */
public class PixyCam implements Runnable
{
	/**
	 * The pixyCam connection
	 */
	private SerialPort pixyConnection;
	
	/**
	 * An array of the data for the object that is being tracked
	 */
	private int[] data;
	
	/**
	 * Constructs a pixyCam connected to the serial port with a buffer size of 100.
	 */
	public PixyCam()
	{
		pixyConnection = new SerialPort(115200, SerialPort.Port.kMXP);
		pixyConnection.setReadBufferSize(100);
		
		data = getNextDataBlock();
		
		new Thread(this).start();
	}
	
	/**
	 * Returns the center X coordinate of the object being tracked.
	 * @return
	 * 	The center X value
	 */
	public int getX()
	{
		return data[2];
	}
	
	/**
	 * Returns the center Y coordinate of the object being tracked.
	 * @return
	 * 	The center Y value
	 */
	public int getY()
	{
		return data[3];
	}
	
	/**
	 * Returns the width of the object in pixels.
	 * @return
	 * 	The width of the object
	 */
	public int getWidth()
	{
		return data[4];
	}
	
	/**
	 * Returns the height of the object in pixels.
	 * @return
	 * 	The height of the object
	 */
	public int getHeight()
	{
		return data[5];
	}
	
	/**
	 * Parses the bytes from the pixy connection to create an int array of the data.
	 * @return
	 * 	An int array with the object's position and size data
	 */
	private int[] getNextDataBlock()
	{
		byte[] receivedData = pixyConnection.read(28);
		
		boolean startFound = false;
		int arrPos = 0;
		
		while(!startFound && arrPos < receivedData.length)
		{
			if(receivedData[arrPos] == 0xaa55)
			{
				startFound = true;
			}
			else
			{
				arrPos++;
			}
		}
		
		int[] sendData = new int[6];
		int current = arrPos + 2;
		
		for(int dataPos = 0; dataPos < sendData.length; dataPos++)
		{
			sendData[dataPos] = receivedData[current] << 8 | receivedData[current + 1];
			current += 2;
		}
		
		return sendData;
	}

	/**
	 * Checks the values using the checksum in index 0 to verify the data is correct.
	 * @param values
	 * 	The values that are being checked wit the checksum in index 0
	 * @return
	 * 	True if the data is correct, false of the data contains an error
	 */
	private boolean checkData(int[] values)
	{
		int sum = 0;
		
		for(int arrPos = 1; arrPos < values.length; arrPos++)
		{
			sum += values[arrPos];
		}
			
		return sum == values[0];
	}
	
	@Override
	public void run() 
	{
		while(true)
		{
			if(pixyConnection.getBytesReceived() > 28)//Checks if there is enough data to successfully parse
			{
				boolean gotData = false;
				int[] nextData = getNextDataBlock();
				
				while(!gotData)
				{
					nextData = getNextDataBlock();
					
					if(!checkData(nextData))
					{
						gotData = true;
					}
				}
				
				data = nextData;
			}
			else //Waits for additional data to be received
			{
				try
				{
					Thread.sleep(1);
				}
				catch (InterruptedException e) 
				{
					e.printStackTrace();
				}
			}
		}
	}
}
