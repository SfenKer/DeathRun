package pl.mrstudios.deathrun.plugin;

import com.sk89q.worldedit.WorldEdit;
import dev.rollczi.litecommands.LiteCommands;
import dev.rollczi.litecommands.annotations.command.Command;
import dev.rollczi.litecommands.argument.ArgumentKey;
import dev.rollczi.litecommands.suggestion.SuggestionResult;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.lingala.zip4j.ZipFile;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import pl.mrstudios.commons.inject.Injector;
import pl.mrstudios.commons.inject.annotation.Inject;
import pl.mrstudios.commons.reflection.Reflections;
import pl.mrstudios.deathrun.arena.Arena;
import pl.mrstudios.deathrun.arena.ArenaServiceRunnable;
import pl.mrstudios.deathrun.arena.trap.TrapRegistry;
import pl.mrstudios.deathrun.arena.trap.impl.*;
import pl.mrstudios.deathrun.command.handler.InvalidCommandUsageHandler;
import pl.mrstudios.deathrun.command.handler.NoCommandPermissionsHandler;
import pl.mrstudios.deathrun.config.Configuration;
import pl.mrstudios.deathrun.config.ConfigurationFactory;
import pl.mrstudios.deathrun.config.impl.LanguageConfiguration;
import pl.mrstudios.deathrun.config.impl.MapConfiguration;
import pl.mrstudios.deathrun.config.impl.PluginConfiguration;
import pl.mrstudios.deathrun.exception.MissingDependencyException;

import java.io.File;
import java.util.Objects;

import static com.sk89q.worldedit.WorldEdit.getInstance;
import static dev.rollczi.litecommands.annotations.LiteCommandsAnnotations.of;
import static dev.rollczi.litecommands.bukkit.LiteCommandsBukkit.builder;
import static dev.rollczi.litecommands.schematic.SchematicFormat.angleBrackets;
import static java.lang.String.format;
import static java.nio.file.Paths.get;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static net.kyori.adventure.platform.bukkit.BukkitAudiences.create;
import static org.apache.commons.io.FileUtils.deleteDirectory;
import static pl.mrstudios.deathrun.api.API.apiInstance;
import static pl.mrstudios.deathrun.api.API.createInstance;

@SuppressWarnings("all")
public class Entrypoint extends JavaPlugin {

    private Arena arena;
    private TrapRegistry trapRegistry;

    private BukkitAudiences audiences;

    private Configuration configuration;
    private ConfigurationFactory configurationFactory;

    private Injector injector;
    private WorldEdit worldEdit;
    private LiteCommands<CommandSender> liteCommands;

    @Override
    public void onEnable() {

        /* Dependency Check */
        if (!this.getServer().getPluginManager().isPluginEnabled("WorldEdit"))
            throw new MissingDependencyException("You must have WorldEdit (v7.2.9+) installed on your server to use this plugin.");

        /* World Edit */
        this.worldEdit = getInstance();

        /* Configuration */
        this.configurationFactory = new ConfigurationFactory(this.getDataFolder().toPath());
        this.configuration = new Configuration(
                this.configurationFactory.produce(PluginConfiguration.class, "config.yml"),
                this.configurationFactory.produce(LanguageConfiguration.class, "language.yml"),
                this.configurationFactory.produce(MapConfiguration.class, "map.yml")
        );

        /* Kyori */
        this.audiences = create(this);

        /* Create Arena Instance */
        this.arena = new Arena(this.configuration.map().arenaName);

        /* Trap Registry */
        this.trapRegistry = new TrapRegistry();

        /* Initialize Injector */
        this.injector = new Injector()

                /* Bukkit */
                .register(Plugin.class, this)
                .register(Server.class, this.getServer())

                /* Kyori */
                .register(BukkitAudiences.class, this.audiences)

                /* World Edit */
                .register(WorldEdit.class, this.worldEdit)

                /* Plugin Stuff */
                .register(Arena.class, this.arena)
                .register(TrapRegistry.class, this.trapRegistry)
                .register(Configuration.class, this.configuration);

        /* Register Traps */
        asList(
                TrapTNT.class,
                TrapAppearingBlocks.class,
                TrapDisappearingBlocks.class,
                TrapArrows.class,
                TrapParticles.class
        ).forEach(this.trapRegistry::register);

        /* Register Commands */
        this.liteCommands = builder()

                /* Settings */
                .settings((settings) -> settings.nativePermissions(false))

                /* Handler */
                .invalidUsage(this.injector.inject(InvalidCommandUsageHandler.class))
                .missingPermission(this.injector.inject(NoCommandPermissionsHandler.class))

                /* Commands */
                .commands(of(
                        new Reflections<>("pl.mrstudios.deathrun.command")
                                .getClassesAnnotatedWith(Command.class)
                                .stream().map(this.injector::inject)
                                .filter(Objects::nonNull).toArray(Object[]::new)
                ))

                /* Schematic */
                .schematicGenerator(angleBrackets())

                /* Suggesters */
                .argumentSuggester(String.class, ArgumentKey.of("type"), SuggestionResult.of(this.trapRegistry.list()))

                /* Build */
                .build();

        /* Register Listeners */
        if (!this.configuration.map().arenaSetupEnabled)
            new Reflections<Listener>("pl.mrstudios.deathrun.arena.listener")
                    .getClassesImplementing(Listener.class).stream().filter(
                            (listener) -> stream(listener.getConstructors())
                                    .anyMatch((constructor) -> constructor.isAnnotationPresent(Inject.class))
                    ).forEach(
                            (listener) -> this.getServer().getPluginManager()
                                    .registerEvents(this.injector.inject(listener), this)
                    );

        /* Start Arena Service */
        if (!this.configuration.map().arenaSetupEnabled)
            this.injector.inject(ArenaServiceRunnable.class)
                    .runTaskTimer(this, 0, 20);

        /* Initialize API */
        createInstance(this.arena, this.trapRegistry);

        /* Set Max Players */
        if (!this.configuration.map().arenaSetupEnabled)
            this.getServer().setMaxPlayers(this.configuration.map().arenaRunnerSpawnLocations.size() + this.configuration.map().arenaDeathSpawnLocations.size());

        /* Register Channel */
        if (!this.configuration.map().arenaSetupEnabled)
            this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        /* Check Branch */
        if (!apiInstance().pluginGitBranch().equals("ver/latest"))
            this.getLogger().warning(
                    """
                         
                         --------------------------------------------------------
                         
                                       DEVELOPMENT BUILD DETECTED
                                        
                          You are running on a development build of the plugin,
                          which may contain bugs and other issues. Please report
                          any bugs you found on our GitHub repository.
                          
                          Version: {version}
                          Current Branch: {branch}
                         
                         --------------------------------------------------------
                         """.replace("{version}", apiInstance().pluginVersion())
                            .replace("{branch}", apiInstance().pluginGitBranch())
            );

    }

    @Override
    public void onDisable() {

        if (this.audiences != null)
            this.audiences.close();

    }

    @Override
    public void onLoad() {

        try {

            stream(new File(this.getDataFolder(), "backup").listFiles())
                    .filter((file) -> file.getName().endsWith(".zip"))
                    .forEach((file) -> {

                        try {

                            deleteDirectory(get(file.getName().replace(".zip", "")).toFile());

                            try (ZipFile zipFile = new ZipFile(file)) {
                                zipFile.extractAll(get("./").toString());
                            }

                        } catch (@NotNull Exception exception) {
                            this.getLogger().severe(format("An error occurred while restoring %s world backup.", file.getName().replace(".zip", "")));
                        }

                    });

        } catch (@NotNull Exception ignored) {}

    }

}
