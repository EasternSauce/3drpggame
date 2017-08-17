package com.mygame.game;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_X;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_Z;

import java.io.File;

import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;

import com.mygame.engine.IGameLogic;
import com.mygame.engine.MouseInput;
import com.mygame.engine.Scene;
import com.mygame.engine.SceneLight;
import com.mygame.engine.Window;
import com.mygame.engine.graph.Camera;
import com.mygame.engine.graph.Mesh;
import com.mygame.engine.graph.Renderer;
import com.mygame.engine.graph.anim.AnimGameItem;
import com.mygame.engine.graph.anim.Animation;
import com.mygame.engine.graph.lights.DirectionalLight;
import com.mygame.engine.items.GameItem;
import com.mygame.engine.items.SkyBox;
import com.mygame.engine.loaders.assimp.AnimMeshesLoader;
import com.mygame.engine.loaders.assimp.StaticMeshesLoader;

public class DummyGame implements IGameLogic {

    private static final float MOUSE_SENSITIVITY = 0.2f;

    private final Vector3f posInc;

    private final Renderer renderer;

    private final Camera camera;

    private Scene scene;

    private static final float CAMERA_POS_STEP = 0.40f;

    private float angleInc;

    private float lightAngle;

    private boolean firstTime;

    private boolean sceneChanged;

    private Animation animation;

    private AnimGameItem animItem;

    @SuppressWarnings("unused")
	private GameItem[] gameItems;
    
    private Vector2f lookAt = new Vector2f(25.0f, 190.0f);
    
    private float camDistance = 6.0f;

    private float previousScroll = 0.0f;
    
    public DummyGame() {
        renderer = new Renderer();
        camera = new Camera();
        posInc = new Vector3f(0.0f, 0.0f, 0.0f);
        angleInc = 0;
        lightAngle = 90;
        firstTime = true;
    }

    @Override
    public void init(Window window) throws Exception {
        renderer.init(window);

        scene = new Scene();

        String fileName = Thread.currentThread().getContextClassLoader()
                .getResource("models/terrain/terrain.obj").getFile();
        File file = new File(fileName);
        Mesh[] terrainMesh = StaticMeshesLoader.load(file.getAbsolutePath(), "/models/terrain");
        GameItem terrain = new GameItem(terrainMesh);
        terrain.setScale(100.0f);

        fileName = Thread.currentThread().getContextClassLoader()
                .getResource("models/bob/boblamp.md5mesh").getFile();
        file = new File(fileName);
        animItem = AnimMeshesLoader.loadAnimGameItem(file.getAbsolutePath(), "");
        animItem.setScale(0.05f);
        animation = animItem.getCurrentAnimation();
        
        fileName = Thread.currentThread().getContextClassLoader()
                .getResource("models/house/house.obj").getFile();
        file = new File(fileName);
        Mesh[] houseMesh = StaticMeshesLoader.load(file.getAbsolutePath(), "/models/house");
        GameItem house = new GameItem(houseMesh);
        house.setScale(0.5f);
        house.setPosition(10.0f, 0.0f, 10.0f);
        
        scene.setGameItems(new GameItem[]{animItem, terrain, house});

        // Shadows
        scene.setRenderShadows(true);

        // Fog
        @SuppressWarnings("unused")
		Vector3f fogColour = new Vector3f(0.5f, 0.5f, 0.5f);
        //scene.setFog(new Fog(true, fogColour, 0.02f));

        // Setup  SkyBox
        float skyBoxScale = 100.0f;
        fileName = Thread.currentThread().getContextClassLoader()
                .getResource("models/skybox/skybox.obj").getFile();
        file = new File(fileName);
        SkyBox skyBox = new SkyBox(file.getAbsolutePath(), "/models/skybox/skybox.png");
        skyBox.setScale(skyBoxScale);
        scene.setSkyBox(skyBox);

        // Setup Lights
        setupLights();

        camera.getPosition().x = -1.5f;
        camera.getPosition().y = 3.0f;
        camera.getPosition().z = 4.5f;
        camera.getRotation().x = 15.0f;
        camera.getRotation().y = 390.0f;
    }

    private void setupLights() {
        SceneLight sceneLight = new SceneLight();
        scene.setSceneLight(sceneLight);

        // Ambient Light
        sceneLight.setAmbientLight(new Vector3f(0.3f, 0.3f, 0.3f));
        sceneLight.setSkyBoxLight(new Vector3f(1.0f, 1.0f, 1.0f));

        // Directional Light
        float lightIntensity = 1.0f;
        Vector3f lightDirection = new Vector3f(0, 1, 1);
        DirectionalLight directionalLight = new DirectionalLight(new Vector3f(1, 1, 1), lightDirection, lightIntensity);
        sceneLight.setDirectionalLight(directionalLight);
    }

    @Override
    public void input(Window window, MouseInput mouseInput) {
        sceneChanged = false;
        boolean moved = false;
        posInc.set(0, 0, 0);
        if (window.isKeyPressed(GLFW_KEY_W)) {
            sceneChanged = true;
            posInc.z = -1;
            moved = true;
        } else if (window.isKeyPressed(GLFW_KEY_S)) {
            sceneChanged = true;
            posInc.z = 1;
            moved = true;
        }
        if (window.isKeyPressed(GLFW_KEY_A)) {
            sceneChanged = true;
            posInc.x = -1;
            moved = true;
        } else if (window.isKeyPressed(GLFW_KEY_D)) {
            sceneChanged = true;
            posInc.x = 1;
            moved = true;
        }
        if (window.isKeyPressed(GLFW_KEY_Z)) {
            sceneChanged = true;
            posInc.y = -1;
            moved = true;
        } else if (window.isKeyPressed(GLFW_KEY_X)) {
            sceneChanged = true;
            posInc.y = 1;
            moved = true;
        }
        if (window.isKeyPressed(GLFW_KEY_LEFT)) {
            sceneChanged = true;
            angleInc -= 0.05f;
        } else if (window.isKeyPressed(GLFW_KEY_RIGHT)) {
            sceneChanged = true;
            angleInc += 0.05f;
        } else {
            angleInc = 0;            
        }
        if (window.isKeyPressed(GLFW_KEY_SPACE)) {
            sceneChanged = true;
            animItem.jump();
            //animation.nextFrame();
        }
        if(moved) {
        	animation.nextFrame();
        }
    }

    @Override
    public void update(float interval, MouseInput mouseInput, Window window) {
    	sceneChanged = true;
        if (mouseInput.isRightButtonPressed()) {
            // Update camera based on mouse            
            Vector2f rotVec = mouseInput.getDisplVec();
            //camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
            //sceneChanged = true;
            lookAt.x +=rotVec.x * MOUSE_SENSITIVITY;
            lookAt.y +=rotVec.y * MOUSE_SENSITIVITY;
        }

        // Update camera position
        //System.out.println((float)Math.cos(Math.toRadians(lookAt.y)));
        if(animItem.getPosition().y + animItem.getVerticalVelocity() < 0.0f) {
        	animItem.setPosition(animItem.getPosition().x, 0.0f, animItem.getPosition().z);
        	animItem.setVerticalVelocity(0.0f);
        }
        
        if(animItem.getVerticalVelocity() != 0.0f) {
        	animItem.movePosition(0.0f, animItem.getVerticalVelocity(), 0.0f);
            animItem.setVerticalVelocity(animItem.getVerticalVelocity() + animItem.getVerticalAccel());
        }

        animItem.movePosition(posInc.z * CAMERA_POS_STEP * (float)Math.cos(Math.toRadians(lookAt.y)) + posInc.x * CAMERA_POS_STEP * (float)Math.cos(Math.toRadians(-lookAt.y + 90.0f)), 
        		posInc.y * CAMERA_POS_STEP,
        		posInc.z * CAMERA_POS_STEP * (float)Math.sin(Math.toRadians(lookAt.y)) + posInc.x * CAMERA_POS_STEP * (float)Math.sin(Math.toRadians(lookAt.y - 90.0f)));
        //camera.setPosition(animItem.getPosition().x, animItem.getPosition().y, animItem.getPosition().z);
        
        camera.setPosition(animItem.getPosition().x + camDistance * (float)Math.cos(Math.toRadians(lookAt.y)) * (float)Math.cos(Math.toRadians(lookAt.x)), 1.5f + animItem.getPosition().y + camDistance * (float)Math.sin(Math.toRadians(lookAt.x)), animItem.getPosition().z + camDistance * (float)Math.sin(Math.toRadians(lookAt.y)) * (float)Math.cos(Math.toRadians(lookAt.x)));
        camera.setRotation(lookAt.x, 90.0f + lookAt.y + 180.0f, 0.0f);
        animItem.setRotation(new Quaternionf().rotateY( (float)Math.toRadians(-90.0f - lookAt.y)));
        
        float scroll = mouseInput.getScroll();
        float deltaScroll = previousScroll - scroll;
        previousScroll = scroll;
        
        camDistance += deltaScroll * 0.04f;

        lightAngle += angleInc;
        if (lightAngle < 0) {
            lightAngle = 0;
        } else if (lightAngle > 180) {
            lightAngle = 180;
        }
        float zValue = (float) Math.cos(Math.toRadians(lightAngle));
        float yValue = (float) Math.sin(Math.toRadians(lightAngle));
        Vector3f lightDirection = this.scene.getSceneLight().getDirectionalLight().getDirection();
        lightDirection.x = 0;
        lightDirection.y = yValue;
        lightDirection.z = zValue;
        lightDirection.normalize();

        // Update view matrix
        camera.updateViewMatrix();
    }

    @Override
    public void render(Window window) {
        if (firstTime) {
            sceneChanged = true;
            firstTime = false;
        }
        renderer.render(window, camera, scene, sceneChanged);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();

        scene.cleanup();
    }
}
