package pl.mrstudios.deathrun.arena.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import pl.mrstudios.commons.inject.annotation.Inject;
import pl.mrstudios.deathrun.config.Configuration;

import static org.bukkit.Material.RED_BED;
import static org.bukkit.event.block.Action.RIGHT_CLICK_AIR;
import static org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK;
import static pl.mrstudios.deathrun.util.ChannelUtil.connect;

public class ArenaClickItemListener implements Listener {

    private final Plugin plugin;
    private final Configuration configuration;

    @Inject
    public ArenaClickItemListener(
            @NotNull Plugin plugin,
            @NotNull Configuration configuration
    ) {
        this.plugin = plugin;
        this.configuration = configuration;
    }

    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.MONITOR)
    public void onStepOnBlockEffect(
            @NotNull PlayerInteractEvent event
    ) {

        if (event.getAction() != RIGHT_CLICK_BLOCK && event.getAction() != RIGHT_CLICK_AIR)
            return;

        if (event.getPlayer().getItemInHand().getType() != RED_BED)
            return;

        connect(plugin, event.getPlayer(), this.configuration.plugin().server);

    }

}
