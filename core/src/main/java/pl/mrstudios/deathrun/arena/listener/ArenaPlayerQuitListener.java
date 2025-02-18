package pl.mrstudios.deathrun.arena.listener;

import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Server;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
import pl.mrstudios.commons.inject.annotation.Inject;
import pl.mrstudios.deathrun.api.arena.event.arena.ArenaUserLeftEvent;
import pl.mrstudios.deathrun.arena.Arena;
import pl.mrstudios.deathrun.config.Configuration;

import static java.lang.String.valueOf;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;
import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;
import static org.bukkit.event.EventPriority.MONITOR;
import static pl.mrstudios.deathrun.api.arena.enums.GameState.STARTING;
import static pl.mrstudios.deathrun.api.arena.enums.GameState.WAITING;

public class ArenaPlayerQuitListener implements Listener {

    private final Arena arena;
    private final Server server;
    private final BukkitAudiences audiences;
    private final Configuration configuration;

    @Inject
    public ArenaPlayerQuitListener(
            @NotNull Arena arena,
            @NotNull Server server,
            @NotNull BukkitAudiences audiences,
            @NotNull Configuration configuration
    ) {
        this.arena = arena;
        this.server = server;
        this.audiences = audiences;
        this.configuration = configuration;
    }

    @SuppressWarnings("deprecation")
    @EventHandler(priority = MONITOR)
    public void onPlayerQuit(
            @NotNull PlayerQuitEvent event
    ) {

        event.setQuitMessage("");
        if (this.arena.getUser(event.getPlayer()) == null)
            return;

        ofNullable(this.arena.getUser(event.getPlayer()))
                .stream().peek(this.arena.getUsers()::remove)
                .findFirst().ifPresent((user) -> {

                    if (this.arena.getGameState() != WAITING && this.arena.getGameState() != STARTING)
                        return;

                    this.arena.getUsers().forEach((target) ->
                            this.audiences.player(requireNonNull(target.asBukkit())).sendMessage(miniMessage().deserialize(
                                    this.configuration.language().chatMessageArenaPlayerLeft
                                            .replace("<player>", event.getPlayer().getDisplayName())
                                            .replace("<currentPlayers>", valueOf(this.arena.getUsers().size()))
                                            .replace("<maxPlayers>", valueOf(this.configuration.map().arenaRunnerSpawnLocations.size() + this.configuration.map().arenaDeathSpawnLocations.size()))
                            )));

                    this.arena.getSidebar().removeViewer(event.getPlayer());
                    this.server.getPluginManager().callEvent(new ArenaUserLeftEvent(user, this.arena));

                });

    }

}
