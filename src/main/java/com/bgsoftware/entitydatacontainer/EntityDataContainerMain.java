package com.bgsoftware.entitydatacontainer;

import java.lang.instrument.Instrumentation;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class EntityDataContainerMain {

    private static final Logger logger = createLogger();

    public static void premain(String agentArgs, Instrumentation instrumentation) {
        try {
            long startTime = System.currentTimeMillis();
            logger.info("Starting EntityDataContainer...");

            instrumentation.addTransformer(new EntityDataContainerTransformer());

            long endTime = System.currentTimeMillis();

            logger.info(String.format("EntityDataContainer was successfully done (Took %dms)!", endTime - startTime));
        } catch (Exception error) {
            logger.warning("An unexpected error occurred while running EntityDataContainer:");
            error.printStackTrace();
        }
    }

    private static Logger createLogger() {
        Logger logger = Logger.getLogger("EntityDataContainer");
        logger.setUseParentHandlers(false);

        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new SimpleFormatter() {
            private static final String LOG_FORMAT = "[%1$tT %2$s]: %3$s %n";

            @Override
            public synchronized String format(LogRecord lr) {
                return String.format(LOG_FORMAT,
                        new Date(lr.getMillis()),
                        lr.getLevel().getLocalizedName(),
                        lr.getMessage()
                );
            }
        });
        logger.addHandler(handler);

        return logger;
    }

}
