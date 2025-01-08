package software.ulpgc.imageviewer.application;

import software.ulpgc.imageviewer.io.ImageDeserializer;
import software.ulpgc.imageviewer.model.Image;
import software.ulpgc.imageviewer.view.ViewPort;
import software.ulpgc.imageviewer.view.ImageDisplay;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.*;
import java.util.List;

public class SwingImageDisplay extends JPanel implements ImageDisplay {
    private final ImageDeserializer deserializer;
    private final List<ImageShifted> imagesOnScreen = Collections.synchronizedList(new ArrayList<>());
    private int initShift;
    private Released released = Released.Null;
    private Shift shift = Shift.Null;
    private Image current;
    private final Map<Image, java.awt.Image> imageCache = new HashMap<>();

    public SwingImageDisplay(ImageDeserializer deserializer) {
        this.deserializer = deserializer;
        this.addMouseListener(mouseListener());
        this.addMouseMotionListener(mouseMotionListener());
    }

    private MouseListener mouseListener() {
        return new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {}

            @Override
            public void mousePressed(MouseEvent e) {
                initShift = e.getX();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                released.offset(e.getX() - initShift);
            }

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) {}
        };
    }

    private MouseMotionListener mouseMotionListener() {
        return new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int offset = e.getX() - initShift;
                shift.offset(offset);
            }

            @Override
            public void mouseMoved(MouseEvent e) {}
        };
    }

    @Override
    public Image currentImage() {
        return current;
    }

    public void clear() {
        synchronized (imagesOnScreen) {
            imagesOnScreen.clear();
        }
        repaint();
    }

    @Override
    public void show(Image image, int offset) {
        synchronized (imagesOnScreen) {
            while (imagesOnScreen.size() > 2) imagesOnScreen.removeFirst();
            imagesOnScreen.add(new ImageShifted(image, offset));
        }
        repaint();
    }

    @Override
    public void on(Shift shift) {
        this.shift = shift != null ? shift : Shift.Null;
    }

    @Override
    public void on(Released released) {
        this.released = released != null ? released : Released.Null;
    }

    @Override
    public void paint(Graphics g) {
        Rectangle clip = g.getClipBounds();
        g.setColor(Color.WHITE);
        g.fillRect(clip.x, clip.y, clip.width, clip.height);

        synchronized (imagesOnScreen) {
            for (ImageShifted shifted : imagesOnScreen) {
                if (Math.abs(shifted.offset()) < getWidth()) {
                    adaptAndDrawImage(g, shifted);
                }
            }
        }
    }

    private void adaptAndDrawImage(Graphics g, ImageShifted shifted) {
        java.awt.Image adaptedImage = getCachedImage(shifted.image());
        ViewPort viewPort = viewPortOf(adaptedImage);
        g.drawImage(
                adaptedImage, viewPort.x() + shifted.offset(), viewPort.y(),
                viewPort.width(), viewPort.height(),
                Color.BLACK, this
        );
    }

    private java.awt.Image getCachedImage(Image image) {
        return imageCache.computeIfAbsent(image, img -> {
            try {
                return (java.awt.Image) deserializer.desearilize(img.content());
            } catch (Exception e) {
                return null;
            }
        });
    }

    private ViewPort viewPortOf(java.awt.Image deserialized) {
        return ViewPort.ofSize(this.getWidth(), this.getHeight())
                .fit(deserialized.getWidth(null), deserialized.getHeight(null));
    }

    private record ImageShifted(Image image, int offset) {}
}
