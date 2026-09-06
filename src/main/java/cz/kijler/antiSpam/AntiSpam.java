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
    private AntiSpamListener antiSpamListener;
    private File blockedWordsFile;
    private FileConfiguration blockedWordsConfig;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        saveDefaultBlockedWords();
        saveDefaultLanguageFiles();

        this.databaseManager = new DatabaseManager(this);
        this.databaseManager.startReportPolling();

        new UpdateChecker(this, 118730).getVersion(version -> {
            if (!this.getDescription().getVersion().equalsIgnoreCase(version)) {
                this.latestVersion = version;
                getLogger().info("--------------------------------------------------");
                getLogger().info("[AntiSpam]");
                getLogger().info("A new version of the plugin is available!");
                getLogger().info("Current version: " + this.getDescription().getVersion());
                getLogger().info("New version: " + version);
                getLogger().info("Download: https://www.spigotmc.org/resources/antispam-with-web-panel.118730/");
                getLogger().info("--------------------------------------------------");
            }
        });

        this.antiSpamListener = new AntiSpamListener(this);
        getServer().getPluginManager().registerEvents(this.antiSpamListener, this);

        getCommand("antispam").setExecutor(new AntiSpamCommand(this));

        ChatReportCommand chatReportCommand = new ChatReportCommand(this);
        if (getCommand("chatreport") != null) {
            getCommand("chatreport").setExecutor(chatReportCommand);
            getCommand("chatreport").setTabCompleter(chatReportCommand);
        }
        if (getCommand("reportchat") != null) {
            getCommand("reportchat").setExecutor(chatReportCommand);
            getCommand("reportchat").setTabCompleter(chatReportCommand);
        }
        if (getCommand("chatreportlogs") != null) {
            getCommand("chatreportlogs").setExecutor(chatReportCommand);
            getCommand("chatreportlogs").setTabCompleter(chatReportCommand);
        }

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

    public AntiSpamListener getAntiSpamListener() {
        return antiSpamListener;
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

    public void reloadBlockedWordsConfig() {
        if (blockedWordsFile == null) {
            blockedWordsFile = new File(getDataFolder(), "BlockedWords.yml");
        }
        blockedWordsConfig = YamlConfiguration.loadConfiguration(blockedWordsFile);
    }

    public FileConfiguration getBlockedWordsConfig() {
        if (blockedWordsConfig == null) {
            reloadBlockedWordsConfig();
        }
        return blockedWordsConfig;
    }

    private void saveDefaultBlockedWords() {
        if (blockedWordsFile == null) {
            blockedWordsFile = new File(getDataFolder(), "BlockedWords.yml");
        }
        if (!blockedWordsFile.exists()) {
            try {
                saveResource("BlockedWords.yml", false);
            } catch (IllegalArgumentException e) {
                blockedWordsConfig = YamlConfiguration.loadConfiguration(blockedWordsFile);
                blockedWordsConfig.set("blocked-words", java.util.Arrays.asList("badword1", "badword2"));
                try {
                    blockedWordsConfig.save(blockedWordsFile);
                } catch (IOException ex) {
                    getLogger().warning("Could not save BlockedWords.yml!");
                }
            }
        }
        reloadBlockedWordsConfig();
    }

    private void saveDefaultLanguageFiles() {
        String[] langs = {"EN", "CZ", "DE"};
        for (String lang : langs) {
            String resourcePath = "languages/Locale_" + lang + ".yml";
            File file = new File(getDataFolder(), resourcePath);
            if (!file.exists()) {
                try {
                    saveResource(resourcePath, false);
                } catch (IllegalArgumentException e) {
                    getLogger().warning("Default language file not found in resources: " + resourcePath);
                }
            }
        }
    }
}
