package entities;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
	private float angleAroundPlayer=0;

	private float pitch=0;
	private float yaw=0;
	private float roll;
	
	private float maxPitch = 90;
    private float minPitch = -90; 

	private Player player;
	
	public Camera(Player player) {
		this.player=player;
	}

	public void move() {
		calculatePitch();
		this.yaw=180-(player.getRotY()+angleAroundPlayer);
	}
	
	public Vector3f getPosition() {
		return new Vector3f(player.getPosition().x, player.getPosition().y + 10, player.getPosition().z);
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}
	
	private void calculatePitch() {
		float pitchChange=Mouse.getDY()*0.1f;
		pitch-=pitchChange;
		
        if (pitch < minPitch) {
            pitch = minPitch;
        } else if (pitch > maxPitch) {
            pitch = maxPitch;
        }
	}
}
