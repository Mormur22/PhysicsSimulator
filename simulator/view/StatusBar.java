package simulator.view;

import simulator.control.Controller;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class StatusBar extends JPanel implements SimulatorObserver {

    // ...
    private final String ltime = "Time: ";
    private final String llaw = "Law: ";
    private final String lbodies = "Bodies: ";

    private JLabel _currTime;
    private JLabel _currLaws;
    private JLabel _numOfBodies;

    private JSeparator separator;

    private int time = 0;
    private int numBodies = 0;
    private String law = "";

    StatusBar(Controller ctrl) {
        initGUI();
        ctrl.addObserver(this);
    }

    private void initGUI() {
        this.setLayout(new FlowLayout(FlowLayout.LEFT));
        this.setBorder(BorderFactory.createBevelBorder(1));

        // TODO complete the code to build the toolbar
        _currTime = new JLabel(ltime + time);
        _currLaws = new JLabel(llaw + law);
        _numOfBodies = new JLabel(lbodies+ numBodies);

        separator = new JSeparator(JSeparator.VERTICAL);

        _currTime.setMaximumSize(new Dimension(100,10));
        _currTime.setMaximumSize(new Dimension(100,10));
        _currTime.setPreferredSize(new Dimension(100,10));
        this.add(_currTime);
        this.add(separator);
        this.add(Box.createRigidArea(new Dimension(180,0)));
        this.add(_numOfBodies);
        this.add(separator);
        this.add(Box.createRigidArea(new Dimension(180,0)));
        this.add(_currLaws);

        this.setVisible(true);


    }
    
    
    // TODO
    @Override
    public void onRegister(List<Body> bodies, double time, double dt, String fLawsDesc) {
    	SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                _currLaws.setText(llaw + fLawsDesc);
            }
        });
   
    }

    @Override
    public void onReset(List<Body> bodies, double time, double dt, String fLawsDesc) {
    	SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                _currTime.setText(ltime+time);
                _numOfBodies.setText(lbodies+ bodies.size());
            }
        });
    }

    @Override
    public void onBodyAdded(List<Body> bodies, Body b) {
    	SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                _numOfBodies.setText(lbodies+ bodies.size());
            }
        });
    }

    @Override
    public void onAdvance(List<Body> bodies, double time) {
    	
    	SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                _currTime.setText(ltime+time);
            }
        });
    	

    }

	public void onDeltaTimeChanged(double dt) {
    	//NADA
    }

    @Override
    public void onForceLawsChanged(String fLawsDesc) {
   
    	SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                _currLaws.setText(llaw + fLawsDesc);
            }
        });
    }


}