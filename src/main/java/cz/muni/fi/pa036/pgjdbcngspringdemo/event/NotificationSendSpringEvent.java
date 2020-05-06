package cz.muni.fi.pa036.pgjdbcngspringdemo.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class NotificationSendSpringEvent extends ApplicationEvent {
    private String channel;
    private String payload;

    public NotificationSendSpringEvent(Object source, String channel, String payload) {
        super(source);
        this.channel = channel;
        this.payload = payload;
    }
}
