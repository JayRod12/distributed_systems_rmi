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

import common.*;

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
    int timeoutMs = 5000;

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
        Util.printSummary(messageCount, totalMessages);
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
      msg = new MessageInfo(data);
      if (receivedMessages == null) {
        totalMessages = msg.totalMessages;
        receivedMessages = new boolean[totalMessages];
        messageCount = 0;
      }
      messageCount++;
		  receivedMessages[msg.messageNum] = true; 
		  if (msg.messageNum == totalMessages - 1) {
        Util.printSummary(messageCount, totalMessages);
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


	public UDPServer(int rp) {
		try {
		  recvSoc = new DatagramSocket(rp);
    } catch (SocketException e) {
      System.err.println("Unable to start UDPSocket");
      e.printStackTrace();
			System.exit(-1);
    }
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

		UDPServer udpserver = new UDPServer(recvPort);
    udpserver.run();

	}

}
