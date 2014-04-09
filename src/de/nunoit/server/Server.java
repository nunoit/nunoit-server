package de.nunoit.server;

import de.nunoit.networking.protocol.Protocol;

public class Server {

	public static void main(String[] args) {
		Protocol.init();
		
		ServerHandler s = new ServerHandler(7654);
		try {
			s.start();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
