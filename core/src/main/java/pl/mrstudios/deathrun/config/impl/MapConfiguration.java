package pl.mrstudios.deathrun.config.impl;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Header;
import eu.okaeri.configs.annotation.Names;
import org.bukkit.Location;
import pl.mrstudios.deathrun.api.arena.trap.ITrap;
import pl.mrstudios.deathrun.arena.checkpoint.Checkpoint;
import pl.mrstudios.deathrun.arena.pad.TeleportPad;

import java.util.ArrayList;
import java.util.List;

import static eu.okaeri.configs.annotation.NameModifier.TO_LOWER_CASE;
import static eu.okaeri.configs.annotation.NameStrategy.HYPHEN_CASE;

@Header({
        " ",
        "--------------------------------------------------------------------------",
        "                                INFORMATION",
        "--------------------------------------------------------------------------",
        " ",
        " Please dont modify this file, any modifications may cause problems with",
        " plugin, modify only if you know what you are doing.",
        " ",
        "--------------------------------------------------------------------------",
        " "
}) @SuppressWarnings("deprecation")
@Names(strategy = HYPHEN_CASE, modifier = TO_LOWER_CASE)
public class MapConfiguration extends OkaeriConfig {

    public String arenaName;

    /* Spawns */
    public Location arenaWaitingLobbyLocation;

    public List<Location> arenaRunnerSpawnLocations = new ArrayList<>();;
    public List<Location> arenaDeathSpawnLocations = new ArrayList<>();;

    /* Traps */
    public List<ITrap> arenaTraps = new ArrayList<>();;

    /* Checkpoints */
    public List<Checkpoint> arenaCheckpoints = new ArrayList<>();;

    /* Misc */
    public List<TeleportPad> teleportPads = new ArrayList<>();
    public List<Location> arenaStartBarrierBlocks = new ArrayList<>();;

    /* Setup Status */
    public boolean arenaSetupEnabled = true;

}
