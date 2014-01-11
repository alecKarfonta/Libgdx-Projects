package MPClient;

import java.io.IOException;
import java.util.Scanner;

import javax.swing.JOptionPane;

import MPClient.Packet.Packet0LoginRequest;
import MPClient.Packet.Packet1LoginAnswer;
import MPClient.Packet.Packet2PlayerPositions;
import MPClient.Packet.Packet3ClientInfo;
import MPClient.Packet.Packet4RequestPositions;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;

public class MPClient {
	
	public Client client;
	public Scanner scanner; 
	public ClientNetworkListener nl;
	
	public MPClient() throws IOException {
		client = new Client();
		registerPackets();
		nl = new ClientNetworkListener();
		nl.init(client);
		client.addListener(nl);
		client.start();
		try {
			String server =	JOptionPane.showInputDialog("Enter the ip of the server", "");
			client.connect(5000, server, 52555, 52555);
			
		} catch(Exception ex) {
			ex.printStackTrace();
			System.out.println("could not connect to server");
			client.stop();
		}
	}
	
	public void updatePosition(Vector2 newPosition) {
		nl.updatePosition(newPosition);
	}
	
	private void registerPackets() {
		Kryo kryo = client.getKryo();	
		kryo.register(Packet0LoginRequest.class);
		kryo.register(Packet1LoginAnswer.class);
		kryo.register(Packet2PlayerPositions.class);
		kryo.register(Packet3ClientInfo.class);
		kryo.register(Packet4RequestPositions.class);
		kryo.register(Vector2.class);
		kryo.register(PlayerData.class);
	}
}
