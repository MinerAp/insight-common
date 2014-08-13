package com.amshulman.insight.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import com.amshulman.insight.serialization.StorageMetadata;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SerializationUtil {

    public static byte[] serializeMetadata(StorageMetadata meta) throws IllegalStateException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);) {

            dataOutput.writeObject(meta);
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new IllegalStateException("Unable to serialize metadata", e);
        }
    }

    public static StorageMetadata deserializeMetadata(byte[] data) throws IllegalArgumentException {
        if (data == null) {
            return null;
        }

        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
             BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);) {

            return (StorageMetadata) dataInput.readObject();
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to deserialize metadata", e);
        }
    }
}
