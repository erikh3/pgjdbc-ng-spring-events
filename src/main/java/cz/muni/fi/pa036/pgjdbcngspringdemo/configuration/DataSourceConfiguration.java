package cz.muni.fi.pa036.pgjdbcngspringdemo.configuration;

import com.impossibl.postgres.jdbc.PGDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceConfiguration {
    // use custom datasource instead of Hikari
    @Bean
    public PGDataSource getDataSource(DatabaseProperties databaseProperties) {
        PGDataSource dataSource = new PGDataSource();

        dataSource.setHost(databaseProperties.getHost());
        dataSource.setPort(databaseProperties.getPort());
        dataSource.setDatabaseName(databaseProperties.getDatabase());
        dataSource.setUser(databaseProperties.getUser());
        dataSource.setPassword(databaseProperties.getPassword());
        // disable cleanup for listen connection
        dataSource.setHousekeeper(false);

        return dataSource;
    }
}
