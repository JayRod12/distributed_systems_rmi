/*
 * Created on 01-Mar-2016
 */
package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Arrays;

import common.MessageInfo;

public class UDPServer {

	private DatagramSocket recvSoc;
	private int totalMessages = -1;
	private int messageCount = -1;
	private boolean[] receivedMessages;
	private boolean close;

	private void run() {
		int				pacSize;
		byte[]			pacData;
		DatagramPacket 	pac;
    int timeoutMs;

		// TO-DO: Receive the messages and process them by calling processMessage(...).
		//        Use a timeout (e.g. 30 secs) to ensure the program doesn't block forever
    timeoutMs = 5000;
		try {
      recvSoc.setSoTimeout(timeoutMs);
    } catch (SocketException e) {
      System.err.println("UDPServer Exception");
      e.printStackTrace();
			System.exit(-1);
    }
    while (true) {
      try {
        pacData = new byte[256];
        pac = new DatagramPacket(pacData, pacData.length);
        recvSoc.receive(pac);
        pacSize = pac.getLength();
        pacData = pac.getData();
        processMessage(new String(pacData, 0, pacSize));
      } catch (SocketTimeoutException e) {
        System.err.println("UDPServer Exception.Socket timed out");
//        e.printStackTrace();
        printSummary();
        clean();
      } catch (Exception e) {
        System.err.println("UDPServer Exception");
        e.printStackTrace();
      }
    }


	}

	public void processMessage(String data) {

		MessageInfo msg = null;

		try {
		  // TO-DO: Use the data to construct a new MessageInfo object
      msg = new MessageInfo(data);
		  // TO-DO: On receipt of first message, initialise the receive buffer
      if (receivedMessages == null) {
        totalMessages = msg.totalMessages;
        receivedMessages = new boolean[totalMessages];
        messageCount = 0;
      }
      //System.out.println("Message received: " + (msg.messageNum + 1) + "/" + totalMessages);
      messageCount++;

		  // TO-DO: Log receipt of the message
		  receivedMessages[msg.messageNum] = true; 

		  // TO-DO: If this is the last expected message, then identify
		  //        any missing messages
		  if (msg.messageNum == totalMessages - 1) {
        printSummary();
        clean();
      }
    } catch (Exception e) {
      System.err.println("UDPServer Process Message Exception");
      e.printStackTrace();
    }
	}
  private void clean() {
    receivedMessages = null;
    messageCount = 0;
    totalMessages = 0;
  }

  private void printSummary() {
    String missing = (totalMessages - messageCount) + " messages out of " + totalMessages + " were lost: ";
    //for (int i = 0; i < totalMessages; i++) {
    //  if (!receivedMessages[i]) {
    //    missing += i + ", ";
    //  }
    //}
    System.out.println(missing);
  }

	public UDPServer(int rp) {
		// TO-DO: Initialise UDP socket for receiving data
		try {
		  recvSoc = new DatagramSocket(rp);
    } catch (SocketException e) {
      System.err.println("Unable to start UDPSocket");
      e.printStackTrace();
			System.exit(-1);
    }
     
		// Done Initialisation
		System.out.println("UDPServer ready");
	}

	public static void main(String args[]) {
		int	recvPort;

		// Get the parameters from command line
		if (args.length < 1) {
			System.err.println("Arguments required: recv port");
			System.exit(-1);
		}
		recvPort = Integer.parseInt(args[0]);

		// TO-DO: Construct Server object and start it by calling run().
		UDPServer udpserver = new UDPServer(recvPort);
    udpserver.run();

	}

}
