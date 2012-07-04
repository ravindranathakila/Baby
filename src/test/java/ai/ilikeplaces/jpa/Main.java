package ai.ilikeplaces.jpa;

import ai.ilikeplaces.util.jpa.RefreshException;
import ai.ilikeplaces.util.jpa.RefreshSpec;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: 2/6/11
 * Time: 9:45 PM
 */
public class Main {

    public static void main(final String args[]) throws RefreshException {

        //Lets try the normal way
        try {

            final RefreshSpec refreshSpec = new RefreshSpec("testCollection");

            final TestBean testBean = new TestBean();

            final List<String> testCollection = new ArrayList<String>(2);

            testCollection.add("sam");
            testCollection.add("walter");

            testBean.setTestCollection(testCollection);
            testBean.setTestSameAnnotatedValueCollection(testCollection);

            testBean.refresh(refreshSpec);
        } catch (final Throwable t) {
            t.printStackTrace(System.err);
        }

        //Lets try a wrong name
        try {

            final RefreshSpec refreshSpec = new RefreshSpec("testCollectio_wrong_name");

            final TestBean testBean = new TestBean();

            final List<String> testCollection = new ArrayList<String>(2);

            testCollection.add("sam");
            testCollection.add("walter");

            testBean.setTestCollection(testCollection);
            testBean.setTestSameAnnotatedValueCollection(testCollection);

            testBean.refresh(refreshSpec);
        } catch (final Throwable t) {
            t.printStackTrace(System.err);
        }

        //Lets try a non collection
        try {

            final RefreshSpec refreshSpec = new RefreshSpec("testCollection", "nonCollection");

            final TestBean testBean = new TestBean();

            final List<String> testCollection = new ArrayList<String>(2);

            testCollection.add("sam");
            testCollection.add("walter");

            testBean.setTestCollection(testCollection);
            testBean.setTestSameAnnotatedValueCollection(testCollection);

            testBean.refresh(refreshSpec);
        } catch (final Throwable t) {
            t.printStackTrace(System.err);
        }


    }
}
