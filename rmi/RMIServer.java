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
    System.out.println("Message received: " + (msg.messageNum + 1) + "/" + totalMessages);
    messageCount++;

		// Log receipt of the message
		receivedMessages[msg.messageNum] = true;

		// If this is the last expected message, then identify
		// any missing messages
		if (msg.messageNum == totalMessages - 1) {
      String missing = (totalMessages - messageCount) + " messages out of " + totalMessages + " were lost: ";
      for (int i = 0; i < totalMessages; i++) {
        if (!receivedMessages[i]) {
          missing += i + ", ";
        }
      }
      System.out.println(missing);
      clean();
    }
	}

  private void clean() {
    receivedMessages = null;
  }

	public static void main(String[] args) {

		RMIServer rmis = null;

		// Initialise Security Manager
    if (System.getSecurityManager() == null) {
      System.setSecurityManager(new SecurityManager());
    }

    try {
		  // Instantiate the server class and bind to RMI registry
      rmis = new RMIServer();
      rebindServer("RMIServer", rmis);

    } catch (Exception e) {
      System.err.println("RMIServer Exception");
      e.printStackTrace();
    }
	}

	protected static void rebindServer(String serverURL, RMIServer server) {
    try {
		  Registry registry = LocateRegistry.createRegistry(8000);
		  registry.rebind(serverURL, server);
    } catch (Exception e) {
      System.err.println("RMIServer rebind server exception");
      e.printStackTrace();
    }
	}
}
