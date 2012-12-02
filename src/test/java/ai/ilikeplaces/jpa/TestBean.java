package ai.ilikeplaces.jpa;

import ai.ilikeplaces.entities.*;

import javax.persistence.Transient;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: 2/6/11
 * Time: 9:45 PM
 */
public class TestBean implements Refreshable<TestBean> {

    @RefreshId("testCollection")
    List<String> testCollection;

    @RefreshId("nonCollection")
    String nonCollection = "";


    @RefreshId("testCollection")
    List<String> testSameAnnotatedValueCollection;

    @Transient
    final private static Refresh<TestBean> REFRESH = new Refresh<TestBean>();


    public List<String> getTestCollection() {
        return testCollection;
    }

    public void setTestCollection(List<String> testCollection) {
        this.testCollection = testCollection;
    }

    public String getNonCollection() {
        return nonCollection;
    }

    public void setNonCollection(String nonCollection) {
        this.nonCollection = nonCollection;
    }

    public List<String> getTestSameAnnotatedValueCollection() {
        return testSameAnnotatedValueCollection;
    }

    public void setTestSameAnnotatedValueCollection(List<String> testSameAnnotatedValueCollection) {
        this.testSameAnnotatedValueCollection = testSameAnnotatedValueCollection;
    }

    @Override
    public TestBean refresh(final RefreshSpec refreshSpec) throws RefreshException {
        REFRESH.refresh(this, refreshSpec);
        return this;
    }
}
