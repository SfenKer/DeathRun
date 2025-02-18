package pl.mrstudios.deathrun.config.impl;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.annotation.Header;
import eu.okaeri.configs.annotation.Names;

import java.util.List;

import static eu.okaeri.configs.annotation.NameModifier.TO_LOWER_CASE;
import static eu.okaeri.configs.annotation.NameStrategy.HYPHEN_CASE;
import static java.util.Arrays.asList;

@Header({
        " ",
        "------------------------------------------------------------------------",
        "                              INFORMATION",
        "------------------------------------------------------------------------",
        " ",
        " This is configuration file for DeathRun plugin, if you found any issue ",
        " contact with us through Discord or create issue on GitHub. If you need",
        " help with configuration visit https://mrstudios.pl/documentation.",
        " "
}) @SuppressWarnings("deprecation")
@Names(strategy = HYPHEN_CASE, modifier = TO_LOWER_CASE)
public class LanguageConfiguration extends OkaeriConfig {

    @Comment({
            "",
            "------------------------------------------------------------------------",
            "                                 GENERAL",
            "------------------------------------------------------------------------",
            ""
    })
    public String chatMessageNoPermissions = "<red>You don't have permissions to this command.";
    public String chatMessageInvalidCommandUsage = "<red>Invalid command usage, correct usage is <dark_red><usage><red>.";
    public String chatMessageArenaPlayerJoined = "<gray><player> <yellow>has joined. <aqua>(<currentPlayers>/<maxPlayers>)";
    public String chatMessageArenaPlayerLeft = "<gray><player> <yellow>has quit.";
    public String chatMessageArenaStartingTimer = "<yellow>Game starts in <gold><timer> seconds<yellow>.";
    public String chatMessageArenaPlayerFinished = "<reset> <white><b>FINISH ></b> <gray>Player <gold><player> <gray>has finished game in <white><seconds> seconds<gray>. <dark_gray>(#<finishPosition>)";

    public List<String> chatMessageArenaGameStartRunner = asList(
            "<reset>",
            "<reset>   <gold><b>*</b> <gray>You are <green>Runner<gray>.",
            "<reset>   <white><b>*</b> <gray>Your task is complete run in shortest possible time, during this task interfering player will trigger various traps.",
            "<reset>"
    );

    public List<String> chatMessageArenaGameStartDeath = asList(
            "<reset>",
            "<reset>   <gold><b>*</b> <gray>You are <red>Death<gray>.",
            "<reset>   <white><b>*</b> <gray>Your task is to disturb runners by launching traps.",
            "<reset>"
    );

    public List<String> chatMessageGameEndSpectator = asList(
            "<reset>",
            "<reset>   <gold><b>*</b> <gray>You are <dark_gray>Spectator<gray>.",
            "<reset>   <white><b>*</b> <gray>Now you can follow other players.",
            "<reset>"
    );

    @Comment({
            "",
            "------------------------------------------------------------------------",
            "                                 TITLES",
            "------------------------------------------------------------------------",
            ""
    })
    public String arenaPreStartingTitle = "<red><timer>";
    public String arenaPreStartingSubtitle = "<reset>";

    public String arenaStartingTitle = "<red><timer>";
    public String arenaStartingSubtitle = "<reset>";

    public String arenaDeathTitle = "<red>YOU DIED!";
    public String arenaDeathSubtitle = "<yellow>Don't give up! Try again!";

    public String arenaCheckpointTitle = "<yellow>CHECKPOINT!";
    public String arenaCheckpointSubtitle = "<gold>You reached <yellow>#<checkpoint> checkpoint<gold>.";

    public String arenaFinishTitle = "<dark_aqua><b>FINISH";
    public String arenaFinishSubtitle = "<gray>Your position is <white>#<position><gray>.";

    public String arenaGameEndTitle = "<red><b>GAME END!";
    public String arenaGameEndSubtitle = "<reset>";

    public String arenaMoveServerTitle = "<aqua>Waiting..";
    public String arenaMoveServerSubtitle = "<gray>You will be transferred to lobby in <white><endTimer> seconds<gray>.";

    @Comment({
            "",
            "------------------------------------------------------------------------",
            "                              SCOREBOARD",
            "------------------------------------------------------------------------",
            ""
    })
    public String arenaScoreboardTitle = "<yellow><b>DEATH RUN";

    public List<String> arenaScoreboardLinesWaiting = asList(
            "<reset>",
            "<white>Map: <green><map>",
            "<white>Players: <green><currentPlayers>/<maxPlayers>",
            "<reset>",
            "<white>Waiting..",
            "<reset>",
            "<yellow>www.mrstudios.pl"
    );

    public List<String> arenaScoreboardLinesStarting = asList(
            "<reset>",
            "<white>Map: <green><map>",
            "<white>Players: <green><currentPlayers>/<maxPlayers>",
            "<reset>",
            "<white>Start in <green><timer> seconds",
            "<reset>",
            "<yellow>www.mrstudios.pl"
    );

    public List<String> arenaScoreboardLinesPlaying = asList(
            "<reset>",
            "<white>Time: <green><timeFormatted>",
            "<white>Role: <green><role>",
            "<reset>",
            "<white>Runners: <green><runners>",
            "<white>Deaths: <red><deaths>",
            "<reset>",
            "<white>Map: <green><map>",
            "<reset>",
            "<yellow>www.mrstudios.pl"
    );

    @Comment({
            "",
            "------------------------------------------------------------------------",
            "                              HOLOGRAMS",
            "------------------------------------------------------------------------",
            ""
    })
    public String arenaHologramTrapDelayed = "<red><delay> seconds";

    @Comment({
            "",
            "------------------------------------------------------------------------",
            "                                ROLES",
            "------------------------------------------------------------------------",
            ""
    })
    public String arenaRolesRunnerName = "<green>Runner";
    public String arenaRolesDeathName = "<red>Death";
    public String arenaRolesSpectatorName = "<gray>Spectator";

    @Comment({
            "",
            "------------------------------------------------------------------------",
            "                                ITEMS",
            "------------------------------------------------------------------------",
            ""
    })
    public String arenaItemLeaveName = "<red>Leave <gray>(Right Click)";

}
