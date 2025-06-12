package me.udnek.coreu.resourcepack.host;

import me.udnek.coreu.CoreU;
import me.udnek.coreu.resourcepack.RPInfo;
import me.udnek.coreu.serializabledata.SerializableDataManager;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;
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

    public static String calculateSHA(File file) {
        try (FileInputStream fis = new FileInputStream(file)){
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead);
            }
            return bytesToHex(digest.digest());
        } catch (NoSuchAlgorithmException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) hexString.append(String.format("%02x", b));
        return hexString.toString();
    }

    public static void updateServerProperties(@NotNull String checksum) throws IOException {
        RPInfo rpInfo = SerializableDataManager.read(new RPInfo(), CoreU.getInstance());

        Properties properties = new Properties();
        FileInputStream inStream = new FileInputStream("server.properties");
        properties.load(inStream);
        inStream.close();

        properties.setProperty("resource-pack", "http://" + rpInfo.ip + ":" + rpInfo.port + "/1");
        properties.setProperty("resource-pack-sha1", checksum);

        FileOutputStream fos = new FileOutputStream("server.properties");
        properties.store(fos, "pohui");
        fos.close();
    }
}
