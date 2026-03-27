package com.engine.graph;

import org.lwjgl.opengl.GL;

import com.engine.Window;
import com.engine.scene.Scene;

import static org.lwjgl.opengl.GL11.*;

public class Render {

    private SceneRender sceneRender;
    private SkyBoxRender skyBoxRender;
    private GuiRender guiRender;

    public Render(Window window) {
        GL.createCapabilities();
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
        sceneRender = new SceneRender();
        guiRender = new GuiRender(window);
        skyBoxRender = new SkyBoxRender();
    }

    public void cleanup() {
        sceneRender.cleanup();
        guiRender.cleanup();
    }

    public void render(Window window, Scene scene) {
        GL.createCapabilities();
        glEnable(GL_DEPTH_TEST);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glViewport(0, 0, window.getWidth(), window.getHeight());

        skyBoxRender.render(scene);
        sceneRender.render(scene);
        guiRender.render(scene);
    }

    public void resize(int width, int height) {
        guiRender.resize(width, height);
    }
}