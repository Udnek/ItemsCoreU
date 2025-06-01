package me.udnek.coreu.resourcepack.host;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ResourcePackUtils {

    public static void zipFolder(@NotNull Path sourcePath, @NotNull String zipFilePath) {
        try {
            ZipOutputStream stream = new ZipOutputStream(new FileOutputStream(zipFilePath));
            Files.walk(sourcePath).filter(path -> !Files.isDirectory(path))
                    .forEach(path -> {
                        try {
                            stream.putNextEntry(new ZipEntry(sourcePath.relativize(path).toString()));
                            Files.copy(path, stream);
                            stream.closeEntry();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        } catch (IOException e){
            throw new RuntimeException(e);
        }

    }

    public static @NotNull String calculateSHA(@NotNull File file) {
        try (FileInputStream fis = new FileInputStream(file)){
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead);
            }

            return bytesToHex(digest.digest());
        } catch (NoSuchAlgorithmException | IOException e) { throw new RuntimeException(e); }
    }

    private static @NotNull String bytesToHex(byte @NotNull [] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }
}
