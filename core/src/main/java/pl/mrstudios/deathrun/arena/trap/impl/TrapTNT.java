package pl.mrstudios.deathrun.arena.trap.impl;

import org.bukkit.Location;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.mrstudios.deathrun.arena.trap.Trap;

import java.time.Duration;
import java.util.List;

import static java.time.Duration.ZERO;
import static org.bukkit.Material.TNT;

public class TrapTNT extends Trap {

    @Override
    public void start() {
        super.locations.forEach(
                (location) -> location.getWorld().spawn(location, TNTPrimed.class, (entity) -> {
                    entity.setFuseTicks(5);
                    entity.setVelocity(new Vector(0, 0.125, 0));
                }));
    }

    @Override
    public void end() {}

    @Override
    public void setExtra(
            @Nullable Object... objects
    ) {}

    @Override
    public @NotNull List<Location> filter(
            @NotNull List<Location> list,
            @Nullable Object... objects
    ) {
        return list.stream()
                .filter((location) -> location.getBlock().getType() == TNT)
                .toList();
    }

    @Override
    public @NotNull Duration getDuration() {
        return ZERO;
    }

}
