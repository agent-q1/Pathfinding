/*
 * Copyright 2014 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.pathfinding;

import org.terasology.registry.CoreRegistry;
import org.terasology.world.block.Block;
import org.terasology.world.block.BlockManager;
import org.terasology.world.chunks.ChunkConstants;
import org.terasology.world.chunks.CoreChunk;
import org.terasology.world.generator.ChunkGenerationPass;

import java.util.Map;

/**
 * Generates chunk with flat areas, marks for chunk borders and stairs, connecting multiple floors.
 *
 * @author synopia
 */
public class PathfinderTestGenerator implements ChunkGenerationPass {
    public Block air;
    public Block ground;
    public boolean generateStairs;
    public boolean openStairs;

    public PathfinderTestGenerator() {
        this(true);
    }

    public PathfinderTestGenerator(boolean generateStairs) {
        this(generateStairs, false);
    }

    public PathfinderTestGenerator(boolean generateStairs, boolean openStairs) {
        this.generateStairs = generateStairs;
        this.openStairs = openStairs;
    }

    @Override
    public void generateChunk(CoreChunk chunk) {
        BlockManager blockManager = CoreRegistry.get(BlockManager.class);
        air = blockManager.getBlock(BlockManager.AIR_ID);
        ground = blockManager.getBlock("CoreBlocks:Dirt");

        generateLevel(chunk, 50);

        generateLevel(chunk, 45);
        if (generateStairs) {
            generateStairs(chunk, 45);
        }
    }

    private void generateLevel(CoreChunk chunk, int groundHeight) {
        for (int x = 0; x < ChunkConstants.SIZE_X; x++) {
            for (int z = 0; z < ChunkConstants.SIZE_Z; z++) {
                chunk.setBlock(x, groundHeight, z, ground);

                for (int y = groundHeight + 1; y < groundHeight + 4; y++) {
                    chunk.setBlock(x, y, z, air);
                }
            }
        }
        for (int x = 0; x < ChunkConstants.SIZE_X; x++) {
            chunk.setBlock(x, groundHeight + 1, 0, ground);
            chunk.setBlock(x, groundHeight + 1, ChunkConstants.SIZE_Z - 1, ground);
        }
        for (int z = 0; z < ChunkConstants.SIZE_Z; z++) {
            chunk.setBlock(0, groundHeight + 1, z, ground);
            chunk.setBlock(ChunkConstants.SIZE_X - 1, groundHeight + 1, z, ground);
        }
    }

    private void generateStairs(CoreChunk chunk, int groundHeight) {
        for (int height = groundHeight + 1; height < groundHeight + 4; height++) {
            for (int x = 0; x < ChunkConstants.SIZE_X; x++) {
                chunk.setBlock(x, height, 0, ground);
                chunk.setBlock(x, height, ChunkConstants.SIZE_Z - 1, ground);
            }
            for (int z = 0; z < ChunkConstants.SIZE_Z; z++) {
                chunk.setBlock(0, height, z, ground);
                chunk.setBlock(ChunkConstants.SIZE_X - 1, height, z, ground);
            }
        }
        int height = groundHeight + 1;
        chunk.setBlock(7, height, 0, air);
        chunk.setBlock(8, height, 0, air);
        chunk.setBlock(7, height, ChunkConstants.SIZE_Z - 1, air);
        chunk.setBlock(8, height, ChunkConstants.SIZE_Z - 1, air);
        chunk.setBlock(0, height, 7, air);
        chunk.setBlock(0, height, 8, air);
        chunk.setBlock(ChunkConstants.SIZE_X - 1, height, 7, air);
        chunk.setBlock(ChunkConstants.SIZE_X - 1, height, 8, air);

        buildWalkable(chunk, 6, height, 7);
        buildWalkable(chunk, 6, height, 8);
        buildWalkable(chunk, 7, height + 1, 7);
        buildWalkable(chunk, 7, height + 1, 8);
        buildWalkable(chunk, 8, height + 2, 7);
        buildWalkable(chunk, 8, height + 2, 8);
        buildWalkable(chunk, 9, height + 3, 7);
        buildWalkable(chunk, 9, height + 3, 8);
    }

    private void buildWalkable(CoreChunk chunk, int x, int y, int z) {
        chunk.setBlock(x, y, z, ground);
        chunk.setBlock(x, y + 1, z, air);
        chunk.setBlock(x, y + 2, z, air);
        if (openStairs) {
            chunk.setBlock(x, y + 3, z, air);
        }
    }

    @Override
    public void setWorldSeed(String seed) {

    }

    @Override
    public Map<String, String> getInitParameters() {
        return null;
    }

    @Override
    public void setInitParameters(Map<String, String> initParameters) {
    }
}
