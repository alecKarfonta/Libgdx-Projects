package MPClient;

import com.badlogic.gdx.math.Vector2;

public class Packet {
	public static class Packet0LoginRequest { }
	public static class Packet1LoginAnswer { public boolean accepted = false; public int playerId;}
	public static class Packet2PlayerPositions { public PlayerData player1, player2; }
	public static class Packet3ClientInfo { public PlayerData playerData; }
	public static class Packet4RequestPositions { };
}
