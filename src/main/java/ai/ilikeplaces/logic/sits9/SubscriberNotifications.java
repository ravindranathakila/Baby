package ai.ilikeplaces.logic.sits9;

import ai.hbase.HBaseCrudService;
import ai.hbase.RowResponse;
import ai.ilikeplaces.entities.GeohashSubscriber;
import ai.ilikeplaces.util.AbstractSLBCallbacks;
import ai.ilikeplaces.util.Loggers;
import ai.ilikeplaces.util.SmartLogger;
import ai.scribble.License;
import com.google.gson.Gson;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Apr 29, 2010
 * Time: 4:10:12 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Stateless
public class SubscriberNotifications extends AbstractSLBCallbacks implements SubscriberNotificationsRemote {
    @Resource
    private TimerService timerService;

    @Override
    public void startTimer() {
        final SmartLogger sl = SmartLogger.start(Loggers.LEVEL.INFO, Loggers.CODE_MEMC +
                "HELLO, STARTING " + this.getClass().getSimpleName() + " TIMER  WITH TIMER INTERVAL:" + 5000,
                60000,
                null,
                true);
        timerService.createTimer(0, 5000, null);
        sl.complete(Loggers.LEVEL.INFO, Loggers.DONE);
    }

    @Timeout
    synchronized public void timeout(final Timer timer) {
        final HBaseCrudService<GeohashSubscriber> _geohashSubscriberHBaseCrudService = new HBaseCrudService<GeohashSubscriber>();
        final HBaseCrudService<GeohashSubscriber>.Scanner _scanner = _geohashSubscriberHBaseCrudService.scan(new GeohashSubscriber(), 1).returnValueBadly();

        while (_scanner.getNewValue() != null) {
            final String _newValue = _scanner.getNewValue();
            Loggers.debug("Scanned value:" + _newValue);
            final RowResponse _rowResponse = new Gson().fromJson(_newValue, RowResponse.class);
            Loggers.debug("Scanned as GSON:" + _rowResponse.toString());
            _geohashSubscriberHBaseCrudService.scan(new GeohashSubscriber(), _scanner);
        }

        Loggers.debug("Completed scanner");

    }
}

