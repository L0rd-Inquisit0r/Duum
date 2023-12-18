package engineTester;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import guis.GuiRenderer;
import guis.GuiTexture;
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
				
		TerrainTexture backgroundTexture=new TerrainTexture(loader.loadTexture("DOOM/FLOOR4_8"));
		TerrainTexture rTexture=new TerrainTexture(loader.loadTexture("DOOM/FLOOR5_2"));
		TerrainTexture gTexture=new TerrainTexture(loader.loadTexture("DOOM/FLOOR6_2"));
		TerrainTexture bTexture=new TerrainTexture(loader.loadTexture("DOOM/FLOOR5_1"));
		
		TerrainTexturePack texturePack=new TerrainTexturePack(backgroundTexture,rTexture,gTexture,bTexture);
		TerrainTexture blendMap=new TerrainTexture(loader.loadTexture("arena"));
		
		Light light=new Light(new Vector3f(20000,40000,20000),new Vector3f(1,1,1));
		
		Terrain terrain=new Terrain(0,-1,loader,texturePack,blendMap,"heightMap");
		
		MasterRenderer renderer=new MasterRenderer();
		
		RawModel model=OBJLoader.loadbjModel("person",loader);
		TexturedModel playerModel=new TexturedModel(model,new ModelTexture(loader.loadTexture("playerTexture")));
		
		Entity enemy=new Entity(playerModel,new Vector3f(100,0,-100),0,0,0,1);
		
		Player player=new Player(playerModel,new Vector3f(100,0,-50),0,180,0,1);
		Camera camera=new Camera(player);
		
		List<GuiTexture> guis=new ArrayList<GuiTexture>();
		GuiTexture gui=new GuiTexture(loader.loadTexture("misery"),new Vector2f(0,-0.90f),new Vector2f(0.15f,0.15f));
		GuiTexture gui2=new GuiTexture(loader.loadTexture("guiBar"),new Vector2f(0,-0.90f),new Vector2f(1.05f,1.05f));
		guis.add(gui2);
		guis.add(gui);
		
		GuiRenderer guiRenderer=new GuiRenderer(loader);
		
		while(!Display.isCloseRequested()&&!Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			player.move(terrain);
			camera.move();
			renderer.processEntity(player);
			renderer.processEntity(enemy);
			renderer.processTerrain(terrain);
			renderer.render(light, camera);
			guiRenderer.render(guis);
			DisplayManager.updateDisplay();
		}
		guiRenderer.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
}
