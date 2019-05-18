/*
 * Copyright (c) 2016, 2017, 2018 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.fabricmc.fabric.api.renderer.v1.render;

import net.fabricmc.fabric.api.renderer.v1.model.DynamicModelBlockEntity;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ExtendedBlockView;

/**
 * BlockView-extending interface to be used by {@link FabricBakedModel} for dynamic model
 * customization. It ensures thread safety and exploits data cached in render 
 * chunks for performance and data consistency.<p>
 * 
 * There are differences from BlockView consumers must understand:<p>
 * 
 * BlockEntity implementations that provide data for model customization should implement 
 * {@link DynamicModelBlockEntity} which will be queried on the main thread when a render
 * chunk is enqueued for rebuild. The model should retrieve the results via {@link #getCachedRenderData()}.
 * While {@link #getBlockEntity(net.minecraft.util.math.BlockPos)} is not disabled, it
 * is not thread-safe for use on render threads.  Models that violate this guidance are
 * responsible for any necessary synchronization or collision detection.<p>
 * 
 * {@link #getBlockState(net.minecraft.util.math.BlockPos)} and {@link #getFluidState(net.minecraft.util.math.BlockPos)}
 * will always reflect the state cached with the render chunk.  Block and fluid states
 * can thus be different from main-thread world state due to lag between block update
 * application from network packets and render chunk rebuilds. Use of {@link #getCachedRenderData()}
 * will ensure consistency of model state with the rest of the chunk being rendered.<p>
 *
 * Models should avoid using {@link ExtendedBlockView#getBlockEntity(BlockPos)}
 * to ensure thread safety because this view may be accessed outside the main client thread.
 * Models that require Block Entity data should implement {@link DynamicModelBlockEntity}
 * and then use {@link #getCachedRenderData(BlockPos)} to retrieve it.  When called from the
 * main thread, that method will simply retrieve the data directly.<p>
 * 
 * This interface is only guaranteed to be present in the client environment.
 */
public interface TerrainBlockView extends ExtendedBlockView {
    /**
     * For models associated with Block Entities that implement {@link DynamicModelBlockEntity}
     * this will be the most recent value provided by that implementation for the given block position.<p>
     *
     * Null in all other cases, or if the result from the implementation was null.<p>
     *
     * @param pos Position of the block for the block model.
     */
    default Object getCachedRenderData(BlockPos pos) {
        BlockEntity be = this.getBlockEntity(pos);
        return be == null ? null : ((DynamicModelBlockEntity)be).getDynamicModelData();
    }
}
