/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.engine.graph;

import java.util.*;

import org.joml.Matrix4f;

import com.engine.scene.Entity;

/**
 *
 * @author ivan.nihtyanov
 */
public class Model {

    private final String id;
    // TODO: store them somwhere else, not in the model
    private List<Entity> entitiesList;
    private List<Material> materialList;
    private List<Animation> animationList;

    public Model(String id, List<Material> materialList, List<Animation> animationList) {
        this.id = id;
        entitiesList = new ArrayList<>();
        this.materialList = materialList;
        this.animationList = animationList;
    }

    public void cleanup() {
        materialList.forEach(Material::cleanup);
    }

    public List<Entity> getEntitiesList() {
        return entitiesList;
    }

    public String getId() {
        return id;
    }

    public List<Material> getMaterialList() {
        return materialList;
    }

    public List<Animation> getAnimationList() {
        return animationList;
    }

    public record AnimatedFrame(Matrix4f[] boneMatrices) {
    }

    public record Animation(String name, double duration, List<AnimatedFrame> frames) {
    }
}
