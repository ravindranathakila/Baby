package ai.ilikeplaces;

import ai.reaver.Return;
import ai.reaver.ReturnImpl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * Created by IntelliJ IDEA.
 * User: Ravindranath Akila
 * Date: 11/13/11
 * Time: 7:19 PM
 */
public class Main {
    public static void main(final String args[]) {
        System.out.println("################ STARTING GENERAL APPLICATION TESTER ################");
        new Main().run();
    }

    public void run() {

        GsonBuilder gb = new GsonBuilder();
        gb.registerTypeAdapter(Return.class, new ReturnInstanceCreator());

        Gson gson = gb.create();


        Return<String> myStrings = new ReturnImpl<String>("Hi", "Successful");

        Type listType = new TypeToken<Return<String>>() {
        }.getType();

        final String json = gson.toJson(myStrings, listType);

        System.out.println(json);

        gson.fromJson(json, listType);

    }

    public class ReturnInstanceCreator implements InstanceCreator<Return<?>> {

        @Override
        public Return<?> createInstance(Type type) {
            return new ReturnImpl<Object>("Bye", "Failed");
        }
    }
}
