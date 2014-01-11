package com.alec.vpongserver;

import java.io.IOException;

import com.alec.vpongserver.Packet.Packet0LoginRequest;
import com.alec.vpongserver.Packet.Packet1LoginAnswer;
import com.alec.vpongserver.Packet.Packet2PlayerPositions;
import com.alec.vpongserver.Packet.Packet3ClientInfo;
import com.alec.vpongserver.Packet.Packet4RequestPositions;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Server;

public class MPServer {
	
	private Server server;
	public ServerNetworkListener nl = new ServerNetworkListener();
	public static MPServer mpServer;
	
	public static void main(String[] args) {
		try {
			mpServer = new MPServer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public MPServer() throws IOException {
		server = new Server(); // create a new server with default packet dimensions
		registerPackets();
		server.addListener(nl);
		server.bind(52555, 52555);
		server.start();
	}
	
	private void registerPackets() {
		Kryo kryo = server.getKryo();	
		kryo.register(Packet0LoginRequest.class);
		kryo.register(Packet1LoginAnswer.class);
		kryo.register(Packet2PlayerPositions.class);
		kryo.register(Packet3ClientInfo.class);
		kryo.register(Packet4RequestPositions.class);
		kryo.register(Vector2.class);
		kryo.register(PlayerData.class);
	}
	
	
}
