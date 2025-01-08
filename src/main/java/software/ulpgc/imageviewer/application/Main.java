package software.ulpgc.imageviewer.application;

import software.ulpgc.imageviewer.control.GoCommand;
import software.ulpgc.imageviewer.io.FileImageLoader;
import software.ulpgc.imageviewer.model.Image;
import software.ulpgc.imageviewer.presenter.ImagePresenter;

import java.io.File;

public class Main {

    public static final String FirstFolder = "src/main/resources/LasPlayitas";

    public static void main(String[] args) {
        MainFrame frame = new MainFrame();
        frame.getImageDisplay().show(image(), 0);
        ImagePresenter presenter = new ImagePresenter((SwingImageDisplay) frame.getImageDisplay());
        frame.add("go", new GoCommand(frame.field(), presenter, FirstFolder));
        presenter.show(image());
        frame.setVisible(true);
    }
    private static Image image() {
        return new FileImageLoader(new File(FirstFolder)).load();
    }
}