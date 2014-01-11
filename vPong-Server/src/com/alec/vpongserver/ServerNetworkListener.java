package com.alec.vpongserver;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.alec.vpongserver.Packet.Packet0LoginRequest;
import com.alec.vpongserver.Packet.Packet1LoginAnswer;
import com.alec.vpongserver.Packet.Packet2PlayerPositions;
import com.alec.vpongserver.Packet.Packet3ClientInfo;
import com.alec.vpongserver.Packet.Packet4RequestPositions;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class ServerNetworkListener extends Listener {
	PlayerData player1 , player2;
	
	// debug
	JPanel jpDisplay = new JPanel();
	JPanel jpPlayerData = new JPanel();
	JTextArea jtaDisplay = new JTextArea();
	JScrollPane jspScroll;
	JLabel jlPlayer1Position, jlPlayer2Position;
	
	
	public ServerNetworkListener() {
		super();
		JFrame frame = new JFrame();
		Dimension windowSize = new Dimension(600, 400);
		
		jlPlayer1Position = new JLabel("(?,?)");
		jlPlayer2Position = new JLabel("(?,?)");
		
		jpDisplay.setLayout(new BorderLayout());
		jpPlayerData.setLayout(new GridLayout(2,2));
		jpDisplay.setPreferredSize(windowSize);
		jspScroll = new JScrollPane(jtaDisplay);
		jspScroll.setPreferredSize(new Dimension(500, 350));
		jpPlayerData.setPreferredSize(new Dimension(100, 50));
		
		jpPlayerData.add(new JLabel("Player 1: "));
		jpPlayerData.add(jlPlayer1Position);
		jpPlayerData.add(new JLabel("Player 2: "));
		jpPlayerData.add(jlPlayer2Position);
		
		jpDisplay.add(jpPlayerData, BorderLayout.NORTH);
		
		jpDisplay.add(jtaDisplay, BorderLayout.SOUTH);
		
		frame.add(jpDisplay);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
		frame.setTitle("Server Log");
		jtaDisplay.append("Server Online \n");
	}
	
	@Override
	public void received(Connection con, Object obj) {
		// if client sends login request
		if (obj instanceof Packet0LoginRequest) {
			// if there is no player 1
			if (player1 == null) {
				// create player 1
				Packet1LoginAnswer loginAnswer = new Packet1LoginAnswer();
				loginAnswer.accepted = true;
				loginAnswer.playerId = 1; // the client keeps track of which player they are
				player1 = new PlayerData(1);
				jtaDisplay.append("Player 1 created \n");
				jtaDisplay.setCaretPosition(jtaDisplay.getDocument().getLength());
				con.sendTCP(loginAnswer);
			// else if there is no player 2
			} else if (player2 == null) {
					// create player 2
					Packet1LoginAnswer loginAnswer = new Packet1LoginAnswer();
					loginAnswer.accepted = true;
					loginAnswer.playerId = 2; 
					player2 = new PlayerData(2);
					jtaDisplay.append("Player 2 created \n");
					jtaDisplay.setCaretPosition(jtaDisplay.getDocument().getLength());
					con.sendTCP(loginAnswer);
			// else both players have been created
			} else {
				Packet1LoginAnswer loginAnswer = new Packet1LoginAnswer();
				jtaDisplay.append("Player rejected room full \n");
				jtaDisplay.setCaretPosition(jtaDisplay.getDocument().getLength());
				con.sendTCP(loginAnswer); // reject client
			}	
		// if a client sends their position
		} else if (obj instanceof Packet3ClientInfo) {
			Packet3ClientInfo clientInfo = (Packet3ClientInfo)obj;
			// if the packet came from player 1
			if (clientInfo.playerData.playerId == 1) {
				player1.setPosition(clientInfo.playerData.position);
				jlPlayer1Position.setText("(" + player1.getPosition().x + "," + player1.getPosition().y + ")");
			// else if the packet came from player 2 	
			} else if (clientInfo.playerData.playerId == 2) { 
				player2.setPosition(clientInfo.playerData.position);
				jlPlayer2Position.setText("(" + player2.getPosition().x + "," + player2.getPosition().y + ")");
			}
		} else if (obj instanceof Packet4RequestPositions) {
			Packet2PlayerPositions playerPos = new Packet2PlayerPositions();
			playerPos.player1 = player1;
			playerPos.player2 = player2;
			con.sendTCP(playerPos);
		}
		
	}	
	@Override
	public void connected(Connection arg0) {
		jtaDisplay.append("User connected \n");
	}
	@Override
	public void disconnected(Connection arg0) {
		jtaDisplay.append("User disconnected \n");
	}
	
}
