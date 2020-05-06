package cz.muni.fi.pa036.pgjdbcngspringdemo.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "app.database")
public class DatabaseProperties {
    private String host;
    private Integer port;
    private String database;
    private String user;
    private String password;
}
