package com.jazzkuh.midicontroller.common.configuration;

import com.jazzkuh.midicontroller.common.utils.configuration.ConfigurateConfig;
import lombok.Getter;

import java.nio.file.Path;

public class DefaultConfiguration extends ConfigurateConfig {
    private final @Getter String version;

    public DefaultConfiguration() {
        super(Path.of("").toAbsolutePath().resolve("controller-config.yml"));

        this.version = rootNode.node("_version").getString("1");
    }
}
