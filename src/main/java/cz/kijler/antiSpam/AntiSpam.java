package cz.kijler.antiSpam;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class AntiSpam extends JavaPlugin {
    private static AntiSpam instance;
    private DatabaseManager databaseManager;
    private String latestVersion;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        ensureDefaultConfig();
        createDefaultLanguageFiles();
        checkAndUpdateLanguageFiles();

        this.databaseManager = new DatabaseManager(this);

        new UpdateChecker(this, 118730).getVersion(version -> {
            if (!this.getDescription().getVersion().equalsIgnoreCase(version)) {
                this.latestVersion = version;
                getLogger().info("New version of AntiSpam plugin found: " + version);
                getLogger().info("Download here: https://www.spigotmc.org/resources/antispam-1-17-1-21.118730/");
            }
        });

        getServer().getPluginManager().registerEvents(new AntiSpamListener(this), this);
        getCommand("antispam").setExecutor(new AntiSpamCommand(this));
        getLogger().info(getPrefix() + "Plugin Enabled");
    }

    @Override
    public void onDisable() {
        if (databaseManager != null) {
            databaseManager.close();
        }
        getLogger().info(getPrefix() + "Plugin Disabled");
    }

    public static AntiSpam getInstance() {
        return instance;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public String getLatestVersion() {
        return latestVersion;
    }

    public String getPrefix() {
        String language = getConfig().getString("language", "EN");
        switch (language) {
            case "CZ":
                return getLanguageConfig().getString("prefix", "§6AntiSpam §8» ");
            case "DE":
                return getLanguageConfig().getString("prefix", "§6AntiSpam §8» ");
            case "EN":
            default:
                return getLanguageConfig().getString("prefix", "§6AntiSpam §8» ");
        }
    }

    public FileConfiguration getLanguageConfig() {
        String language = getConfig().getString("language", "EN");
        File file = new File(getDataFolder(), "languages/Locale_" + language + ".yml");
        return YamlConfiguration.loadConfiguration(file);
    }

    private void createDefaultLanguageFiles() {
        File languageFolder = new File(getDataFolder(), "languages");
        if (!languageFolder.exists()) {
            languageFolder.mkdirs();
        }

        File[] languageFiles = {
                new File(languageFolder, "Locale_EN.yml"),
                new File(languageFolder, "Locale_CZ.yml"),
                new File(languageFolder, "Locale_DE.yml")
        };

        for (File file : languageFiles) {
            if (!file.exists()) {
                saveResource("languages/" + file.getName(), false);
            }
        }
    }

    private void ensureDefaultConfig() {
        FileConfiguration config = getConfig();

        if (!config.contains("language")) {
            config.set("language", "EN");
        }

        if (!config.contains("database.type")) config.set("database.type", "SQLITE");
        if (!config.contains("database.host")) config.set("database.host", "localhost");
        if (!config.contains("database.port")) config.set("database.port", 3306);
        if (!config.contains("database.database")) config.set("database.database", "minecraft");
        if (!config.contains("database.username")) config.set("database.username", "root");
        if (!config.contains("database.password")) config.set("database.password", "password");
        if (!config.contains("database.useSSL")) config.set("database.useSSL", false);

        if (!config.contains("punishment-issuer")) {
            config.set("punishment-issuer", "AntiSpam");
        }

        if (!config.contains("block-repeated-messages")) {
            config.set("block-repeated-messages", true);
        }
        if (!config.contains("delay")) {
            config.set("delay", 3);
        }
        if (!config.contains("repeat-message-delay")) {
            config.set("repeat-message-delay", 60);
        }
        if (!config.contains("block-repeated-characters")) {
            config.set("block-repeated-characters", true);
        }
        if (!config.contains("max-repeating-characters")) {
            config.set("max-repeating-characters", 3);
        }
        if (!config.contains("blocked-words")) {
            config.set("blocked-words", new String[]{"badword1", "badword2"});
        }

        if (!config.contains("punishments.blocked-word.enabled")) config.set("punishments.blocked-word.enabled", true);
        if (!config.contains("punishments.blocked-word.threshold")) config.set("punishments.blocked-word.threshold", 3);
        if (!config.contains("punishments.blocked-word.command")) config.set("punishments.blocked-word.command", "tempmute %player% 1h Inappropriate behavior #%punish_id%");

        if (!config.contains("punishments.spam.enabled")) config.set("punishments.spam.enabled", true);
        if (!config.contains("punishments.spam.threshold")) config.set("punishments.spam.threshold", 5);
        if (!config.contains("punishments.spam.command")) config.set("punishments.spam.command", "tempmute %player% 30m Spam #%punish_id%");

        if (!config.contains("punishments.flood.enabled")) config.set("punishments.flood.enabled", true);
        if (!config.contains("punishments.flood.threshold")) config.set("punishments.flood.threshold", 5);
        if (!config.contains("punishments.flood.command")) config.set("punishments.flood.command", "tempmute %player% 15m Flood #%punish_id%");

        try {
            config.save(new File(getDataFolder(), "config.yml"));
        } catch (IOException e) {
            getLogger().warning("Could not save config.yml!");
        }
    }

    private void checkAndUpdateLanguageFiles() {
        File languageFolder = new File(getDataFolder(), "languages");
        File[] languageFiles = {
                new File(languageFolder, "Locale_EN.yml"),
                new File(languageFolder, "Locale_CZ.yml"),
                new File(languageFolder, "Locale_DE.yml")
        };

        String defaultCzechMessages =
                "prefix: \"§6AntiSpam §8» \"\n" +
                        "blocked-word-message: \"§7Vaše zpráva obsahuje blokované slovo.\"\n" +
                        "reload-message: \"§aPlugin byl úspěšně znovu načten.\"\n" +
                        "wait-message: \"§7Musíte počkat §6%remaining_time% §7sekundy před odesláním další zprávy.\"\n" +
                        "repeat-message: \"§7Neposílejte stejnou zprávu dvakrát za sebou.\"\n" +
                        "no-permission-message: \"§cNemáte oprávnění k použití tohoto příkazu.\"\n" +
                        "repeat-character-message: \"§7Vaše zpráva obsahuje příliš mnoho opakujících se znaků.\"\n" +
                        "invalid-id-message: \"§cID musí být číslo.\"\n" +
                        "punish-check-found: |-\n" +
                        "  §7Vyhledávání trestu\n" +
                        "  §7Punishment ID §6%id%\n" +
                        "  §7player §e%player%\n" +
                        "  §7category §c%category%\n" +
                        "  §7for message: §f%content%\n" +
                        "punish-check-not-found: \"§cTrest s tímto ID nebyl nalezen.\"\n";

        String defaultGermanMessages =
                "prefix: \"§6AntiSpam §8» \"\n" +
                        "blocked-word-message: \"§7Ihre Nachricht enthält ein blockiertes Wort.\"\n" +
                        "reload-message: \"§7Plugin wurde erfolgreich neu geladen.\"\n" +
                        "wait-message: \"§7Sie müssen noch §6%remaining_time% §7Sekunden warten, bevor Sie eine weitere Nachricht senden können.\"\n" +
                        "repeat-message: \"§7Senden Sie nicht zweimal hintereinander die gleiche Nachricht.\"\n" +
                        "no-permission-message: \"§cSie haben keine Berechtigung, diesen Befehl zu verwenden.\"\n" +
                        "repeat-character-message: \"§7Ihre Nachricht enthält zu viele wiederholte Zeichen.\"\n" +
                        "invalid-id-message: \"§cID muss eine Nummer sein.\"\n" +
                        "punish-check-found: |-\n" +
                        "  §7Punishment Lookup\n" +
                        "  §7Punishment ID §6%id%\n" +
                        "  §7player §e%player%\n" +
                        "  §7category §c%category%\n" +
                        "  §7for message: §f%content%\n" +
                        "punish-check-not-found: \"§cStrafe mit dieser ID nicht gefunden.\"\n";

        String defaultEnglishMessages =
                "prefix: \"§6AntiSpam §8» \"\n" +
                        "blocked-word-message: \"§7Your message contains a blocked word.\"\n" +
                        "reload-message: \"§7Plugin reloaded successfully.\"\n" +
                        "wait-message: \"§7You need to wait §6%remaining_time% §7seconds before sending another message.\"\n" +
                        "repeat-message: \"§7Do not send the same message twice in a row.\"\n" +
                        "no-permission-message: \"§cYou do not have permission to use this command.\"\n" +
                        "repeat-character-message: \"§7Your message contains too many repeating characters.\"\n" +
                        "invalid-id-message: \"§cID must be a number.\"\n" +
                        "punish-check-found: |-\n" +
                        "  §7Punishment Lookup\n" +
                        "  §7Punishment ID §6%id%\n" +
                        "  §7player §e%player%\n" +
                        "  §7category §c%category%\n" +
                        "  §7for message: §f%content%\n" +
                        "punish-check-not-found: \"§cPunishment with this ID not found.\"\n";

        for (File file : languageFiles) {
            FileConfiguration langConfig = YamlConfiguration.loadConfiguration(file);
            String language = file.getName().split("_")[1].split("\\.")[0];

            if (!langConfig.contains("prefix")) langConfig.set("prefix", getDefaultMessage(language, "prefix", defaultCzechMessages, defaultGermanMessages, defaultEnglishMessages));
            if (!langConfig.contains("blocked-word-message")) langConfig.set("blocked-word-message", getDefaultMessage(language, "blocked-word-message", defaultCzechMessages, defaultGermanMessages, defaultEnglishMessages));
            if (!langConfig.contains("reload-message")) langConfig.set("reload-message", getDefaultMessage(language, "reload-message", defaultCzechMessages, defaultGermanMessages, defaultEnglishMessages));
            if (!langConfig.contains("wait-message")) langConfig.set("wait-message", getDefaultMessage(language, "wait-message", defaultCzechMessages, defaultGermanMessages, defaultEnglishMessages));
            if (!langConfig.contains("repeat-message")) langConfig.set("repeat-message", getDefaultMessage(language, "repeat-message", defaultCzechMessages, defaultGermanMessages, defaultEnglishMessages));
            if (!langConfig.contains("no-permission-message")) langConfig.set("no-permission-message", getDefaultMessage(language, "no-permission-message", defaultCzechMessages, defaultGermanMessages, defaultEnglishMessages));
            if (!langConfig.contains("repeat-character-message")) langConfig.set("repeat-character-message", getDefaultMessage(language, "repeat-character-message", defaultCzechMessages, defaultGermanMessages, defaultEnglishMessages));
            if (!langConfig.contains("invalid-id-message")) langConfig.set("invalid-id-message", getDefaultMessage(language, "invalid-id-message", defaultCzechMessages, defaultGermanMessages, defaultEnglishMessages));

            if (!langConfig.contains("punish-check-found")) {
                if (language.equals("CZ")) {
                    langConfig.set("punish-check-found", "§7Vyhledávání trestu\n§7Punishment ID §6%id%\n§7player §e%player%\n§7category §c%category%\n§7for message: §f%content%");
                } else {
                    langConfig.set("punish-check-found", "§7Punishment Lookup\n§7Punishment ID §6%id%\n§7player §e%player%\n§7category §c%category%\n§7for message: §f%content%");
                }
            }
            if (!langConfig.contains("punish-check-not-found")) langConfig.set("punish-check-not-found", getDefaultMessage(language, "punish-check-not-found", defaultCzechMessages, defaultGermanMessages, defaultEnglishMessages));

            try {
                langConfig.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getDefaultMessage(String language, String key, String defaultCzech, String defaultGerman, String defaultEnglish) {
        String source = defaultEnglish;
        if (language.equals("CZ")) source = defaultCzech;
        if (language.equals("DE")) source = defaultGerman;

        for (String line : source.split("\n")) {
            if (line.startsWith(key + ":")) {
                if (line.contains("|-")) return null;
                return line.substring(line.indexOf("\"") + 1, line.lastIndexOf("\""));
            }
        }
        return "";
    }
}
