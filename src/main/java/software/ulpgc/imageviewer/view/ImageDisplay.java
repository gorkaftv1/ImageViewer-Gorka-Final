package software.ulpgc.imageviewer.view;

import software.ulpgc.imageviewer.model.Image;

public interface ImageDisplay {
    Image currentImage();

    void show(Image image, int offset);
    void on(Shift shift);
    void on(Released released);

    interface Shift{
        Shift Null = offset -> {};
        void offset(int offset);
    }
    interface Released{
        Released Null = offset -> {};
        void offset(int offset);
    }
}
