package engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
		
		TexturedModel grass=new TexturedModel(OBJLoader.loadbjModel("res/grassModel", loader),
				new ModelTexture(loader.loadTexture("res/grassTexture")));
		grass.getTexture().setHasTransparency(true);
		grass.getTexture().setUseFakeLighting(true);
		TexturedModel fern=new TexturedModel(OBJLoader.loadbjModel("res/fern", loader),
				new ModelTexture(loader.loadTexture("res/fern")));
		fern.getTexture().setHasTransparency(true);
		
		List<Entity> entities=new ArrayList<Entity>();
		Random random=new Random();
		for(int i=0;i<500;i++) {
			entities.add(new Entity(staticModel,new Vector3f(random.nextFloat()*800-400,0,
					random.nextFloat()*-600),0,0,0,3));
			entities.add(new Entity(grass,new Vector3f(random.nextFloat()*800-400,0,
					random.nextFloat()*-600),0,0,0,1));
			entities.add(new Entity(fern,new Vector3f(random.nextFloat()*800-400,0,
					random.nextFloat()*-600),0,0,0,0.6f));
		}
		
		Light light=new Light(new Vector3f(20000,20000,20000),new Vector3f(1,1,1));
		
		Terrain terrain=new Terrain(-1,-1,loader,new ModelTexture(loader.loadTexture("res/grass")));
		Terrain terrain2=new Terrain(0,-1,loader,new ModelTexture(loader.loadTexture("res/grass")));
		
		Camera camera=new Camera();
		MasterRenderer renderer=new MasterRenderer();
		
		while(!Display.isCloseRequested()) {
			camera.move();
			renderer.processTerrain(terrain);
			renderer.processTerrain(terrain2);
			for(Entity entity:entities) {
				renderer.processEntity(entity);
			}
			renderer.render(light, camera);
			DisplayManager.updateDisplay();
		}
		
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
}
