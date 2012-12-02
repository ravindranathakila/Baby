package ai.ilikeplaces.entities.etc;

/**
 * Use with JPA entityManager.find
 * Use only for that :-)
 * <p/>
 * Created by IntelliJ IDEA.
 * User: ravindranathakila
 * Date: 7/22/12
 * Time: 11:15 AM
 */
public class Find<Class, ID> {

    /**
     * DO NOT, DO NOT MAKE THIS A NON-CONSTANT AS DURING RUNTIME IT MIGHT TRIGGER MILLIONS OF EXCEPTION OBJECTS!
     */
    public static final NullPointerException NULL_POINTER_EXCEPTION = new NullPointerException(
            "PARAMETERS TO " + Find.class.getName() + " CAN NEVER BE NULL. " +
                    "THIS IS A GENERAL ERROR AND WILL NOT TRACE FULLY DOWN TO THE INITIATING LINE OF CODE.");

    /**
     * THIS IS INVARIABLY THE ENTITY Class
     */
    public final Class type;

    /**
     * THIS IS INVARIABLY THE ENTITY'S PRIMARY KEY, AND PREFERABLY A BASIC TYPE
     */
    public final ID id;

    public Find(Class type, ID id) {
        if (type != null && id != null) {
            throw NULL_POINTER_EXCEPTION;
        }

        this.type = type;
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Find find = (Find) o;

        if (!id.equals(find.id)) return false;
        if (!type.equals(find.type)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + id.hashCode();
        return result;
    }
}
