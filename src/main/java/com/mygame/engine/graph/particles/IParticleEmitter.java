package com.mygame.engine.graph.particles;

import java.util.List;

import com.mygame.engine.items.GameItem;

public interface IParticleEmitter {

    void cleanup();
    
    Particle getBaseParticle();
    
    List<GameItem> getParticles();
}
