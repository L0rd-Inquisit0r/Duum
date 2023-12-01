package engineTester;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Light;
import entities.Player;
import models.RawModel;
import models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

public class MainGameLoop {

	public static void main(String[] args) {
		DisplayManager.createDisplay();
		Loader loader=new Loader();
				
		TerrainTexture backgroundTexture=new TerrainTexture(loader.loadTexture("DOOM_E1M1/FLOOR4_8"));
		TerrainTexture rTexture=new TerrainTexture(loader.loadTexture("DOOM_E1M1/FLOOR5_2"));
		TerrainTexture gTexture=new TerrainTexture(loader.loadTexture("DOOM_E1M1/FLOOR6_2"));
		TerrainTexture bTexture=new TerrainTexture(loader.loadTexture("DOOM_E1M1/FLOOR5_1"));
		
		TerrainTexturePack texturePack=new TerrainTexturePack(backgroundTexture,rTexture,gTexture,bTexture);
		TerrainTexture blendMap=new TerrainTexture(loader.loadTexture("arena"));
		
		Light light=new Light(new Vector3f(20000,40000,20000),new Vector3f(1,1,1));
		
		Terrain terrain=new Terrain(0,-1,loader,texturePack,blendMap);
		
		MasterRenderer renderer=new MasterRenderer();
		
		RawModel model=OBJLoader.loadbjModel("cube",loader);
		TexturedModel playerModel=new TexturedModel(model,new ModelTexture(loader.loadTexture("misery")));
		
		Player player=new Player(playerModel,new Vector3f(100,0,-50),0,180,0,1);
		Camera camera=new Camera(player);
		
		while(!Display.isCloseRequested()&&!Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			camera.move();
			player.move();
			renderer.processEntity(player);
			renderer.processTerrain(terrain);
			renderer.render(light, camera);
			DisplayManager.updateDisplay();
		}
		
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
}
