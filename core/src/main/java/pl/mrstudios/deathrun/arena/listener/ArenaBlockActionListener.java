package pl.mrstudios.deathrun.arena.listener;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;
import pl.mrstudios.commons.inject.annotation.Inject;

import static java.util.Arrays.stream;
import static org.bukkit.Material.*;
import static org.bukkit.event.EventPriority.MONITOR;
import static org.bukkit.event.block.Action.PHYSICAL;
import static org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK;

public class ArenaBlockActionListener implements Listener {

    @Inject
    public ArenaBlockActionListener() {}

    @EventHandler(priority = MONITOR)
    public void onBlockBreak(
            @NotNull BlockBreakEvent event
    ) {
        event.setCancelled(true);
    }

    @EventHandler(priority = MONITOR)
    public void onBlockPlace(
            @NotNull BlockPlaceEvent event
    ) {
        event.setCancelled(true);
    }

    @EventHandler(priority = MONITOR)
    public void onPlayerInteract(
            @NotNull PlayerInteractEvent event
    ) {

        if (event.getAction() == RIGHT_CLICK_BLOCK)
            if (event.getClickedBlock() != null)
                if (stream(containerMaterials).anyMatch((material) -> material == event.getClickedBlock().getType()))
                    event.setCancelled(true);

        if (event.getAction() == PHYSICAL)
            event.setCancelled(true);

    }

    protected static final Material[] containerMaterials = {
            CHEST, DISPENSER, DROPPER, FURNACE,
            HOPPER, BREWING_STAND, BEACON, ANVIL,
            CHIPPED_ANVIL, DAMAGED_ANVIL, ENCHANTING_TABLE,
            ENDER_CHEST, BARREL, BLAST_FURNACE, SMOKER
    };

}
