package me.udnek.coreu.resourcepack.host;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import me.udnek.coreu.CoreU;
import me.udnek.coreu.util.LogUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class ResourcePackHost implements HttpHandler {

    public static final int PORT = 25564;
    public static final String NAME = "resourcepack";

    public static @NotNull Path getFolderPath(){
        Path path = CoreU.getInstance().getDataPath().toAbsolutePath().resolve(NAME);
        path.toFile().mkdir();
        return path;
    }

    public static @NotNull Path getFilePath(){
        return CoreU.getInstance().getDataPath().resolve(NAME + ".zip");
    }

    public void start(){
        if (!Files.exists(getFilePath())) LogUtils.pluginWarning("Resourcepack was not generated! Use /resourcepack");

        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
            server.createContext("/", this);
            server.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        byte[] fileBytes = Files.readAllBytes(getFilePath());
        exchange.getResponseHeaders().set("Content-Type", "application/zip");
        exchange.getResponseHeaders().set(
                "Content-Disposition", "attachment; filename*=UTF-8''" + URLEncoder.encode(getFilePath().getFileName().toString(), StandardCharsets.UTF_8));
        exchange.sendResponseHeaders(200, fileBytes.length);
        OutputStream stream = exchange.getResponseBody();
        stream.write(fileBytes);
    }
}
