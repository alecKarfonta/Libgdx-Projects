package MPClient;

import com.badlogic.gdx.math.Vector2;

public class PlayerData {
	int playerId;
	Vector2 position;

	public PlayerData() {
		
	}
	
	public PlayerData(int playerId) {
		this(playerId, new Vector2(0,0));
	}
	
	public PlayerData(int playerId, Vector2 position) {
		super();
		this.playerId = playerId;
		this.position = position;
	}

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public Vector2 getPosition() {
		return position;
	}

	public void setPosition(Vector2 position) {
		this.position = position;
	}
	
	
}
