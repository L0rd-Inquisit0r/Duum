package engineTester;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Enemy;
import entities.Light;
import entities.Player;
import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontRendering.TextMaster;
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
import toolbox.MousePicker;

public class MainGameLoop {

	public static void main(String[] args) {
		DisplayManager.createDisplay();
		Loader loader=new Loader();
		TextMaster.init(loader);
				
		TerrainTexture backgroundTexture=new TerrainTexture(loader.loadTexture("FLOOR4_8"));
		TerrainTexture rTexture=new TerrainTexture(loader.loadTexture("FLOOR5_2"));
		TerrainTexture gTexture=new TerrainTexture(loader.loadTexture("FLOOR6_2"));
		TerrainTexture bTexture=new TerrainTexture(loader.loadTexture("FLOOR5_1"));
		
		TerrainTexturePack texturePack=new TerrainTexturePack(backgroundTexture,rTexture,gTexture,bTexture);
		TerrainTexture blendMap=new TerrainTexture(loader.loadTexture("arena"));
		
		Light light=new Light(new Vector3f(20000,40000,20000),new Vector3f(1,1,1));
		
		Terrain arena=new Terrain(0,-1,loader,texturePack,blendMap,"heightMap");
		
		MasterRenderer renderer=new MasterRenderer(loader);
		
		RawModel sprite=OBJLoader.loadbjModel("doomguy",loader);
		TexturedModel model=new TexturedModel(sprite,new ModelTexture(loader.loadTexture("doomguy")));
		
		model.getTexture().setHasTransparency(true);
		model.getTexture().setUseFakeLighting(true);
		
		Player player=new Player(model,new Vector3f(100,0,-50),0,180,0,1);
		Enemy enemy=new Enemy(model,new Vector3f(200,0,-200),0,0,0,1.25f);
		Camera camera=new Camera(player);
		
		List<GuiTexture> guis=new ArrayList<GuiTexture>();
		GuiTexture gun=new GuiTexture(loader.loadTexture("shotgun"),new Vector2f(0,-0.55f),new Vector2f(0.25f,0.25f));
		GuiTexture gato=new GuiTexture(loader.loadTexture("misery"),new Vector2f(0,-0.90f),new Vector2f(0.15f,0.15f));
		GuiTexture statBar=new GuiTexture(loader.loadTexture("guiBar"),new Vector2f(0,-0.90f),new Vector2f(1.05f,1.05f));
		guis.add(statBar);
		guis.add(gun);
		guis.add(gato);
		
		GuiRenderer guiRenderer=new GuiRenderer(loader);
		
		MousePicker picker=new MousePicker(camera,renderer.getProjectionMatrix(),arena);
		
		FontType font=new FontType(loader.loadTexture("verdana"),new File("res/verdana.fnt"));
		GUIText text=new GUIText("HITS: "+player.getShotsCount(),3,font,new Vector2f(0.40f,0.90f),1,true);
		text.setColour(1,0,0);
		
		while(!Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			player.move(arena);
			enemy.move(arena,player);
			camera.move();
			picker.update();
			player.shoot(enemy,picker);
			
			renderer.processEntity(player);
			renderer.processEntity(enemy);
			renderer.processTerrain(arena);
			renderer.render(light,camera);
			
			guiRenderer.render(guis);
			TextMaster.render();
			TextMaster.removeText(text);
			text=new GUIText("HITS: "+player.getShotsCount(),3,font,new Vector2f(0.40f,0.90f),1,true);
			text.setColour(1,0,0);
			DisplayManager.updateDisplay();
		}
		
		TextMaster.cleanUp();
		guiRenderer.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
}
