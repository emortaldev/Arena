package net.minestom.arena.utils;

import net.kyori.adventure.key.Key;
import net.minestom.server.MinecraftServer;
import net.minestom.server.registry.RegistryKey;
import net.minestom.server.world.DimensionType;

public class FullbrightDimension {
    public static final RegistryKey<DimensionType> INSTANCE = MinecraftServer.getDimensionTypeRegistry()
            .register(Key.key("minestom", "fullbright"),
                    DimensionType.builder()
                            .ambientLight(1.0f)
                            .build()
            );
}
