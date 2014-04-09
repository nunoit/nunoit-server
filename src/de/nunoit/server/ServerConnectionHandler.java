package de.nunoit.server;

import de.nunoit.networking.PacketHandler;
import de.nunoit.networking.protocol.Packet;
import de.nunoit.networking.protocol.PacketWrapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

@RequiredArgsConstructor
public class ServerConnectionHandler extends ChannelHandlerAdapter {

	@NonNull
	private Channel ch;

	private PacketHandler handler;

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		handler = new ServerPacketHandler(this);
		handler.connected();
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		disconnect();
		// TODO
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		PacketWrapper p = (PacketWrapper) msg;
		try {
			p.getPacket().handle(handler);
		} finally {
			p.release();
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		// TODO
		System.out.println("Exception: " + cause.getMessage());
	}

	public void disconnect() {
		if (ch != null && !ch.isOpen()) {
			ch.close();
		}
	}

	public void send(Packet packet) {
		if (ch != null && ch.isWritable()) {
			ch.write(packet, ch.voidPromise());
		}
	}

	public void flush() {
		if (ch != null && ch.isWritable()) {
			ch.flush();
		}
	}
}
