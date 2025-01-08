package software.ulpgc.imageviewer.io;

import software.ulpgc.imageviewer.model.Image;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

public class FileImageLoader implements ImageLoader {
    private final File[] files;

    public FileImageLoader(File folder) {
        this.files = requireNonNull(folder.listFiles(ofTypeImage()));
    }

    @Override
    public Image load() {
        return imageAt(0);
    }

    private Image imageAt(int index) {
        return new Image() {
            @Override
            public byte[] content() {
                try {
                    return Files.readAllBytes(currentFile().toPath());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public Image next() {
                return imageAt(previousIndex());
            }

            @Override
            public Image previous() {
                return imageAt(nextIndex());
            }

            private File currentFile() {
                return files[index];
            }

            private int previousIndex() {
                return (index + 1) % files.length;
            }

            private int nextIndex() {
                return index > 0 ? index - 1 : files.length - 1;
            }
        };
    }

    private FileFilter ofTypeImage() {
        return f -> validImageExtensions()
                .anyMatch(e-> fileHasValidExtension(f, e));
    }

    private Stream<String> validImageExtensions() {
        return Arrays.stream(Image.Format.values())
                .map(s->s.name().toLowerCase());

    }

    private static boolean fileHasValidExtension(File f, String e) {
        return f.getName().toLowerCase().endsWith(e);
    }
}
