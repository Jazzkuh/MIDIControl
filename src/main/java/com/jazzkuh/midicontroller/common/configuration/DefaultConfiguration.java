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
        this.spotifyClientId = rootNode.node("spotify", "client-id").getString(SecretValues.SPOTIFY_CLIENT_ID.getValue());
        this.spotifyClientSecret = rootNode.node("spotify", "client-secret").getString(SecretValues.SPOTIFY_CLIENT_SECRET.getValue());
        this.spotifyRefreshToken = rootNode.node("spotify", "refresh-token").getString(SecretValues.SPOTIFY_REFRESH_TOKEN.getValue());
    }
}
