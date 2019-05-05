package ee8security;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class ClockProducer {

    private static final Logger log = Logger.getLogger(ClockProducer.class.getName());

    private static final Clock CLOCK;

    static {
        String prop = System.getProperty("ee8security.fixedUnixTime");
        CLOCK = prop == null
                ? Clock.systemUTC()
                : Clock.fixed(Instant.ofEpochSecond(Long.parseLong(prop)), ZoneOffset.UTC);
    }

    public void postConstruct(@Observes @Initialized(ApplicationScoped.class) Object o) {
        log.log(Level.INFO, "The clock instance to use in this deployment: {0}", CLOCK);
    }

    @Produces
    Clock clock() {
        return CLOCK;
    }
}
