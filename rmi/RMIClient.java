/*
 * Created on 01-Mar-2016
 */
package rmi;

import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.*;

import common.MessageInfo;

public class RMIClient {

	public static void main(String[] args) {

		RMIServerI iRMIServer = null;

		// Check arguments for Server host and number of messages
		if (args.length < 2){
			System.out.println("Needs 2 arguments: ServerHostName/IPAddress, TotalMessageCount");
			System.exit(-1);
		}

		int numMessages = Integer.parseInt(args[1]);
    int recvPort = 8000;

		// Initialise Security Manager
    if (System.getSecurityManager() == null) {
      System.setSecurityManager(new SecurityManager());
    }

    try {
		  // Bind to RMIServer
      Registry registry = LocateRegistry.getRegistry(args[0], recvPort);
      iRMIServer = (RMIServerI) registry.lookup("RMIServer");
		  // Attempt to send messages the specified number of times
		  for (int i = 0; i < numMessages; i++) {
        MessageInfo msg = new MessageInfo(numMessages + ";" + i);
        iRMIServer.receiveMessage(msg);
      }
    } catch (Exception e) {
      System.err.println("RMIClient Exception");
      e.printStackTrace();
    }
	}
}
