package entities;

import org.lwjgl.util.vector.Vector3f;

import models.TexturedModel;
import renderEngine.DisplayManager;
import terrains.Terrain;

public class Enemy extends Entity {

    private static final float ENEMY_SPEED = 40;
    private static final float DETECTION_RANGE = 50;

    public Enemy(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        super(model, position, rotX, rotY, rotZ, scale);
    }

    public void move(Terrain terrain,Player player) {
        float distanceToPlayer = calculateDistance(player);
        
        Vector3f playerPos = player.getPosition();
        Vector3f enemyToPlayer = new Vector3f(playerPos.x - getPosition().x, 0, playerPos.z - getPosition().z);
        enemyToPlayer.normalise();

        if (distanceToPlayer < DETECTION_RANGE) {
            float distance = ENEMY_SPEED * DisplayManager.getFrameTimeSeconds();
            float dx = distance * enemyToPlayer.x;
            float dz = distance * enemyToPlayer.z;

            super.increasePosition(dx, 0, dz);
        }
        float angle = (float) Math.toDegrees(Math.atan2(enemyToPlayer.x, enemyToPlayer.z));
        super.setRotY(angle);
    }
    
    private float calculateDistance(Player player) {
        Vector3f thisPos = getPosition();
        Vector3f otherPos = player.getPosition();
        float dx = thisPos.x - otherPos.x;
        float dy = thisPos.y - otherPos.y;
        float dz = thisPos.z - otherPos.z;
        return (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
    }
}