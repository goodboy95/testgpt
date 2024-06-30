package com.seekerhut.utils;

import java.io.*;

public class Serializer {
	public static byte[] serialize(Object obj) throws IOException {
		try (var bos = new ByteArrayOutputStream()) {
			try (var oos = new ObjectOutputStream(bos)) {
				oos.writeObject(obj);
				var result = bos.toByteArray();
				return result;
			}
		}
	}

	public static <T> T deserialize(byte[] byteArr, Class<T> clazz) throws IOException, ClassNotFoundException {
		try (var bis = new ByteArrayInputStream(byteArr)) {
			try (var ois = new ObjectInputStream(bis)) {
				var rawResult = ois.readObject();
				var result = (T)rawResult;
				return result;
			}
		}
	}
}