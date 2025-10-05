package logic.utils.serialization;

import java.io.*;

public class SerializationManager {
    public static void save(Object obj, String path) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path + ".ser"))) {
            out.writeObject(obj);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T load(String path, Class<T> clazz) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(path + ".ser"))) {
            Object obj = in.readObject();
            return clazz.cast(obj);
        }
    }
}
