package me.udnek.coreu.resourcepack.host;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class RpUtils {

    public static void zipFolder(@NotNull Path sourcePath, @NotNull Path zipFilePath) {
        try (ZipOutputStream stream = new ZipOutputStream(new FileOutputStream(zipFilePath.toFile()))) {
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

    public static @NotNull String calculateSHA(@NotNull Path folderPath) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            List<Path> filePaths = new ArrayList<>();
            try (Stream<Path> walk = Files.walk(Paths.get(folderPath.toUri()))) {
                walk.filter(Files::isRegularFile).sorted().forEach(filePaths::add);
            }

            for (Path filePath : filePaths) updateDigest(digest, filePath, folderPath);
            return bytesToHex(digest.digest());

        } catch (NoSuchAlgorithmException | IOException e) {
            throw new RuntimeException(e);
        }



    }

    private static void updateDigest(@NotNull MessageDigest digest, @NotNull Path filePath, @NotNull Path basePath) {
        String relativePath = Paths.get(basePath.toUri()).relativize(filePath).toString();
        digest.update(relativePath.getBytes());

        try (InputStream is = Files.newInputStream(filePath);
             BufferedInputStream bis = new BufferedInputStream(is)) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = bis.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static @NotNull String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = String.format("%02x", b);
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
