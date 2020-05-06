package cz.muni.fi.pa036.pgjdbcngspringdemo;

import com.impossibl.postgres.api.jdbc.PGConnection;
import com.impossibl.postgres.api.jdbc.PGNotificationListener;
import cz.muni.fi.pa036.pgjdbcngspringdemo.event.NotificationReceivedSpringEvent;
import cz.muni.fi.pa036.pgjdbcngspringdemo.event.NotificationSendSpringEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

@Slf4j
@Component
@RequiredArgsConstructor
public class PGNotificationManager {
    private final DataSource dataSource;
    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * Sends NOTIFY SQL query to database
     *
     * @param channel the channel to use
     * @param payload the payload text
     * @throws SQLException if any exception occurs
     */
    public void notify(String channel, String payload) throws SQLException {
        String channelEscaped = channel.replace(",", "").replace(";", "");
        String payloadEscaped = payload.replace("'", "''");
        try (PreparedStatement statement = DataSourceUtils.getConnection(dataSource)
                .prepareStatement("NOTIFY " + channelEscaped + " , '" + payloadEscaped + "'")) {  // ? substitution won't work
            statement.executeUpdate();
        }
    }

    @Async
    @EventListener
    public void handleNotificationSend(NotificationSendSpringEvent event) {
        try {
            notify(event.getChannel(), event.getPayload());
        } catch (SQLException e) {
            log.error("Error in notify", e);
        }
    }

    @Async
    @EventListener
    public void onStart(ContextRefreshedEvent event) {
        try {
            PGConnection connection = DataSourceUtils.getConnection(dataSource).unwrap(PGConnection.class);

            connection.addNotificationListener(createPGListener());

            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate("LISTEN my_channel_name");
            }

            // keep this thread alive
            while (true) {
                Thread.sleep(1000);
                if (Thread.interrupted()) {
                    return;
                }
            }
        } catch (SQLException e) {
            log.error("Error in listener", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // bridge between pg listen/notify & spring events
    private PGNotificationListener createPGListener() {
        return new PGNotificationListener() {
            @Override
            public void notification(int processId, String channelName, String payload) {
                log.debug("PG listener new data on " + channelName + ", " + payload);
                NotificationReceivedSpringEvent event = new NotificationReceivedSpringEvent(this, channelName, payload);
                applicationEventPublisher.publishEvent(event);
            }
        };
    }
}
