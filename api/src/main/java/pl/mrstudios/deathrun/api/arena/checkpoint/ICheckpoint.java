package pl.mrstudios.deathrun.api.arena.checkpoint;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ICheckpoint {

    @NotNull Integer id();
    @NotNull Location spawn();
    @NotNull List<Location> locations();

}
