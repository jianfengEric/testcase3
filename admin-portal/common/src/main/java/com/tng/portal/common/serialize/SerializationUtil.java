package com.tng.portal.common.serialize;

import java.io.*;

/**
 * @author Anna_Popova
 */

public class SerializationUtil {
	
	private SerializationUtil(){}

    public static <T> byte[] serialize(T obj) throws IOException {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        ObjectOutputStream o = new ObjectOutputStream(b);
        o.writeObject(obj);
        return b.toByteArray();
    }

    public static <T> T deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream b = new ByteArrayInputStream(bytes);
        ObjectInputStream o = new ObjectInputStream(b);
        return (T) o.readObject();
    }
}
