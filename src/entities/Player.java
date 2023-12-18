package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import models.TexturedModel;
import renderEngine.DisplayManager;
import terrains.Terrain;
import toolbox.MousePicker;

public class Player extends Entity{
	
	private static final float RUN_SPEED=75;
	private static final float STRAFE_SPEED=75;
	private static final float GRAVITY=-80;
	private static final float JUMP_POWER=40;
	private static final float SHOOT_RANGE=100;

	private float currentSpeed=0;
	private float currentStrafeSpeed = 0;
	private float currentTurnSpeed=0;
	private float upwardsSpeed=0;
	
	private int shotsCount=0;
	
	private boolean isInAir=false;
	
	private boolean mouseButtonPreviouslyPressed = false;
	
	public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
	}
	
	public void move(Terrain terrain) {
		checkInputs();
		super.increaseRotation(0,currentTurnSpeed*DisplayManager.getFrameTimeSeconds(),0);
		
		float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();
        float strafe = currentStrafeSpeed * DisplayManager.getFrameTimeSeconds();

		
		float dx=(float)(distance*Math.sin(Math.toRadians(super.getRotY())));
		float dz=(float)(distance*Math.cos(Math.toRadians(super.getRotY())));
		
		float strafeDx=(float)(strafe*Math.sin(Math.toRadians(super.getRotY()+90)));
        float strafeDz=(float)(strafe*Math.cos(Math.toRadians(super.getRotY()+90)));

		
        super.increasePosition(dx+strafeDx,0,dz+strafeDz);
		
		upwardsSpeed+=GRAVITY*DisplayManager.getFrameTimeSeconds();
		super.increasePosition(0,upwardsSpeed*DisplayManager.getFrameTimeSeconds(),0);
		
		float terrainHeight=terrain.getHeightOfTerrain(super.getPosition().x,super.getPosition().z);
		if(super.getPosition().y<terrainHeight) {
			upwardsSpeed=0;
			isInAir=false;
			super.getPosition().y=terrainHeight;
		}
	}
	
	private void jump() {
		if(!isInAir) {
			this.upwardsSpeed=JUMP_POWER;
			isInAir=true;
		}
	}
	
	private void checkInputs() {
		if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
			this.currentSpeed=RUN_SPEED;
		}else if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
			this.currentSpeed=-RUN_SPEED;
		}else {
			this.currentSpeed=0;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            this.currentStrafeSpeed = STRAFE_SPEED;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            this.currentStrafeSpeed = -STRAFE_SPEED;
        } else {
            this.currentStrafeSpeed = 0;
        }
		
		this.currentTurnSpeed = -Mouse.getDX();
		
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			jump();
		}
	}
	
	public void shoot(Enemy enemy,MousePicker picker) {
		boolean isMouseButtonDown = Mouse.isButtonDown(0);
		
        if (isMouseButtonDown && !mouseButtonPreviouslyPressed) {
            Vector3f ray = picker.getCurrentRay();
            Vector3f playerToEnemy = new Vector3f(
                    enemy.getPosition().x - getPosition().x,
                    enemy.getPosition().y - getPosition().y,
                    enemy.getPosition().z - getPosition().z
            );

            float angle = Vector3f.angle(ray, playerToEnemy);
            float distanceToEnemy = playerToEnemy.length();

            if (angle < 0.1f && distanceToEnemy < SHOOT_RANGE) {
                hit(enemy);
            }
        }
        mouseButtonPreviouslyPressed = isMouseButtonDown;
    }
	
	private void hit(Enemy enemy) {
        System.out.println("HIT!");
        shotsCount++;
    }
	
	public int getShotsCount() {
        return shotsCount;
    }
}
