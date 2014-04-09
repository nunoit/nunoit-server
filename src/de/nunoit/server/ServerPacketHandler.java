package de.nunoit.server;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import de.nunoit.networking.PacketHandler;
import de.nunoit.networking.protocol.packets.Connect;
import de.nunoit.networking.protocol.packets.ImageRequest;
import de.nunoit.networking.protocol.packets.ImageTask;
import de.nunoit.networking.protocol.packets.TaskCancel;

@RequiredArgsConstructor
public class ServerPacketHandler extends PacketHandler {

	@NonNull
	private ServerConnectionHandler connection;

	@Override
	public void handle(Connect connect) {
		// HI USER!
		System.out.println("User connected: " + connect.getUuid());
	}

	@Override
	public void handle(ImageRequest request) {
		connection.send(new ImageTask(-1, 0, 1, 2, 3, 4));
		connection.flush();
	}

	@Override
	public void handle(TaskCancel cancel) {
		connection.disconnect();
	}
}
