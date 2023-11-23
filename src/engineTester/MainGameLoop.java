package engineTester;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.RawModel;
import models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrains.Terrain;
import textures.ModelTexture;

public class MainGameLoop {

	public static void main(String[] args) {
		DisplayManager.createDisplay();
		Loader loader=new Loader();
				
		RawModel model=OBJLoader.loadbjModel("res/tree", loader);
		
		TexturedModel staticModel=new TexturedModel(model,new ModelTexture(loader.loadTexture("res/tree")));
		ModelTexture texture=staticModel.getTexture();
		
		Entity entity=new Entity(staticModel,new Vector3f(0,0,-25),0,0,0,1);
		Light light=new Light(new Vector3f(3000,2000,2000),new Vector3f(1,1,1));
		
		Terrain terrain=new Terrain(-1,-1,loader,new ModelTexture(loader.loadTexture("res/grass")));
		Terrain terrain2=new Terrain(0,-1,loader,new ModelTexture(loader.loadTexture("res/grass")));
		
		Camera camera=new Camera();
		MasterRenderer renderer=new MasterRenderer();
		
		while(!Display.isCloseRequested()) {
			camera.move();
			renderer.processTerrain(terrain);
			renderer.processTerrain(terrain2);
			renderer.processEntity(entity);
			renderer.render(light, camera);
			DisplayManager.updateDisplay();
		}
		
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
}
