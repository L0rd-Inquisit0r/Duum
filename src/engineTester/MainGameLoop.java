package engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
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
				
		TerrainTexture backgroundTexture=new TerrainTexture(loader.loadTexture("grassy2"));
		TerrainTexture rTexture=new TerrainTexture(loader.loadTexture("mud"));
		TerrainTexture gTexture=new TerrainTexture(loader.loadTexture("grassFlowers"));
		TerrainTexture bTexture=new TerrainTexture(loader.loadTexture("path"));
		
		TerrainTexturePack texturePack=new TerrainTexturePack(backgroundTexture,rTexture,gTexture,bTexture);
		TerrainTexture blendMap=new TerrainTexture(loader.loadTexture("blendMap"));
		
		RawModel model=OBJLoader.loadbjModel("tree",loader);
		
		TexturedModel staticModel=new TexturedModel(model,new ModelTexture(
				loader.loadTexture("tree")));
		TexturedModel grass=new TexturedModel(OBJLoader.loadbjModel("grassModel",loader),
				new ModelTexture(loader.loadTexture("grassTexture")));
		TexturedModel flower=new TexturedModel(OBJLoader.loadbjModel("grassModel",loader),
				new ModelTexture(loader.loadTexture("flower")));
		TexturedModel fern=new TexturedModel(OBJLoader.loadbjModel("fern",loader),
				new ModelTexture(loader.loadTexture("fern")));
		TexturedModel bobble=new TexturedModel(OBJLoader.loadbjModel("lowPolyTree",loader),
				new ModelTexture(loader.loadTexture("lowPolyTree")));
		
		grass.getTexture().setHasTransparency(true);
		grass.getTexture().setUseFakeLighting(true);
		flower.getTexture().setHasTransparency(true);
		flower.getTexture().setUseFakeLighting(true);
		fern.getTexture().setHasTransparency(true);
		
		List<Entity> entities=new ArrayList<Entity>();
		Random random=new Random(676452);
		for(int i=0;i<400;i++) {
			if(i%7==0) {
				entities.add(new Entity(grass,new Vector3f(random.nextFloat()*400-200,0,
						random.nextFloat()*-400),0,0,0,1.8f));
				entities.add(new Entity(flower,new Vector3f(random.nextFloat()*400-200,0,
						random.nextFloat()*-400),0,0,0,2.3f));
			}
			if(i%3==0) {
				entities.add(new Entity(fern,new Vector3f(random.nextFloat()*400-200,0,
						random.nextFloat()*-400),0,random.nextFloat()*360,0,0.9f));
				entities.add(new Entity(bobble,new Vector3f(random.nextFloat()*800-400,0,
						random.nextFloat()*-600),0,random.nextFloat()*360,0,random
						.nextFloat()*0.1f+0.6f));
				entities.add(new Entity(staticModel,new Vector3f(random.nextFloat()*800-400,
						0,random.nextFloat()*-600),0,0,0,random.nextFloat()*1+4));
			}
		}
		
		Light light=new Light(new Vector3f(20000,40000,20000),new Vector3f(1,1,1));
		
		Terrain terrain=new Terrain(0,-1,loader,texturePack,blendMap);
		Terrain terrain2=new Terrain(-1,-1,loader,texturePack,blendMap);
		
		Camera camera=new Camera();
		MasterRenderer renderer=new MasterRenderer();
		
		RawModel bunnyModel=OBJLoader.loadbjModel("person",loader);
		TexturedModel stanfordBunny=new TexturedModel(bunnyModel,new ModelTexture(loader.loadTexture("playerTexture")));
		
		Player player=new Player(stanfordBunny,new Vector3f(100,0,-50),0,0,0,1);
		
		while(!Display.isCloseRequested()) {
			camera.move();
			player.move();
			renderer.processEntity(player);
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
