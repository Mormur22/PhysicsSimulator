package simulator.view;

import simulator.control.Controller;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    Controller _ctrl;

    public MainWindow(Controller ctrl) {
        super("Physics Simulator");
        _ctrl = ctrl;
        initGUI();

        this.setVisible(true);
        this.pack();
    }

    private void initGUI() {
        this.setSize(500, 500);
        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        ControlPanel cp = new ControlPanel(_ctrl);
        this.add(cp, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new GridLayout(2, 1));
        mainPanel.add(new BodiesTable(_ctrl));
        mainPanel.add(new Viewer(_ctrl));

        this.add(mainPanel);
        this.add(new StatusBar(_ctrl), BorderLayout.SOUTH);
        
    }

}