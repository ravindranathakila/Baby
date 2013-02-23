package ai.ilikeplaces.logic.sits9;

import ai.hbase.Cell;
import ai.hbase.HBaseCrudService;
import ai.hbase.Row;
import ai.hbase.RowResponse;
import ai.ilikeplaces.entities.GeohashSubscriber;
import ai.ilikeplaces.logic.mail.SendMailLocal;
import ai.ilikeplaces.rbs.RBGet;
import ai.ilikeplaces.util.AbstractSLBCallbacks;
import ai.ilikeplaces.util.HTMLDocParser;
import ai.ilikeplaces.util.Loggers;
import ai.ilikeplaces.util.SmartLogger;
import ai.scribble.License;
import com.google.gson.Gson;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import sun.misc.BASE64Decoder;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.xml.transform.TransformerException;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: Apr 29, 2010
 * Time: 4:10:12 PM
 */

@License(content = "This code is licensed under GNU AFFERO GENERAL PUBLIC LICENSE Version 3")
@Stateless
public class SubscriberNotifications extends AbstractSLBCallbacks implements SubscriberNotificationsRemote {
    private static final String SUBSCRIBER_EMAIL = "ai/ilikeplaces/logic/Listeners/widgets/subscribe/SubscriberEventEmail.html";

    @Resource
    private TimerService timerService;

    @EJB
    private SendMailLocal sendMailLocal;

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
    synchronized public void timeout(final Timer timer) throws IOException, SAXException, TransformerException {
        final HBaseCrudService<GeohashSubscriber> _geohashSubscriberHBaseCrudService = new HBaseCrudService<GeohashSubscriber>();
        final HBaseCrudService<GeohashSubscriber>.Scanner _scanner = _geohashSubscriberHBaseCrudService.scan(new GeohashSubscriber(), 1).returnValueBadly();

        while (_scanner.getNewValue() != null) {
            final String _newValue = _scanner.getNewValue();
            Loggers.debug("Scanned value:" + _newValue);
            final RowResponse _rowResponse = new Gson().fromJson(_newValue, RowResponse.class);
            Loggers.debug("Scanned as GSON:" + _rowResponse.toString());

            for (final Row _row : _rowResponse.Row) {

                final BASE64Decoder _base64DecoderRowKey = new BASE64Decoder();
                final byte[] _bytes = _base64DecoderRowKey.decodeBuffer(_row.key);
                final String rowKey = new String(_bytes);
                Loggers.debug("Decoded row key:" + rowKey);

                for (final Cell _cell : _row.Cell) {
                    final BASE64Decoder _base64DecoderValue = new BASE64Decoder();
                    final byte[] _valueBytes = _base64DecoderValue.decodeBuffer(_cell.$);
                    final String _cellAsString = new String(_valueBytes);
                    Loggers.debug("Cell as string:" + _cellAsString);
                    final GeohashSubscriber _geohashSubscriber = new GeohashSubscriber();

                    final DatumReader<GeohashSubscriber> _geohashSubscriberSpecificDatumReader = new SpecificDatumReader<GeohashSubscriber>(_geohashSubscriber.getSchema());
                    final BinaryDecoder _binaryDecoder = DecoderFactory.get().binaryDecoder(_valueBytes, null);
                    final GeohashSubscriber _read = _geohashSubscriberSpecificDatumReader.read(_geohashSubscriber, _binaryDecoder);
                    Loggers.debug("Decoded value avro:" + _read.toString());
                }


                final Document document = HTMLDocParser.getDocument(RBGet.getGlobalConfigKey("PAGEFILES") + SUBSCRIBER_EMAIL);


                final String _content = HTMLDocParser.convertNodeToHtml(HTMLDocParser.$("content", document));

            }


            _geohashSubscriberHBaseCrudService.scan(new GeohashSubscriber(), _scanner);
        }

        Loggers.debug("Completed scanner");

    }
}

