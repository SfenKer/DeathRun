package pl.mrstudios.deathrun.arena.trap.impl;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.mrstudios.deathrun.api.arena.trap.annotations.Serializable;
import pl.mrstudios.deathrun.arena.trap.Trap;
import pl.mrstudios.deathrun.plugin.Entrypoint;

import java.time.Duration;
import java.util.List;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import static java.lang.String.valueOf;
import static java.time.Duration.ofSeconds;
import static java.util.Optional.ofNullable;
import static org.bukkit.Bukkit.getServer;
import static org.bukkit.plugin.java.JavaPlugin.getPlugin;

public class TrapParticles extends Trap {

    @Serializable
    private Particle particle;

    @Serializable
    private int count;

    @Serializable
    private double offset;

    private @Nullable BukkitTask bukkitTask;

    @Override
    public void start() {
        this.bukkitTask = getServer().getScheduler()
                .runTaskTimer(getPlugin(Entrypoint.class), () -> super.locations.stream().map(Location::toCenterLocation).forEach((location) -> {

                    location.getWorld().spawnParticle(this.particle, location, this.count, this.offset, this.offset, this.offset, 0);
                    location.getNearbyPlayers(1, this.offset * 2)
                            .forEach((player) -> player.damage(1));

                }), 0, 15);
    }

    @Override
    public void end() {

        ofNullable(this.bukkitTask)
                .ifPresent(BukkitTask::cancel);

    }

    @Override
    public void setExtra(
            @Nullable Object... objects
    ) {
        ofNullable(objects)
                .filter((array) -> array.length >= 3)
                .ifPresent((array) -> {
                    this.particle = (Particle) array[0];
                    this.count = parseInt(valueOf(array[1]));
                    this.offset = parseDouble(valueOf(array[2]));
                });
    }

    @Override
    public @NotNull List<Location> filter(
            @NotNull List<Location> list,
            @Nullable Object... objects
    ) {
        return list;
    }

    @Override
    public @NotNull Duration getDuration() {
        return ofSeconds(3);
    }

}
