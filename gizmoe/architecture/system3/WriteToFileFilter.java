package gizmo.architecture.system3;
/******************************************************************************************************************
* File:WriteToFileFilter.java
* Course: 17655
* Project: Assignment 1
* Copyright: Copyright (c) 2003 Carnegie Mellon University
* Versions:
*	1.0 8th February 2013 - Initial code (ukd)
*
* Description:
*
* This class will create a filter to write to predefined file. It print a header, and then will print the 
* formatted values from the datastream. Remember to change the header and data to write to suit your needs. 
* Currently prints Time, Temperature and Altitude.
*
* Parameters: 	None
*
* Internal Methods: 
* 	run()
* 
* Main author/Owner: 
* 	Upsham K Dawra
*
******************************************************************************************************************/
import java.util.*;						// This class is used to interpret time words
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;		// This class is used to format and write time in a string format.

public class WriteToFileFilter extends SingleOutputFilterFramework
{
	public void run()
    {
		/************************************************************************************
		*	TimeStamp is used to compute time using java.util's Calendar class.
		* 	TimeStampFormat is used to format the time value so that it can be easily printed
		*	to the terminal.
		*************************************************************************************/

		Calendar TimeStamp = Calendar.getInstance();
		SimpleDateFormat TimeStampFormat = new SimpleDateFormat("yyyy MM dd::hh:mm:ss:SSS");

		int bytesread = 0;				// This is the number of bytes read from the stream
		int byteswritten = 0;
		long measurement;				// This is the word used to store all measurements - conversions are illustrated.
		int id;							// This is the measurement id
		String altitudeFrameBuffer = "";
		String pressureFrameBuffer = "";
		/*************************************************************
		*	First we announce to the world that we are alive...
		**************************************************************/

		System.out.print( "\n" + this.getName() + "::WriteToFileFilter Reading ");
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter("Output.dat"));
			out.write("Time:\t\t\t\t\t\t    Temperature(C):     Altitude(m):        Pressure(psi)        Altitude(degrees)::");
			out.newLine();
			out.write("---------------------------------------------------------"
					+"----------------------------------------------------------");
			while (true)
			{
				try
				{
					id = ReadIDFromFilterInputPort();

					measurement = ReadMeasurementFromFilterInputPort();
					bytesread+=12;
					if(id == VELOCITY){
						byteswritten+=12;
					}
					if ( id == TIME )
					{
						out.newLine();
						TimeStamp.setTimeInMillis(measurement);
						out.write(TimeStampFormat.format(TimeStamp.getTime())+"\t\t");
						byteswritten+=12;
						
					} // if
					else if(id == ALTITUDE){
						
						altitudeFrameBuffer = String.format("%11s", String.format("%5.5f",Double.longBitsToDouble(measurement))).replace(' ', '0');
						out.write(altitudeFrameBuffer+"\t\t\t");
						byteswritten+=12;
					}
					else if(id == TEMPERATURE){
						out.write(String.format("%9s", String.format("%.5f",Double.longBitsToDouble(measurement)))+"\t\t\t");
						//out.write(altitudeFrameBuffer+"\t\t\t");
						//out.write(pressureFrameBuffer+"\t\t\t");
						byteswritten+=12;
					}
					else if(id == PRESSURE){
						pressureFrameBuffer = String.format("%8s", String.format("%2.5f",Double.longBitsToDouble(measurement))).replace(' ', '0').replace('.',':');
						out.write(pressureFrameBuffer+"\t\t\t");
						byteswritten+=12;
					}
					else if(id == PRESSURE+CORRECTION_OFFSET){
						pressureFrameBuffer = String.format("%8s", String.format("%2.5f",Double.longBitsToDouble(measurement))).replace(' ', '0').replace('.',':')+"*";
						out.write(pressureFrameBuffer+"\t\t\t");
						byteswritten+=12;
					}
					else if(id == ATTITUDE){
						out.write(String.format("%8s", String.format("%2.5f",Double.longBitsToDouble(measurement))).replace(' ', '0').replace('.',':'));
						byteswritten+=12;
					}
					else if(id == ATTITUDE+CORRECTION_OFFSET){
						out.write(String.format("%8s", String.format("%2.5f",Double.longBitsToDouble(measurement))).replace(' ', '0').replace('.',':')+"*");
						byteswritten+=12;
					}

				} // try
				catch (EndOfStreamException e)
				{
					ClosePorts();
					out.close();
					System.out.print( "\n" + this.getName() + "::WriteToFileFilter Exiting; bytes read: " + bytesread +" bytes written: "+byteswritten);
					break;

				} // catch

			} // while
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

   } // run

} // SingFilter