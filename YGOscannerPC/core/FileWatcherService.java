package YGOscannerPC.core;

import java.io.IOException;
import java.nio.file.*;

public class FileWatcherService implements Runnable {

    private final Path folder;

    public FileWatcherService(String folderPath) {
        this.folder = Paths.get(folderPath);
    }

    @Override
    public void run() {
        try (WatchService watchService = FileSystems.getDefault().newWatchService()) {

            folder.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);

            while (true) {
                WatchKey key = watchService.take();

                for (WatchEvent<?> event : key.pollEvents()) {
                    Path file = folder.resolve((Path) event.context());
                    JsonImporter.importFile(file.toString());
                }

                key.reset();
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}