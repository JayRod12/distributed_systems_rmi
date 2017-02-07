/*
 * Created on 01-Mar-2016
 */
package rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.registry.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;

import common.*;

public class RMIServer extends UnicastRemoteObject implements RMIServerI {

	private int totalMessages = -1;
	private int messageCount = -1;
	private boolean[] receivedMessages;

	public RMIServer() throws RemoteException {
	}

	public void receiveMessage(MessageInfo msg) throws RemoteException {

		// On receipt of first message, initialise the receive buffer
		if (receivedMessages == null) {
      totalMessages = msg.totalMessages;
      receivedMessages = new boolean[totalMessages];
      messageCount = 0;
    }
    messageCount++;

		// Log receipt of the message
		receivedMessages[msg.messageNum] = true;


		// If this is the last expected message, then identify
		// any missing messages
		if (msg.messageNum == totalMessages - 1) {
      Util.printSummary(messageCount, totalMessages);
      clean();
    }
	}


  private void clean() {
    receivedMessages = null;
    messageCount = 0;
    totalMessages = 0;
  }

	public static void main(String[] args) {

    int recvPort = 8000;
		RMIServer rmis = null;

		// Initialise Security Manager
    if (System.getSecurityManager() == null) {
      System.setSecurityManager(new SecurityManager()); } try {
		  // Instantiate the server class and bind to RMI registry
      rmis = new RMIServer();
      rebindServer("RMIServer", rmis, recvPort);

    } catch (Exception e) {
      System.err.println("RMIServer Exception");
      e.printStackTrace();
    }
	}

	protected static void rebindServer(String serverURL, RMIServer server, int recvPort) {
    try {
		  Registry registry = LocateRegistry.createRegistry(recvPort);
		  registry.rebind(serverURL, server);
      System.out.println("RMIServer ready");
    } catch (Exception e) {
      System.err.println("RMIServer rebind server exception");
      e.printStackTrace();
    }
	}
}
