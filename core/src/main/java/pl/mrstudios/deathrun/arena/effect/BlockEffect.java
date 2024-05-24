package pl.mrstudios.deathrun.arena.effect;

import org.bukkit.Material;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public record BlockEffect(
        @NotNull Material blockType,
        @NotNull PotionEffectType effectType,
        @NotNull Integer amplifier,
        @NotNull Float duration
) {}
