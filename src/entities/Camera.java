package entities;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
	
	private float distanceFromPlayer=50;
	private float angleAroundPlayer=0;
	
	private Vector3f position=new Vector3f(0,0,0);
	private float pitch=20;
	private float yaw=0;
	private float roll;
	
	private Player player;
	
	public Camera(Player player) {
		this.player=player;
	}

	public void move() {
		calculateZoom();
		calculatePitch();
		calculateAngleAroundPlayer();
		float horizontalDistance=calculateHorizontalDistance();
		float verticalDistance=calculateVerticalDistance();
		calculateCameraPosition(horizontalDistance,verticalDistance);
		this.yaw=180-(player.getRotY()+angleAroundPlayer);
	}
	
	public Vector3f getPosition() {
		return position;
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
	
	private void calculateCameraPosition(float horizDistance,float verticDistance) {
		float theta=player.getRotY()+angleAroundPlayer;
		float offsetX=(float)(horizDistance*Math.sin(Math.toRadians(theta)));
		float offsetZ=(float)(horizDistance*Math.cos(Math.toRadians(theta)));
		position.x=player.getPosition().x-offsetX;
		position.z=player.getPosition().z-offsetZ;
		position.y=player.getPosition().y+verticDistance;
	}
	
	private float calculateHorizontalDistance() {
		float horizontalDist=(float) (distanceFromPlayer*Math.cos(Math.toRadians(pitch)));
		if(horizontalDist<0) {
			horizontalDist=0;
		}
		return horizontalDist;
	}
	
	private float calculateVerticalDistance() {
		float verticalDist=(float) (distanceFromPlayer*Math.sin(Math.toRadians(pitch)));
		if(verticalDist<0) {
			verticalDist=0;
		}
		return verticalDist;
	}
	
	private void calculateZoom() {
		float zoomLevel=Mouse.getDWheel()*0.1f;
		distanceFromPlayer-=zoomLevel;
	}
	
	private void calculatePitch() {
		if(Mouse.isButtonDown(1)) {
			float pitchChange=Mouse.getDY()*0.1f;
			pitch-=pitchChange;
			if(pitch<0) {
				pitch=0;
			}else if(pitch>90) {
				pitch=90;
			}
		}
	}
	
	private void calculateAngleAroundPlayer() {
		if(Mouse.isButtonDown(1)) {
			float angleChange=Mouse.getDX()*0.3f;
			angleAroundPlayer-=angleChange;
		}
	}
}
