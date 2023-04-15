package com.jazzkuh.midicontroller.common.configuration;

import com.jazzkuh.midicontroller.common.utils.configuration.ConfigurateConfig;
import lombok.Getter;

import java.nio.file.Path;

public class DefaultConfiguration extends ConfigurateConfig {
    private final @Getter String version;
    private final @Getter String spotifyClientId;
    private final @Getter String spotifyClientSecret;
    private final @Getter String spotifyRefreshToken;

    public DefaultConfiguration() {
        super(Path.of("").toAbsolutePath().resolve("controller-config.yml"));

        this.version = rootNode.node("_version").getString("1");
        this.spotifyClientId = rootNode.node("spotify", "client-id").getString("SPOTIFY_CLIENT_ID");
        this.spotifyClientSecret = rootNode.node("spotify", "client-secret").getString("SPOTIFY_CLIENT_SECRET");
        this.spotifyRefreshToken = rootNode.node("spotify", "refresh-token").getString("SPOTIFY_REFRESH_TOKEN");
    }
}
