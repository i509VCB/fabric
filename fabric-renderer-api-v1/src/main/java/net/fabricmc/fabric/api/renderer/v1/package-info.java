/*
 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
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

/**
 * Fabric Renderer API, version 1.
 *
 * <p>Fabric's Renderer API is designed to facilitate enhanced rendering, buffering and model lighting capabilities.
 * Unlike most modules in Fabric API, the renderer API is designed to allow other developers to implement their own
 * performance and visual enhancements while providing a common interface between all implementations for mod compatibility.
 *
 * <p><b>For Mod developers</b>
 * <p>To use the renderer API, you would start by getting the current {@link net.fabricmc.fabric.api.renderer.v1.Renderer Renderer} implement through {@link net.fabricmc.fabric.api.renderer.v1.RendererAccess RendererAccess}.
 * Note there may be no renderer implementation in specific cases, this can be efficiently check by using {@link net.fabricmc.fabric.api.renderer.v1.RendererAccess#hasRenderer()}.
 * Typically you will be dealing with the default renderer implementation from Indigo which is included in a typical Fabric
 * API install, however a different renderer may be in use.
 *
 * <p>The renderer api provides extended facilities for creating models.
 * The heart of the extensions provided by the renderer api for creating models is done by implementing {@link net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel FabricBakedModel}.
 * FabricBakedModel specifically provides methods to allow a mod developer to emit a model in the context of rendering a block or item.
 * More information on how to implement the extensions, see the documentation for {@link net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel FabricBakedModel}.
 *
 * <p>You are encouraged to use a {@link net.fabricmc.fabric.api.renderer.v1.mesh.Mesh Mesh} where possible to increase performance and reduce memory overhead.
 * A mesh may be created using {@link net.fabricmc.fabric.api.renderer.v1.Renderer#meshBuilder()}.
 *
 * <p><b>For Renderer Implementors</b>
 * <p>Fabric's Renderer API is designed to expose as little implementation detail on the format for meshes and emitting
 * quads which leaves an implementation to be free to represent meshes and quads in the implementation's desired format.
 * Implementations are free to provide any desired extensions to their own renderer.
 *
 * @see net.fabricmc.fabric.api.renderer.v1.Renderer
 * @see net.fabricmc.fabric.api.renderer.v1.RendererAccess
 * @see net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel FabricBakedModel
 * @see net.fabricmc.fabric.api.renderer.v1.mesh.Mesh Mesh
 */

package net.fabricmc.fabric.api.renderer.v1;
