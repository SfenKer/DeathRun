package pl.mrstudios.deathrun.arena.trap;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import pl.mrstudios.deathrun.api.arena.trap.ITrap;

import java.util.List;

public abstract class Trap implements ITrap {

    public Location button;
    public List<Location> locations;

    @Override
    public @NotNull Location getButton() {
        return this.button;
    }

    @Override
    public void setButton(
            @NotNull Location location
    ) {
        this.button = location;
    }

    @Override
    public @NotNull List<Location> getLocations() {
        return this.locations;
    }

    @Override
    public void setLocations(
            @NotNull List<Location> locations
    ) {
        this.locations = locations;
    }

}
