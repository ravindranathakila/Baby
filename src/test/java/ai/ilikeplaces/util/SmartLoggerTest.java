package ai.ilikeplaces.util;

import org.junit.Before;
import org.junit.Test;

/**
* Created by IntelliJ IDEA.
* User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
* Date: 5/14/11
* Time: 7:15 PM
*/
public class SmartLoggerTest {

    SmartLogger slHappy = null;

    @Before
    public void beforeSmartLoggerTest() {
        slHappy = SmartLogger.start(Loggers.LEVEL.DEBUG, "SL Happy", 10, "Starting SL Happy Logger", true);
    }

    @Test
    public void testSmartLoggerTest() {
        slHappy.l("Message 1");
        try{
            throw new Exception("Exception 1");
        } catch (final Exception e){
            slHappy.l(e);
        }
    }

    @Test
    public void afterSmartLoggerTest() {
        slHappy.complete("Ending SL Happy Logger");
    }
}
