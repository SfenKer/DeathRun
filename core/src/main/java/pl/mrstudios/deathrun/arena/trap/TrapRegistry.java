package pl.mrstudios.deathrun.arena.trap;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.mrstudios.deathrun.api.arena.trap.ITrap;
import pl.mrstudios.deathrun.api.arena.trap.ITrapRegistry;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static java.util.List.copyOf;

public class TrapRegistry implements ITrapRegistry {

    private final Map<String, Class<? extends ITrap>> traps;

    public TrapRegistry() {
        this.traps = new HashMap<>();
    }

    @Override
    public @Nullable Class<? extends ITrap> get(
            @NotNull String identifier
    ) {
        return this.traps.getOrDefault(identifier.toUpperCase(), null);
    }

    @Override
    public void register(
            @NotNull Class<? extends ITrap> trapClass
    ) {
        this.register(trapClass.getSimpleName().replace("Trap", ""), trapClass);
    }

    @Override
    public void register(
            @NotNull String identifier,
            @NotNull Class<? extends ITrap> trapClass
    ) {
        this.traps.put(identifier.toUpperCase(), trapClass);
    }

    @Override
    public @NotNull Set<String> list() {
        return Set.copyOf(this.traps.keySet());
    }

    @Override
    public @NotNull Collection<String> trapRegistryKeys() {
        return copyOf(this.traps.keySet());
    }

}
