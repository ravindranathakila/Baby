package ai.ilikeplaces.jpa;

import ai.ilikeplaces.util.jpa.RefreshSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: <a href="http://www.ilikeplaces.com"> http://www.ilikeplaces.com </a>
 * Date: 2/6/11
 * Time: 9:30 PM
 */
public class Refresh<T> implements Serializable{

    public static Logger LOGGER = LoggerFactory.getLogger(ai.ilikeplaces.util.jpa.Refresh.class.getClass().getCanonicalName());

    private static final String ERROR_WHILE_REFRESHING_COLLECTION = "ERROR WHILE REFRESHING LAZY FIELD";
    private static final String COUNLDN_T_FIND_IN_THE_BEAN = "COULDN'T FIND IN THE BEAN: ";
    private static final String THE_JAVA_UTIL_COLLECTION_FIELD_NAMED = " THE FIELD NAMED: ";
    private static final String GIVEN_FIELD_VALUE_IS_NULL = "GIVEN FIELDS VALUE IS NULL. FIELD IDENTIFIER:";
    private static final String GIVEN_FIELD_IS_NOT_OF_TYPE_JAVA_UTIL_COLLECTION = "GIVEN FIELD IS NOT OF TYPE type java.util.Collection!";
    private static final String RECEIVED_BEAN = "Received bean:{}";
    private static final String RECEIVED_SPEC = "Received spec:{}";
    private static final String MULTIPLE_FIELDS_HAVE_THE_SAME_FIELD_NAME_ANNOTATION_VALUE = "MULTIPLE FIELDS HAVE THE SAME @RefreshId ANNOTATION VALUE:";
    private static final String GET = "get";

    public T refresh(final T bean, final RefreshSpec refreshSpec) throws RefreshException {
        LOGGER.trace(RECEIVED_BEAN, bean);
        LOGGER.trace(RECEIVED_SPEC, refreshSpec);

        final Field[] fields = bean.getClass().getFields();

        Annotation Annotation;//To be reused
        boolean found = false;//To be reused
        Object finallyFetchedObject;//To be reused
        Method getter;//To be reused

        for (final String spec : refreshSpec.fields) {
            found = false;
            for (final Field field : fields) {
                Annotation = field.getAnnotation(RefreshId.class);
                if (Annotation != null) {
                    if (((RefreshId) Annotation).value().equals(spec)) {
                        try {
                            getter = bean.getClass().getMethod(GET + ((Character) spec.charAt(0)).toString().toUpperCase() + spec.substring(1, spec.length()));
                            //finallyFetchedObject = field.get(bean);
                            finallyFetchedObject = getter.invoke(bean);
                            if (finallyFetchedObject == null) {
                                throw new RefreshException(GIVEN_FIELD_VALUE_IS_NULL + spec);
                            } else if (!(finallyFetchedObject instanceof Collection)) {
                                finallyFetchedObject.equals(finallyFetchedObject);//This, within this persistence context, will refresh this lazily initialized field
                                //throw new RefreshException(GIVEN_FIELD_IS_NOT_OF_TYPE_JAVA_UTIL_COLLECTION);
                                found = true;
                            } else {
                                ((Collection) finallyFetchedObject).size();//This, within this persistence context, will refresh this lazily initialized Collection
                                if (found) {
                                    throw new RefreshException(MULTIPLE_FIELDS_HAVE_THE_SAME_FIELD_NAME_ANNOTATION_VALUE + spec);
                                } else {
                                    found = true;
                                }
                            }
                        } catch (final Exception e) {
                            throw new RefreshException(ERROR_WHILE_REFRESHING_COLLECTION, e);

                        }
                    }
                }
            }
            if (!found) {
                throw new RefreshException(COUNLDN_T_FIND_IN_THE_BEAN + bean.getClass().getCanonicalName() + THE_JAVA_UTIL_COLLECTION_FIELD_NAMED + spec);
            }
        }
        return bean;
    }
}
