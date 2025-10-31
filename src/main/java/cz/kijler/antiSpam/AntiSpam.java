package cz.kijler.antiSpam;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class AntiSpam extends JavaPlugin {
    private static AntiSpam instance;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        ensureDefaultConfig();
        createDefaultLanguageFiles();
        checkAndUpdateLanguageFiles();
        getServer().getPluginManager().registerEvents(new AntiSpamListener(this), this);
        getCommand("antispam").setExecutor(new AntiSpamCommand(this));
        getLogger().info(getPrefix() + "Plugin Enabled");
    }

    @Override
    public void onDisable() {
        getLogger().info(getPrefix() + "Plugin Disabled");
    }

    public static AntiSpam getInstance() {
        return instance;
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

        try {
            config.save(new File(getDataFolder(), "config.yml"));
        } catch (IOException e) {
            getLogger().warning("Nepodařilo se uložit config.yml!");
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
                        "repeat-character-message: \"§7Vaše zpráva obsahuje příliš mnoho opakujících se znaků.\"\n";

        String defaultGermanMessages =
                "prefix: \"§6AntiSpam §8» \"\n" +
                        "blocked-word-message: \"§7Ihre Nachricht enthält ein blockiertes Wort.\"\n" +
                        "reload-message: \"§7Plugin wurde erfolgreich neu geladen.\"\n" +
                        "wait-message: \"§7Sie müssen noch §6%remaining_time% §7Sekunden warten, bevor Sie eine weitere Nachricht senden können.\"\n" +
                        "repeat-message: \"§7Senden Sie nicht zweimal hintereinander die gleiche Nachricht.\"\n" +
                        "no-permission-message: \"§cSie haben keine Berechtigung, diesen Befehl zu verwenden.\"\n" +
                        "repeat-character-message: \"§7Ihre Nachricht enthält zu viele wiederholte Zeichen.\"\n";

        String defaultEnglishMessages =
                "prefix: \"§6AntiSpam §8» \"\n" +
                        "blocked-word-message: \"§7Your message contains a blocked word.\"\n" +
                        "reload-message: \"§7Plugin reloaded successfully.\"\n" +
                        "wait-message: \"§7You need to wait §6%remaining_time% §7seconds before sending another message.\"\n" +
                        "repeat-message: \"§7Do not send the same message twice in a row.\"\n" +
                        "no-permission-message: \"§cYou do not have permission to use this command.\"\n" +
                        "repeat-character-message: \"§7Your message contains too many repeating characters.\"\n";

        for (File file : languageFiles) {
            FileConfiguration langConfig = YamlConfiguration.loadConfiguration(file);
            String language = file.getName().split("_")[1].split("\\.")[0];

            if (!langConfig.contains("prefix")) {
                langConfig.set("prefix", getDefaultMessage(language, "prefix", defaultCzechMessages, defaultGermanMessages, defaultEnglishMessages));
            }
            if (!langConfig.contains("blocked-word-message")) {
                langConfig.set("blocked-word-message", getDefaultMessage(language, "blocked-word-message", defaultCzechMessages, defaultGermanMessages, defaultEnglishMessages));
            }
            if (!langConfig.contains("reload-message")) {
                langConfig.set("reload-message", getDefaultMessage(language, "reload-message", defaultCzechMessages, defaultGermanMessages, defaultEnglishMessages));
            }
            if (!langConfig.contains("wait-message")) {
                langConfig.set("wait-message", getDefaultMessage(language, "wait-message", defaultCzechMessages, defaultGermanMessages, defaultEnglishMessages));
            }
            if (!langConfig.contains("repeat-message")) {
                langConfig.set("repeat-message", getDefaultMessage(language, "repeat-message", defaultCzechMessages, defaultGermanMessages, defaultEnglishMessages));
            }
            if (!langConfig.contains("no-permission-message")) {
                langConfig.set("no-permission-message", getDefaultMessage(language, "no-permission-message", defaultCzechMessages, defaultGermanMessages, defaultEnglishMessages));
            }
            if (!langConfig.contains("repeat-character-message")) {
                langConfig.set("repeat-character-message", getDefaultMessage(language, "repeat-character-message", defaultCzechMessages, defaultGermanMessages, defaultEnglishMessages));
            }

            try {
                langConfig.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getDefaultMessage(String language, String key, String defaultCzech, String defaultGerman, String defaultEnglish) {
        switch (language) {
            case "CZ":
                return defaultCzech.split("\n")[0];
            case "DE":
                return defaultGerman.split("\n")[0];
            case "EN":
            default:
                return defaultEnglish.split("\n")[0];
        }
    }
}
