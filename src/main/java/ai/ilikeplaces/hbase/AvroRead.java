package ai.ilikeplaces.hbase;

import ai.ilikeplaces.entities.Subscriber;
import org.apache.avro.Schema;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.reflect.ReflectData;
import org.apache.avro.reflect.ReflectDatumReader;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA Ultimate.
 * User: http://www.ilikeplaces.com
 * Date: 26/1/13
 * Time: 7:52 PM
 */
public class AvroRead {

    public static void readData(final Subscriber subscriber) throws IOException {
        final Schema schema = ReflectData.get().getSchema(Subscriber.class);
        final ReflectDatumReader<Subscriber> reflectDatumReader = new ReflectDatumReader<Subscriber>(schema);


        final HBaseAdmin hBaseAdmin = new HBaseAdmin(new Configuration());
        final boolean _subscriber = hBaseAdmin.tableExists(AvroCreate.SUBSCRIBER);

        if (_subscriber) {

            final HTable _hTable = new HTable(AvroCreate.SUBSCRIBER);
            final Get _get = new Get(Bytes.toBytes(subscriber.getEmailId().toString()));
            final Result _result = _hTable.get(_get);


            final HColumnDescriptor _family = new HColumnDescriptor("value");


            final List<KeyValue> _column = _result.getColumn(_family.getName(), _family.getName());
            for (final KeyValue _keyValue : _column) {
                final Decoder _decoder = DecoderFactory.get().binaryDecoder(_keyValue.getValue(), null);
                final Subscriber readSubscriber = reflectDatumReader.read(null, _decoder);
                System.out.println("Read data from HBase:" + readSubscriber.toString());
            }

        }


    }
}
