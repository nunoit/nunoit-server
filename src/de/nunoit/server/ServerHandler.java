package de.nunoit.server;

import de.nunoit.networking.FrameDecoder;
import de.nunoit.networking.FramePrepender;
import de.nunoit.networking.PacketDecoder;
import de.nunoit.networking.PacketEncoder;
import lombok.RequiredArgsConstructor;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

@RequiredArgsConstructor
public class ServerHandler {

	private final int port;

	private EventLoopGroup eventLoops;

	public void start() throws InterruptedException {
		try {
			eventLoops = new NioEventLoopGroup();

			ServerBootstrap b = new ServerBootstrap();
			b.group(eventLoops);
			b.channel(NioServerSocketChannel.class);
			b.childHandler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel channel)
						throws Exception {
					channel.pipeline().addLast(new FramePrepender());
					channel.pipeline().addLast(new FrameDecoder());
					channel.pipeline().addLast(new PacketEncoder());
					channel.pipeline().addLast(new PacketDecoder());
					channel.pipeline().addLast(
							new ServerConnectionHandler(channel));
				}
			});
			ChannelFuture f = b.bind(port).sync();

			f.channel().closeFuture().sync();
		} finally {
			eventLoops.shutdownGracefully();
		}
	}

}
