/******************************************************************************************************************
* File:
* Course: 17655
* Project: Assignment 1
* Versions:
*  1.0 9 February 2013 - Writing time and converted temperature, altitude Filter code (ss).
*
* Description:
*
* This class will create a filter to write to predefined file. It print a header, and then will print the 
* formatted values from the datastream. Remember to change the header and data to write to suit your needs. 
* Currently prints Time, Temperature and Altitude.
*
* Parameters: 		 
* 	double temperatureC - stores the converted measurement (temperature in celsius)
*
* Internal Methods: 
* 	public void run()
* 
* Main author/Owner: 
* 	Sindhu Satish
*
******************************************************************************************************************/
package gizmoe.architecture.system1;
import java.util.*;						// This class is used to interpret time words
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;		// This class is used to format and write time in a string format.

public class WriteTempAltFilter extends SingleOutputFilterFramework
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

		long measurement;				// This is the word used to store all measurements - conversions are illustrated.
		int id;							// This is the measurement id
		String altitudeFrameBuffer = "";
		/*************************************************************
		*	First we announce to the world that we are alive...
		**************************************************************/

		System.out.print( "\n" + this.getName() + "::Sink Reading ");
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter("OutputA.dat"));
			out.write("Time:\t\t\t\t\t\t    Temperature(C):     Altitude(m):");
			out.newLine();
			out.write("---------------------------------------------------------"
					+"--------------------------------");
			while (true)
			{
				try
				{
					id = ReadIDFromFilterInputPort();

					measurement = ReadMeasurementFromFilterInputPort();
					bytesread+=12;

					if ( id == 0 )
					{
						out.newLine();
						TimeStamp.setTimeInMillis(measurement);
						out.write(TimeStampFormat.format(TimeStamp.getTime())+"\t\t");
					} // if
					else if(id == 2){

						altitudeFrameBuffer = String.format("%11s", String.format("%5.5f",Double.longBitsToDouble(measurement))).replace(' ', '0');
					}
					else if(id == 4){
						out.write(String.format("%9s", String.format("%.5f",Double.longBitsToDouble(measurement)))+"\t\t\t");
						out.write(altitudeFrameBuffer);
					}

				} // try
				catch (EndOfStreamException e)
				{
					ClosePorts();
					out.close();
					System.out.print( "\n" + this.getName() + "::Sink Exiting; bytes read: " + bytesread );
					break;

				} // catch

			} // while
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


   } // run

} // WriteTempAltFilter