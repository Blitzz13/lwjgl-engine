/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.game;

import org.joml.*;

import com.engine.*;
import com.engine.graph.*;
import com.engine.scene.*;
import com.engine.scene.lights.PointLight;
import com.engine.scene.lights.SceneLights;
import com.engine.scene.lights.SpotLight;

import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiCond;

import java.lang.Math;

import static org.lwjgl.glfw.GLFW.*;

/**
 *
 * @author ivan.nihtyanov
 */
public class Main implements IAppLogic, IGuiInstance {
    private static final float MOUSE_SENSITIVITY = 0.1f;
    private static final float MOVEMENT_SPEED = 0.005f;
    private Entity soldierEntity;
    private float rotation;
    private LightControls lightControls;
    private static final int NUM_CHUNKS = 4;
    private Entity[][] terrainEntities;

    public static void main(String[] args) {
        // 1. Create the options
        Window.WindowOptions opts = new Window.WindowOptions();

        // 2. SET YOUR CUSTOM SIZE HERE
        opts.width = 1280;
        opts.height = 720;
        opts.fps = 0; // Set to 0 to disable FPS limit

        Main main = new Main();
        Engine gameEng = new Engine("Renderer", opts, main);
        gameEng.start();
    }

    @Override
    public void cleanup() {
        // Nothing to be done yet
    }

    @Override
    public void init(Window window, Scene scene, Render render) {
        String quadModelId = "quad-model";
        Model quadModel = ModelLoader.loadModel(quadModelId, "src/main/resources/models/quad/quad.obj",
                scene.getTextureCache());
        scene.addModel(quadModel);

        int numRows = NUM_CHUNKS * 2 + 1;
        int numCols = numRows;

        terrainEntities = new Entity[numRows][numCols];
        for (int j = 0; j < numRows; j++) {
            for (int i = 0; i < numCols; i++) {
                Entity entity = new Entity("TERRAIN_" + j + "_" + i, quadModelId);
                terrainEntities[j][i] = entity;
                scene.addEntity(entity);
            }
        }

        Model soldierModel = ModelLoader.loadModel("soldier-model", "src/main/resources/models/soldier/cod_soldier.obj",
                scene.getTextureCache());
        scene.addModel(soldierModel);

        soldierEntity = new Entity("soldier-entity", soldierModel.getId());
        soldierEntity.setPosition(-0.5f, -5, -5);
        scene.addEntity(soldierEntity);

        SceneLights sceneLights = new SceneLights();
        sceneLights.getAmbientLight().setIntensity(0.3f);
        scene.setSceneLights(sceneLights);
        sceneLights.getPointLights().add(new PointLight(new Vector3f(1, 1, 1),
                new Vector3f(0, 0, -1.4f), 1.0f));

        SkyBox skyBox = new SkyBox("src/main/resources/models/skybox/skybox.obj", scene.getTextureCache());
        skyBox.getSkyBoxEntity().setScale(50);
        skyBox.getSkyBoxEntity().updateModelMatrix();
        scene.setSkyBox(skyBox);

        Vector3f coneDir = new Vector3f(0, 0, -1);
        sceneLights.getSpotLights().add(new SpotLight(new PointLight(new Vector3f(1, 1, 1),
                new Vector3f(0, 0, -1.4f), 0.0f), coneDir, 140.0f));

        lightControls = new LightControls(scene);
        scene.setGuiInstance(lightControls);

        scene.getCamera().moveUp(0.1f);

        updateTerrain(scene);
    }

    @Override
    public void input(Window window, Scene scene, long diffTimeMillis, boolean inputConsumed) {
        if (inputConsumed) {
            return;
        }

        ImGuiIO io = ImGui.getIO();

        Camera camera = scene.getCamera();
        if (!io.getWantCaptureKeyboard()) {
            float move = diffTimeMillis * MOVEMENT_SPEED;
            if (window.isKeyPressed(GLFW_KEY_W)) {
                camera.moveForward(move);
            } else if (window.isKeyPressed(GLFW_KEY_S)) {
                camera.moveBackwards(move);
            }

            if (window.isKeyPressed(GLFW_KEY_A)) {
                camera.moveLeft(move);
            } else if (window.isKeyPressed(GLFW_KEY_D)) {
                camera.moveRight(move);
            }

            if (window.isKeyPressed(GLFW_KEY_E)) {
                camera.moveUp(move);
            } else if (window.isKeyPressed(GLFW_KEY_Q)) {
                camera.moveDown(move);
            }

            if (window.isKeyPressed(GLFW_KEY_LEFT)) {
                camera.addRotation(0, (float) Math.toRadians(0.8));
            } else if (window.isKeyPressed(GLFW_KEY_RIGHT)) {
                camera.addRotation(0, (float) Math.toRadians(-0.8));
            }

            if (window.isKeyPressed(GLFW_KEY_UP)) {
                camera.addRotation((float) Math.toRadians(0.8), 0);
            } else if (window.isKeyPressed(GLFW_KEY_DOWN)) {
                camera.addRotation((float) Math.toRadians(-0.8), 0);
            }
        }

        if (!io.getWantCaptureMouse()) {
            MouseInput mouseInput = window.getMouseInput();
            if (mouseInput.isLeftButtonPressed()) {
                Vector2f displVec = mouseInput.getDisplVec();
                camera.addRotation((float) Math.toRadians(-displVec.x * MOUSE_SENSITIVITY),
                        (float) Math.toRadians(-displVec.y * MOUSE_SENSITIVITY));
                // System.out.println(
                // "Camera rotation: " + -displVec.x * MOUSE_SENSITIVITY + ", " + -displVec.y *
                // MOUSE_SENSITIVITY);
            }
        }
    }

    @Override
    public void update(Window window, Scene scene, long diffTimeMillis) {
        rotation += 1.5;
        if (rotation > 360) {
            rotation = 0;
        }

        // cubeEntity.setRotation(0, 1, 0, (float) Math.toRadians(rotation));
        soldierEntity.updateModelMatrix();
        updateTerrain(scene);
    }

    public void updateTerrain(Scene scene) {
        int cellSize = 10;
        Camera camera = scene.getCamera();
        Vector3f cameraPos = camera.getPosition();
        int cellCol = (int) (cameraPos.x / cellSize);
        int cellRow = (int) (cameraPos.z / cellSize);

        int numRows = NUM_CHUNKS * 2 + 1;
        int numCols = numRows;
        int zOffset = -NUM_CHUNKS;
        float scale = cellSize / 2.0f;
        for (int j = 0; j < numRows; j++) {
            int xOffset = -NUM_CHUNKS;
            for (int i = 0; i < numCols; i++) {
                Entity entity = terrainEntities[j][i];
                entity.setScale(scale);
                entity.setPosition((cellCol + xOffset) * 2.0f, -0.62f, (cellRow + zOffset) * 2.0f);
                entity.getModelMatrix().identity().scale(scale).translate(entity.getPosition());
                xOffset++;
            }
            zOffset++;
        }
    }

    @Override
    public void drawGui() {
        ImGui.newFrame();
        ImGui.setNextWindowPos(0, 0, ImGuiCond.Always);
        ImGui.showDemoWindow();
        ImGui.endFrame();
        ImGui.render();
    }

    @Override
    public boolean handleGuiInput(Scene scene, Window window) {
        ImGuiIO imGuiIO = ImGui.getIO();
        MouseInput mouseInput = window.getMouseInput();
        Vector2f mousePos = mouseInput.getCurrentPos();

        // 1. Get the Mac Retina Scale
        float[] scaleX = new float[1];
        float[] scaleY = new float[1];
        glfwGetWindowContentScale(window.getWindowHandle(), scaleX, scaleY);

        // 2. Multiply the mouse position by the scale to match the framebuffer
        float actualMouseX = mousePos.x * scaleX[0];
        float actualMouseY = mousePos.y * scaleY[0];

        // 3. Pass the SCALED coordinates to ImGui
        imGuiIO.addMousePosEvent(actualMouseX, actualMouseY);

        imGuiIO.addMouseButtonEvent(0, mouseInput.isLeftButtonPressed());
        imGuiIO.addMouseButtonEvent(1, mouseInput.isRightButtonPressed());

        return imGuiIO.getWantCaptureMouse() || imGuiIO.getWantCaptureKeyboard();
    }
}