package com.jazzkuh.midicontroller.common.utils.configuration;

import com.jazzkuh.midicontroller.MidiController;
import lombok.Getter;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.loader.HeaderMode;
import org.spongepowered.configurate.util.MapFactories;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Level;

public abstract class ConfigurateConfig {
    protected final @Getter YamlConfigurationLoader loader;
    protected @Getter CommentedConfigurationNode rootNode;

    public ConfigurateConfig(Path path) {
        loader = YamlConfigurationLoader.builder()
                .path(path)
                .indent(2)
                .nodeStyle(NodeStyle.BLOCK)
                .headerMode(HeaderMode.PRESET)
                .defaultOptions(options -> options.mapFactory(MapFactories.sortedNatural()))
                .build();

        try {
            rootNode = loader.load();
        } catch (IOException e) {
            MidiController.getInstance().getLogger().log(Level.WARNING, "An error occurred while loading this configuration: " + e.getMessage());
        }
    }

    public void saveConfiguration() {
        try {
            loader.save(rootNode);
        } catch (final ConfigurateException e) {
            MidiController.getInstance().getLogger().log(Level.WARNING, "Unable to save your configuration! Sorry! " + e.getMessage());
        }
    }
}
