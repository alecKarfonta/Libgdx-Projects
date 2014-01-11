package MPClient;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import MPClient.Packet.Packet0LoginRequest;
import MPClient.Packet.Packet1LoginAnswer;
import MPClient.Packet.Packet2PlayerPositions;
import MPClient.Packet.Packet3ClientInfo;
import MPClient.Packet.Packet4RequestPositions;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class ClientNetworkListener extends Listener {
	private Client client;
	private PlayerData playerData;
	public Vector2 player2Position;
	
	// debug
	JPanel jpDisplay = new JPanel();

	JPanel jpPlayerData = new JPanel();
	JTextArea jtaDisplay = new JTextArea();
	JScrollPane jspScroll;
	JLabel jlPlayer1Position, jlPlayer2Position;
	JLabel jlTimeSinceUpdate;
	long startTime;
	
	public void init(Client client) {
			this.client = client;
			this.playerData = null;
			this.player2Position = null;
			
			// debug panel
			startTime = System.currentTimeMillis();
			JFrame frame = new JFrame();
			Dimension windowSize = new Dimension(610, 410);
			jpDisplay.setLayout(new BorderLayout());
			jpPlayerData.setLayout(new GridLayout(3,3));
			jpDisplay.setPreferredSize(new Dimension(500, 350));
			jpPlayerData.setPreferredSize(new Dimension(100, 50));
			
			
			jlPlayer1Position = new JLabel("(?,?)");
			jlPlayer2Position = new JLabel("(?,?)");
			jlTimeSinceUpdate = new JLabel("?");
			
			jpPlayerData.add(new JLabel("Player 1: "));
			jpPlayerData.add(jlPlayer1Position);
			jpPlayerData.add(new JLabel("Player 2: "));
			jpPlayerData.add(jlPlayer2Position);
			
			jpDisplay.add(jpPlayerData, BorderLayout.NORTH);
			
			jspScroll = new JScrollPane(jtaDisplay);
			
			jpDisplay.add(jspScroll, BorderLayout.CENTER);
			
			frame.add(jpDisplay);
			
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setSize(windowSize);
			frame.setTitle("Client Log");
			frame.setVisible(true);
	}
	
	@Override
	public void received(Connection con, Object obj) {
		// if the client recieves a response to a login attempt
		if (obj instanceof Packet1LoginAnswer) {
			// if the login was accepted
			if (((Packet1LoginAnswer)obj).accepted == true) {
				// assign the local playerData to the id given by the server
				playerData = new PlayerData(((Packet1LoginAnswer) obj).playerId);
				jtaDisplay.append("Player " + playerData.playerId + "\n");
				
			}
		// else if the client recieves the player position data
		} else if (obj instanceof Packet2PlayerPositions) {
			jtaDisplay.append("Player Positions Recieved at : " + ((System.currentTimeMillis() - startTime) / 1000) + " seconds \n" );
			jtaDisplay.setCaretPosition(jtaDisplay.getDocument().getLength());
			// if the client is a player ?
			if (playerData != null) {
				// if the client is player 1
				if (playerData.playerId == 1) {
					jlPlayer1Position.setText("(" + playerData.getPosition().x + "," + playerData.getPosition().y + ")");
					if (((Packet2PlayerPositions) obj).player2 != null) {
						jlPlayer2Position.setText("(" + ((Packet2PlayerPositions) obj).player2.getPosition().x + "," + ((Packet2PlayerPositions) obj).player2.getPosition().y + ")");
						// assign the left bumper to the player 2 position, later make this opposite sides
						player2Position = ((Packet2PlayerPositions) obj).player2.getPosition();
					}
				// else if the client is player 2
				} else if (playerData.playerId == 2) {
					jlPlayer2Position.setText("(" + playerData.getPosition().x + "," + playerData.getPosition().y + ")");
					if (((Packet2PlayerPositions) obj).player1 != null) {
						jlPlayer1Position.setText("(" + ((Packet2PlayerPositions) obj).player1.getPosition().x + "," + ((Packet2PlayerPositions) obj).player1.getPosition().y + ")");
						// assign the left bumper to the player 1 position
						player2Position = ((Packet2PlayerPositions) obj).player1.getPosition();
					}
				}
			}
		}
	}		
	
	// function to update player positions 
	public void updatePosition(Vector2 newPosition) {
		if (playerData != null) {
			playerData.setPosition(newPosition);
			Packet3ClientInfo playerDataPacket = new Packet3ClientInfo();
			playerDataPacket.playerData = playerData;
			client.sendTCP(playerDataPacket);
		}
	}
	
	public void requestPositionUpdate() {
		client.sendTCP(new Packet4RequestPositions());
	}
	
	@Override
	public void connected(Connection arg0) {
		client.sendTCP(new Packet0LoginRequest());
	}
	@Override
	public void disconnected(Connection arg0) {
	}
	
	
}
