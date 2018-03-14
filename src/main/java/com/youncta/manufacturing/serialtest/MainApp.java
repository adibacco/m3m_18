package com.youncta.manufacturing.serialtest;


import com.fazecast.jSerialComm.SerialPort;

import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.util.Arrays;

import javax.swing.JTextArea;
public class MainApp  {



	static byte[] RESET = new byte[] { (byte) 0xff, (byte) 0xff, (byte) 0xff };
	static byte[] FREQUENCY_SETUP_8G = new byte[] { (byte) 'F', (byte) 'R', (byte) 0x10 };
	static byte[] LIGHT_ON = new byte[] { (byte) 'L', (byte) 'I', (byte) 0x01 };
	static byte[] LIGHT_OFF = new byte[] { (byte) 'L', (byte) 'I', (byte) 0x00 };
	static byte[] POLL = new byte[] { (byte) 0x00 };
	  

	 
	public static void sleep(int milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
	    byte[] readBuffer = new byte[1024];

		SerialPort comPort = SerialPort.getCommPorts()[0];
		System.out.println("comPort " + comPort.getSystemPortName());
		comPort.setParity(SerialPort.NO_PARITY);
		comPort.setFlowControl(SerialPort.FLOW_CONTROL_DISABLED);
		comPort.setNumStopBits(1);
		comPort.setBaudRate(19200);
		comPort.openPort();


		comPort.setComPortTimeouts(SerialPort.TIMEOUT_NONBLOCKING, 1000, 100);
		comPort.readBytes(readBuffer, readBuffer.length);
		
		comPort.writeBytes(RESET, RESET.length);
		sleep(5000);
		comPort.writeBytes(LIGHT_ON, LIGHT_ON.length);
		sleep(3000);
		comPort.writeBytes(LIGHT_OFF, LIGHT_OFF.length);
		sleep(100);
		comPort.writeBytes(FREQUENCY_SETUP_8G, FREQUENCY_SETUP_8G.length);
		sleep(100);
	
		try {

		      while (true) {
				  comPort.writeBytes(POLL, POLL.length);
				  sleep(3000);

			      int numRead = comPort.readBytes(readBuffer, readBuffer.length);
			      
			      if (numRead > 8) {
				      byte[] str = new byte[16];
				      System.arraycopy(readBuffer, 0, str, 0, 9);
				      System.out.println("Read " + new String(str, 0));

			      }
		      }

		} catch (Exception e) { 
			e.printStackTrace(); 
		}
		
		comPort.closePort();
	}



}
