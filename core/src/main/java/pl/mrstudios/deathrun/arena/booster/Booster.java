package pl.mrstudios.deathrun.arena.booster;

import org.bukkit.Sound;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.mrstudios.deathrun.api.arena.booster.IBooster;
import pl.mrstudios.deathrun.api.arena.booster.IBoosterItem;
import pl.mrstudios.deathrun.api.arena.booster.enums.Direction;

public record Booster(
        @NotNull Integer slot,
        @NotNull Float power,
        @NotNull Integer delay,
        @NotNull IBoosterItem item,
        @NotNull IBoosterItem delayItem,
        @NotNull Direction direction,
        @Nullable Sound sound
) implements IBooster {}