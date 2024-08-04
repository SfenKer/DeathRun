package pl.mrstudios.deathrun.arena.listener;

import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Server;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import pl.mrstudios.commons.bukkit.item.ItemBuilder;
import pl.mrstudios.commons.inject.annotation.Inject;
import pl.mrstudios.deathrun.api.arena.event.user.UserArenaCheckpointEvent;
import pl.mrstudios.deathrun.api.arena.event.user.UserArenaFinishedEvent;
import pl.mrstudios.deathrun.api.arena.user.IUser;
import pl.mrstudios.deathrun.arena.Arena;
import pl.mrstudios.deathrun.config.Configuration;

import java.util.Objects;

import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static java.util.Optional.ofNullable;
import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;
import static net.kyori.adventure.title.Title.Times.times;
import static net.kyori.adventure.title.Title.title;
import static org.bukkit.GameMode.ADVENTURE;
import static org.bukkit.Material.NETHER_PORTAL;
import static org.bukkit.Material.RED_BED;
import static org.bukkit.event.EventPriority.MONITOR;
import static org.bukkit.inventory.ItemFlag.values;
import static pl.mrstudios.deathrun.api.arena.user.enums.Role.RUNNER;
import static pl.mrstudios.deathrun.api.arena.user.enums.Role.SPECTATOR;

public class ArenaCheckpointReachedListener implements Listener {

    private final Arena arena;
    private final Plugin plugin;
    private final Server server;
    private final BukkitAudiences audiences;
    private final Configuration configuration;

    @Inject
    public ArenaCheckpointReachedListener(
            @NotNull Arena arena,
            @NotNull Plugin plugin,
            @NotNull Server server,
            @NotNull BukkitAudiences audiences,
            @NotNull Configuration configuration
    ) {
        this.arena = arena;
        this.plugin = plugin;
        this.server = server;
        this.audiences = audiences;
        this.configuration = configuration;
    }

    @SuppressWarnings("deprecation")
    @EventHandler(priority = MONITOR)
    public void onPlayerMove(
            @NotNull PlayerMoveEvent event
    ) {

        if (event.getTo().getBlock().getType() != NETHER_PORTAL)
            return;

        if (
                event.getFrom().getBlockX() == event.getTo().getBlockX()
                        && event.getFrom().getBlockY() == event.getTo().getBlockY()
                        && event.getFrom().getBlockZ() == event.getTo().getBlockZ()
                        && event.getFrom().getPitch() != event.getTo().getPitch()
                        && event.getFrom().getYaw() != event.getTo().getYaw()
        ) return;

        this.configuration.map().arenaCheckpoints.stream()
                .filter((checkpoint) -> checkpoint.locations().stream().anyMatch(
                        (location) -> location.getBlockX() == event.getTo().getBlockX() && location.getBlockY() == event.getTo().getBlockY() && location.getBlockZ() == event.getTo().getBlockZ()
                )).findFirst()
                .ifPresent(
                        (checkpoint) -> ofNullable(this.arena.getUser(event.getPlayer()))
                                .filter((user) -> user.getRole() == RUNNER)
                                .filter((user) -> user.getCheckpoint().id() < checkpoint.id())
                                .ifPresent((user) -> {

                                    UserArenaCheckpointEvent userArenaCheckpointEvent = new UserArenaCheckpointEvent(user, checkpoint);

                                    this.server.getPluginManager().callEvent(userArenaCheckpointEvent);
                                    if (event.isCancelled())
                                        return;

                                    user.setCheckpoint(checkpoint);
                                    this.audiences.player(event.getPlayer()).showTitle(
                                            title(
                                                    miniMessage().deserialize(
                                                            this.configuration.language().arenaCheckpointTitle
                                                                    .replace("<checkpoint>", String.valueOf(checkpoint.id()))
                                                    ),
                                                    miniMessage().deserialize(
                                                            this.configuration.language().arenaCheckpointSubtitle
                                                                    .replace("<checkpoint>", String.valueOf(checkpoint.id()))
                                                    ),
                                                    times(ofMillis(250), ofSeconds(3), ofMillis(250))
                                            )
                                    );

                                    event.getPlayer().playSound(event.getPlayer().getLocation(), this.configuration.plugin().arenaSoundCheckpointReached, 1, 1);
                                    if (!checkpoint.id().equals(this.configuration.map().arenaCheckpoints.get(this.configuration.map().arenaCheckpoints.size() - 1).id()))
                                        return;

                                    this.arena.setFinishedRuns(this.arena.getFinishedRuns() + 1);

                                    int position = this.arena.getFinishedRuns(), time = this.arena.getElapsedTime();
                                    this.server.getPluginManager().callEvent(
                                            new UserArenaFinishedEvent(user, time, position)
                                    );

                                    if (position == 1)
                                        if (this.arena.getRemainingTime() >= 60)
                                            this.arena.setRemainingTime(60);

                                    this.audiences.player(event.getPlayer()).showTitle(
                                            title(
                                                    miniMessage().deserialize(
                                                            this.configuration.language().arenaFinishTitle
                                                                    .replace("<position>", String.valueOf(position))
                                                                    .replace("<seconds>", String.valueOf(time))
                                                    ),
                                                    miniMessage().deserialize(
                                                            this.configuration.language().arenaFinishSubtitle
                                                                    .replace("<position>", String.valueOf(position))
                                                                    .replace("<seconds>", String.valueOf(time))
                                                    ),
                                                    times(ofMillis(250), ofSeconds(3), ofMillis(250))
                                            )
                                    );

                                    user.setRole(SPECTATOR);
                                    event.getPlayer().teleport(this.configuration.map().arenaCheckpoints.get(0).spawn());
                                    this.arena.getUsers().stream()
                                            .map(IUser::asBukkit)
                                            .filter(Objects::nonNull)
                                            .forEach((target) -> this.audiences.player(target).sendMessage(miniMessage().deserialize(
                                                    this.configuration.language().chatMessageArenaPlayerFinished
                                                            .replace("<player>", event.getPlayer().getDisplayName())
                                                            .replace("<seconds>", String.valueOf(time))
                                                            .replace("<finishPosition>", String.valueOf(position))
                                            )));

                                    this.configuration.language().chatMessageGameEndSpectator.stream()
                                            .map(miniMessage()::deserialize)
                                            .forEach((component) -> this.audiences.player(event.getPlayer()).sendMessage(component));

                                    event.getPlayer().setAllowFlight(true);
                                    event.getPlayer().setGameMode(ADVENTURE);
                                    this.arena.getUsers()
                                            .stream().map(IUser::asBukkit)
                                            .filter(Objects::nonNull).forEach(
                                                    (target) -> target.hidePlayer(this.plugin, event.getPlayer())
                                            );

                                    event.getPlayer().getInventory().clear();
                                    event.getPlayer().getInventory().setItem(
                                            8, new ItemBuilder(RED_BED)
                                                    .name(miniMessage().deserialize(this.configuration.language().arenaItemLeaveName))
                                                    .itemFlags(values())
                                                    .build()
                                    );

                                })
                );

    }

}
