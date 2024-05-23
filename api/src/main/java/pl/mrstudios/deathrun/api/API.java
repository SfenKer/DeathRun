package pl.mrstudios.deathrun.api;

import org.jetbrains.annotations.NotNull;
import pl.mrstudios.deathrun.api.arena.IArena;
import pl.mrstudios.deathrun.api.arena.trap.ITrapRegistry;

import static org.jetbrains.annotations.ApiStatus.*;

public record API(
        @NotNull IArena arenaInstance,
        @NotNull ITrapRegistry trapRegistryInstance
) {

    @Deprecated(
            forRemoval = true,
            since = "1.3.0"
    )
    @ScheduledForRemoval(inVersion = "1.4.0")
    public @NotNull IArena getArena() {
        return this.arenaInstance();
    }

    @Deprecated(
            forRemoval = true,
            since = "1.3.0"
    )
    @ScheduledForRemoval(inVersion = "1.4.0")
    public @NotNull ITrapRegistry getTrapRegistry() {
        return this.trapRegistryInstance();
    }

    @Deprecated(
            forRemoval = true,
            since = "1.3.0"
    )
    @ScheduledForRemoval(inVersion = "1.4.0")
    public @NotNull String getVersion() {
        return pluginVersion();
    }

    @Deprecated(
            forRemoval = true,
            since = "1.3.0"
    )
    @ScheduledForRemoval(inVersion = "1.4.0")
    public static @NotNull API instance;

    /* Version Related */
    public @NotNull String pluginVersion() {
        return "{version}";
    }

    public @NotNull String pluginGitBranch() {
        return "{gitBranch}";
    }

    public @NotNull String pluginGitCommit() {
        return "{gitCommitHash}";
    }

    /* Static Methods */
    public static @NotNull API apiInstance() {
        return instance;
    }

}
