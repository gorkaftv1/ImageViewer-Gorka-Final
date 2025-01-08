package software.ulpgc.imageviewer.presenter;

import software.ulpgc.imageviewer.view.ImageDisplay;
import software.ulpgc.imageviewer.model.Image;
import software.ulpgc.imageviewer.application.SwingImageDisplay;

public class ImagePresenter {
    private Image current;
    private final SwingImageDisplay display;

    public ImagePresenter(SwingImageDisplay display) {
        this.display = display;
        this.current = display.currentImage();
        this.display.on((ImageDisplay.Shift) this::shift);
        this.display.on((ImageDisplay.Released) this::released);
    }

    private void shift(int offset) {
        display.show(current, offset);
        if (offset > 0) {
            display.show(current.previous(), offset - display.getWidth());
        } else {
            display.show(current.next(), display.getWidth() + offset);
        }
    }

    public void released(int offset) {
        if (Math.abs(offset) >= display.getWidth() / 2) {
            current = offset > 0 ? current.previous() : current.next();
        }
        repaint();
    }

    private void repaint() {
        display.clear();
        display.show(current, 0);
        display.show(current.previous(), -display.getWidth());
        display.show(current.next(), display.getWidth());
        display.repaint();
    }


    public void show(Image image){
        current = image;
        display.show(image, 0);
    }
}
