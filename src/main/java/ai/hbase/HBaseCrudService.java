package ai.hbase;

import ai.ilikeplaces.util.Loggers;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.specific.SpecificRecord;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;

import java.io.*;

/**
 * Created with IntelliJ IDEA Ultimate.
 * User: http://www.ilikeplaces.com
 * Date: 16/2/13
 * Time: 5:06 PM
 */
public class HBaseCrudService<T extends SpecificRecord> {


    public T create(final RowKey rowKey, final T t) {
        try {
            System.out.println("Attempting to write:" + t.toString());

            final ByteArrayOutputStream _outputStream = new ByteArrayOutputStream();
            final BinaryEncoder _binaryEncoder = EncoderFactory.get().binaryEncoder(_outputStream, null);

            final DatumWriter<T> _datumWriter = new SpecificDatumWriter<T>(t.getSchema());
            _datumWriter.write(t, _binaryEncoder);
            _binaryEncoder.flush();

            final byte[] dataByteArray = _outputStream.toByteArray();


            final org.apache.commons.httpclient.HttpClient httpClient = new org.apache.commons.httpclient.HttpClient();

            //curl -H "Content-Type: application/octet-stream" --data '[Sample Data]' http://localhost:9090/Subscriber/samplerow1/value:value

            final PutMethod _putMethod = new PutMethod("http://localhost:9090/" + t.getClass().getSimpleName() + "/" + rowKey.getRowKey() + "/value:value");
            _putMethod.setRequestHeader("Content-Type", "application/octet-stream");
            _putMethod.setRequestEntity(new ByteArrayRequestEntity(dataByteArray));

            int statusCode = 0;
            try {
                statusCode = httpClient.executeMethod(_putMethod);
            } catch (final IOException e) {
                throw new RuntimeException(e);
            }

            if (statusCode != HttpStatus.SC_OK) {
                throw new RuntimeException(String.valueOf(statusCode));
            }
            InputStream inputStream = null;

            try {
                inputStream = _putMethod.getResponseBodyAsStream();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            final BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            String accumulator = "";
            try {
                while ((line = br.readLine()) != null) {
                    accumulator += line;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                br.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Loggers.debug(accumulator);
        } catch (IOException e) {
            Loggers.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        return null;
    }

    public T find(final T t, final Object id) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Scanner scan(final T t, final Scanner... scanner) {
        System.out.println("Attempting to scan:" + t.toString());

        Scanner returnVal = null;

        if (scanner.length == 0) {
            final org.apache.commons.httpclient.HttpClient httpClient = new org.apache.commons.httpclient.HttpClient();

            final String _uri = "http://localhost:9090/" + t.getClass().getSimpleName() + "/" + "scanner";
            Loggers.debug("Calling:" + _uri);
            final PutMethod _putMethod = new PutMethod(_uri);
            _putMethod.setRequestHeader("Content-Type", "text/xml");
            _putMethod.setRequestEntity(new StringRequestEntity("<Scanner batch=\"100\"/>"));


            int statusCode = 0;
            try {
                statusCode = httpClient.executeMethod(_putMethod);
            } catch (final IOException e) {
                throw new RuntimeException(e);
            }

            if (statusCode != HttpStatus.SC_OK && statusCode != HttpStatus.SC_CREATED) {
                throw new RuntimeException(String.valueOf("Http request ended up with status code:" + statusCode));
            }
            InputStream inputStream = null;

            try {
                inputStream = _putMethod.getResponseBodyAsStream();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            final BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            String accumulator = "";
            try {
                while ((line = br.readLine()) != null) {
                    accumulator += line;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                br.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Loggers.debug(accumulator);

            final Header _responseHeader = _putMethod.getResponseHeader("Location");
            final String _location = _responseHeader.getValue();

            returnVal = scan(t, new Scanner()/*Since varargs length is 0*/
                    .setNewScanUrl(_location)
                    .setNewValue(null));
        } else {

            final org.apache.commons.httpclient.HttpClient httpClient = new org.apache.commons.httpclient.HttpClient();

            Loggers.debug("Calling:" + scanner[0].getNewScanUrl());

            final GetMethod _getMethod = new GetMethod(scanner[0].getNewScanUrl());
            _getMethod.setRequestHeader("Content-Type", "text/xml");

            int statusCode = 0;
            try {
                statusCode = httpClient.executeMethod(_getMethod);
            } catch (final IOException e) {
                throw new RuntimeException(e);
            }

            if (statusCode != HttpStatus.SC_OK) {
                throw new RuntimeException(String.valueOf(statusCode));
            }
            InputStream inputStream = null;

            try {
                inputStream = _getMethod.getResponseBodyAsStream();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            final BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            String accumulator = "";
            try {
                while ((line = br.readLine()) != null) {
                    accumulator += line;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                br.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Loggers.debug(accumulator);

            final Header _responseHeader = _getMethod.getResponseHeader("Location");
            final String _location = _responseHeader.getValue();
            Loggers.debug(_location);

            returnVal = scanner[0]
                    .setNewScanUrl(_location)
                    .setNewValue(accumulator);
        }


        return returnVal;

    }

    public T findBadly(final Class typeOfEntity, final Object idByWhichToLookup) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public T update(final T t) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void delete(final Class type, final Object id) {
        throw new UnsupportedOperationException();
    }

    public class Scanner {
        private String newScanUrl;

        private String newValue;

        public String getNewValue() {
            return newValue;
        }

        public Scanner setNewValue(final String __newValue) {
            newValue = __newValue;
            return this;
        }

        public String getNewScanUrl() {
            return newScanUrl;
        }

        public Scanner setNewScanUrl(final String __newScanUrl) {
            newScanUrl = __newScanUrl;
            return this;
        }
    }
}
