package software.ulpgc.imageviewer.application;

import software.ulpgc.imageviewer.control.Command;
import software.ulpgc.imageviewer.view.ImageDisplay;
import software.ulpgc.imageviewer.view.RouteField;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class MainFrame extends JFrame {
    private ImageDisplay imageDisplay;
    private final Map<String, Command> commands = new HashMap<>();
    private RouteField field;

    public MainFrame()  {
        this.setTitle("Image Viewer");
        this.setSize(800,600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.add(createImageDisplay());
        this.add(BorderLayout.SOUTH, toolbar());
    }

    private Component toolbar() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.setBackground(Color.WHITE);
        panel.add(goButton());
        panel.add(folderInput());
        return panel;
    }

    private JTextField folderInput() {
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(200, 30));
        field.setMinimumSize(new Dimension(100, 30));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        this.field = field::getText;
        return field;
    }

    private Component goButton() {
        JButton jButton = new JButton("Go");
        jButton.addActionListener(e -> commands.get("go").execute());
        return jButton;
    }

    public RouteField field(){return field;}

    public void add(String name, Command command) {
        commands.put(name, command);
    }

    public ImageDisplay getImageDisplay() {
        return imageDisplay;
    }

    private Component createImageDisplay() {
        SwingImageDisplay display = new SwingImageDisplay(new SwingImageDeserializer());
        this.imageDisplay = display;
        return display;
    }
}
