package helper;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonIO {
    private static JsonIO INSTANCE;
    private final Gson gson;

    private JsonIO() {
        gson = new GsonBuilder()
        		.setPrettyPrinting()
        		.disableHtmlEscaping()
				.registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
        		.create();
    }

    public static JsonIO getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new JsonIO();
        }
        return INSTANCE;
    }

    public <T> void writeToJson(T obj, String filePath) throws IOException {
        FileWriter writer = new FileWriter(filePath);
        gson.toJson(obj, writer);
        writer.close();
    }

    public <T> T readFromJson(String filePath, Type typeOfT) throws FileNotFoundException {
        T obj = null;
        FileReader reader = new FileReader(filePath);
        obj = gson.fromJson(reader, typeOfT);
        return obj;
    }
    
    
    public static void clearData(String filePath) throws IOException {
    	FileWriter writer = new FileWriter(filePath);
        writer.write("");
        writer.close();
    }
}