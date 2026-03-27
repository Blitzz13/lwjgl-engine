/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.engine.scene.lights;

import org.joml.Vector3f;

/**
 *
 * @author ivan.nihtyanov
 */
public class AmbientLight {
    private Vector3f color;
    private float intensity;

    public AmbientLight(float intensity, Vector3f color) {
        this.color = color;
        this.intensity = intensity;
    }

    public AmbientLight() {
        this(1.0f, new Vector3f(1.0f, 1.0f, 1.0f));
    }

    public Vector3f getColor() {
        return color;
    }

    public void setColor(float r, float g, float b) {
        this.color.set(r, g, b);
    }

    public float getIntensity() {
        return intensity;
    }

    public void setIntensity(float intensity) {
        this.intensity = intensity;
    }
}
