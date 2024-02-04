package me.ghaxz.supplier;

import me.ghaxz.interfaces.ArgParser;
import me.ghaxz.store.ConfigFile;
import me.ghaxz.store.ServerConfig;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;

/*
Responsible for downloading and storing jars from the API (https://serverjars.com/)
 */
public class JarDownloader {
    public static void downloadServerConfig(ServerConfig config) throws IOException {
        URL url = new URL("https://serverjars.com/api/fetchJar/" + config.getType().getApiURL() + "/" + config.getVersion().getVersion() + "/");

        Files.createDirectory(Paths.get(config.getStorageDirectory() +
                FileSystems.getDefault().getSeparator() +
                config.getConfigName()));

        String outputPath = config.getStorageDirectory() +
                        FileSystems.getDefault().getSeparator() +
                        config.getConfigName() +
                        FileSystems.getDefault().getSeparator() +
                        config.getVersion().getJarFilename();

        System.out.println(url);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);

        try (InputStream inputStream = connection.getInputStream();
             OutputStream outputStream = new FileOutputStream(outputPath)) {

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
    }
}
