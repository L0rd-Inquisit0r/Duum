package entities;

import org.lwjgl.util.vector.Vector3f;

import models.TexturedModel;
import renderEngine.DisplayManager;
import terrains.Terrain;

public class Enemy extends Entity {

    private static final float ENEMY_SPEED = 40;
    private static final float DETECTION_RANGE = 50;

    private Player player;

    public Enemy(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale, Player player) {
        super(model, position, rotX, rotY, rotZ, scale);
        this.player = player;
    }

    public void move(Terrain terrain) {
    	 // Calculate the distance between the player and the enemy
        float distanceToPlayer = calculateDistance(player);
        
        // Calculate the direction vector from the enemy to the player
        Vector3f playerPos = player.getPosition();
        Vector3f enemyToPlayer = new Vector3f(playerPos.x - getPosition().x, 0, playerPos.z - getPosition().z);
        enemyToPlayer.normalise();
        // Check if the player is within the detection range
        if (distanceToPlayer < DETECTION_RANGE) {
            // Move the enemy towards the player
            float distance = ENEMY_SPEED * DisplayManager.getFrameTimeSeconds();
            float dx = distance * enemyToPlayer.x;
            float dz = distance * enemyToPlayer.z;

            super.increasePosition(dx, 0, dz);
        }
        // Face the player
        float angle = (float) Math.toDegrees(Math.atan2(enemyToPlayer.x, enemyToPlayer.z));
        super.setRotY(angle);
        // else: The player is outside the detection range, so the enemy stays in place facing the player
    }
    
    private float calculateDistance(Player player) {
        // Calculate the distance between this entity and another entity
        Vector3f thisPos = getPosition();
        Vector3f otherPos = player.getPosition();
        float dx = thisPos.x - otherPos.x;
        float dy = thisPos.y - otherPos.y;
        float dz = thisPos.z - otherPos.z;
        return (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
    }
}