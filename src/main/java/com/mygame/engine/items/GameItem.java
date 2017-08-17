package com.mygame.engine.items;

import org.joml.Quaternionf;
import org.joml.Vector3f;

import com.mygame.engine.graph.Mesh;

public class GameItem {

    private boolean selected;

    private Mesh[] meshes;

    private final Vector3f position;

    private float scale;

    private final Quaternionf rotation;

    private int textPos;
    
    private boolean disableFrustumCulling;

    private boolean insideFrustum;

	private float verticalVelocity;
    private float verticalAccel;

    public GameItem() {
        selected = false;
        position = new Vector3f(0, 0, 0);
        scale = 1;
        rotation = new Quaternionf();
        textPos = 0;
        insideFrustum = true;
        disableFrustumCulling = false;
        verticalVelocity = 0.0f;
        verticalAccel = -0.07f;
    }

    public GameItem(Mesh mesh) {
        this();
        this.meshes = new Mesh[]{mesh};
    }

    public GameItem(Mesh[] meshes) {
        this();
        this.meshes = meshes;
    }

    public Vector3f getPosition() {
        return position;
    }

    public int getTextPos() {
        return textPos;
    }

    public boolean isSelected() {
        return selected;
    }

    public final void setPosition(float x, float y, float z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
    }
    
    public void movePosition(float offsetX, float offsetY, float offsetZ) {
    	//Vector3f euler = rotation.getEulerAnglesXYZ(new Vector3f());
    	//euler.y += Math.PI / 2;
    	//System.out.println(euler.y);
//        if ( offsetZ != 0 ) {
//            position.x += (float)Math.sin(angle) * -1.0f * offsetZ;
//            position.z += (float)Math.cos(angle) * offsetZ;
//        }
//        if ( offsetX != 0) {
//            position.x += (float)Math.sin(angle - 90) * -1.0f * offsetX;
//            position.z += (float)Math.cos(angle - 90) * offsetX;
//        }
//        position.y += offsetY;
        
        this.position.x += offsetX;
        this.position.y += offsetY;
        this.position.z += offsetZ;
    }

    public float getScale() {
        return scale;
    }

    public final void setScale(float scale) {
        this.scale = scale;
    }

    public Quaternionf getRotation() {
        return rotation;
    }

    public final void setRotation(Quaternionf q) {
        this.rotation.set(q);
    }

    public Mesh getMesh() {
        return meshes[0];
    }

    public Mesh[] getMeshes() {
        return meshes;
    }

    public void setMeshes(Mesh[] meshes) {
        this.meshes = meshes;
    }

    public void setMesh(Mesh mesh) {
        this.meshes = new Mesh[]{mesh};
    }

    public void cleanup() {
        int numMeshes = this.meshes != null ? this.meshes.length : 0;
        for (int i = 0; i < numMeshes; i++) {
            this.meshes[i].cleanUp();
        }
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setTextPos(int textPos) {
        this.textPos = textPos;
    }

    public boolean isInsideFrustum() {
        return insideFrustum;
    }

    public void setInsideFrustum(boolean insideFrustum) {
        this.insideFrustum = insideFrustum;
    }
    
    public boolean isDisableFrustumCulling() {
        return disableFrustumCulling;
    }

    public void setDisableFrustumCulling(boolean disableFrustumCulling) {
        this.disableFrustumCulling = disableFrustumCulling;
    }

	public float getVerticalVelocity() {
		return verticalVelocity;
	}

	public void setVerticalVelocity(float verticalVelocity) {
		this.verticalVelocity = verticalVelocity;
	}

	public float getVerticalAccel() {
		return verticalAccel;
	}

	public void setVerticalAccel(float verticalAccel) {
		this.verticalAccel = verticalAccel;
	}
	
	public void jump() {
		if(position.y <= 0.0f) {
			this.verticalVelocity = 1.3f;

		}
	}
    
    
}
