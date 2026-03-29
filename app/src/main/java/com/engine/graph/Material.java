/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.engine.graph;

import java.util.*;

import org.joml.Vector4f;

/**
 *
 * @author ivan.nihtyanov
 */
public class Material {

    public static final Vector4f DEFAULT_COLOR = new Vector4f(0.0f, 0.0f, 0.0f, 1.0f);

    private Vector4f diffuseColor;
    private List<Mesh> meshList;
    private String texturePath;
    private Vector4f ambientColor;
    private float reflectance;
    private Vector4f specularColor;
    private String normalMapPath;

    public Material() {
        diffuseColor = DEFAULT_COLOR;
        ambientColor = DEFAULT_COLOR;
        specularColor = DEFAULT_COLOR;
        meshList = new ArrayList<>();
    }

    public void cleanup() {
        meshList.forEach(Mesh::cleanup);
    }

    public float getReflectance() {
        return reflectance;
    }

    public Vector4f getSpecularColor() {
        return specularColor;
    }

    public void setSpecularColor(Vector4f specularColor) {
        this.specularColor = specularColor;
    }

    public void setReflectance(float reflectance) {
        this.reflectance = reflectance;
    }

    public Vector4f getAmbientColor() {
        return ambientColor;
    }

    public void setAmbientColor(Vector4f ambientColor) {
        this.ambientColor = ambientColor;
    }

    public List<Mesh> getMeshList() {
        return meshList;
    }

    public String getTexturePath() {
        return texturePath;
    }

    public void setTexturePath(String texturePath) {
        this.texturePath = texturePath;
    }

    public Vector4f getDiffuseColor() {
        return diffuseColor;
    }

    public void setDiffuseColor(Vector4f diffuseColor) {
        this.diffuseColor = diffuseColor;
    }

    public String getNormalMapPath() {
        return normalMapPath;
    }

    public void setNormalMapPath(String normalMapPath) {
        this.normalMapPath = normalMapPath;
    }
}
