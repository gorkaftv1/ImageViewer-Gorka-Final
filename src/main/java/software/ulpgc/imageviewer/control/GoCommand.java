package software.ulpgc.imageviewer.control;

import software.ulpgc.imageviewer.io.FileImageLoader;
import software.ulpgc.imageviewer.model.Image;
import software.ulpgc.imageviewer.presenter.ImagePresenter;
import software.ulpgc.imageviewer.view.RouteField;

import java.io.File;

public class GoCommand implements Command{
    private final RouteField field;
    private final ImagePresenter presenter;
    private String currentFile;

    public GoCommand(RouteField field, ImagePresenter presenter, String currentFile) {
        this.presenter = presenter;
        this.field = field;
        this.currentFile = currentFile;
    }

    @Override
    public void execute() {
        if (field.getText() == null || field.getText().isEmpty())return;
        String newRoute = field.getText();
        if (currentFile.equals(newRoute))return;
        currentFile = newRoute;
        Image load = new FileImageLoader(new File(newRoute)).load();
        if (load == null) return;
        presenter.show(load);
    }
}
