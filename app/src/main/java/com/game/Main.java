/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.game;

import org.joml.*;

import com.engine.*;
import com.engine.graph.*;
import com.engine.scene.*;

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
    private Entity cubeEntity;
    private float rotation;

    public static void main(String[] args) {
        // 1. Create the options
        Window.WindowOptions opts = new Window.WindowOptions();

        // 2. SET YOUR CUSTOM SIZE HERE
        opts.width = 1280;
        opts.height = 720;
        opts.fps = 0; // Set to 0 to disable FPS limit

        Main main = new Main();
        Engine gameEng = new Engine("chapter-09", opts, main);
        gameEng.start();
    }

    @Override
    public void cleanup() {
        // Nothing to be done yet
    }

    @Override
    public void init(Window window, Scene scene, Render render) {
        Model soldierModel = ModelLoader.loadModel("soldier-model", "src/main/resources/models/soldier/cod_soldier.obj",
                scene.getTextureCache());
        scene.addModel(soldierModel);

        cubeEntity = new Entity("soldier-entity", soldierModel.getId());
        cubeEntity.setPosition(-0.5f, -5, -5);
        scene.addEntity(cubeEntity);
        scene.setGuiInstance(this);
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
                System.out.println("key W pressed");
                System.out.println("move: " + move);
            } else if (window.isKeyPressed(GLFW_KEY_S)) {
                camera.moveBackwards(move);
                System.out.println("key S pressed");
                System.out.println("move: " + move);
            }
            if (window.isKeyPressed(GLFW_KEY_A)) {
                camera.moveLeft(move);
                System.out.println("key A pressed");
                System.out.println("move: " + move);
            } else if (window.isKeyPressed(GLFW_KEY_D)) {
                camera.moveRight(move);
                System.out.println("key D pressed");
                System.out.println("move: " + move);
            }
            if (window.isKeyPressed(GLFW_KEY_UP)) {
                camera.moveUp(move);
                System.out.println("key UP pressed");
                System.out.println("move: " + move);
            } else if (window.isKeyPressed(GLFW_KEY_DOWN)) {
                camera.moveDown(move);
                System.out.println("key DOWN pressed");
                System.out.println("move: " + move);
            }
        }

        if (!io.getWantCaptureMouse()) {
            MouseInput mouseInput = window.getMouseInput();
            if (mouseInput.isLeftButtonPressed()) {
                Vector2f displVec = mouseInput.getDisplVec();
                camera.addRotation((float) Math.toRadians(-displVec.x * MOUSE_SENSITIVITY),
                        (float) Math.toRadians(-displVec.y * MOUSE_SENSITIVITY));
                System.out.println(
                        "Camera rotation: " + -displVec.x * MOUSE_SENSITIVITY + ", " + -displVec.y * MOUSE_SENSITIVITY);
            }
        }
    }

    @Override
    public void update(Window window, Scene scene, long diffTimeMillis) {
        rotation += 1.5;
        if (rotation > 360) {
            rotation = 0;
        }

        cubeEntity.setRotation(0, 1, 0, (float) Math.toRadians(rotation));
        cubeEntity.updateModelMatrix();
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