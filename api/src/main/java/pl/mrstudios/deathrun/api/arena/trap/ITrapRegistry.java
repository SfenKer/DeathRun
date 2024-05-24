package pl.mrstudios.deathrun.api.arena.trap;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Set;

import static org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;

public interface ITrapRegistry {

    @Nullable Class<? extends ITrap> get(@NotNull String identifier);

    void register(@NotNull Class<? extends ITrap> trapClass);
    void register(@NotNull String identifier, @NotNull Class<? extends ITrap> trapClass);

    @Deprecated(
            since = "1.3.0",
            forRemoval = true
    ) @ScheduledForRemoval(inVersion = "1.4.0")
    @NotNull Set<String> list();

    @NotNull Collection<String> trapRegistryKeys();

}
