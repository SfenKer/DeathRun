package pl.mrstudios.deathrun.api;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.mrstudios.deathrun.api.arena.IArena;
import pl.mrstudios.deathrun.api.arena.trap.ITrapRegistry;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.jetbrains.annotations.ApiStatus.Internal;
import static org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;

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
    public static @Nullable API instance;

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
        return checkNotNull(instance, "Instance of API is not initialized yet!");
    }

    @Internal
    public static void createInstance(
            @NotNull IArena arenaInstance,
            @NotNull ITrapRegistry trapRegistryInstance
    ) {
        checkArgument(instance == null, "Instance of API is already initialized!");
        instance = new API(
                arenaInstance,
                trapRegistryInstance
        );
    }

}
