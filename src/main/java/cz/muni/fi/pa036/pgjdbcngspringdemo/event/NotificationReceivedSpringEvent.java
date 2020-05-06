package cz.muni.fi.pa036.pgjdbcngspringdemo.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class NotificationReceivedSpringEvent extends ApplicationEvent {
    private String channel;
    private String payload;

    public NotificationReceivedSpringEvent(Object source, String channel, String payload) {
        super(source);
        this.channel = channel;
        this.payload = payload;
    }
}
