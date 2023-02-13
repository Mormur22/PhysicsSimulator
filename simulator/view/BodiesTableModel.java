package simulator.view;

import simulator.control.Controller;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

// TODO Revise
public class BodiesTableModel extends AbstractTableModel implements SimulatorObserver {

    private List<Body> _bodies;

    BodiesTableModel (Controller ctrl) {
        _bodies = new ArrayList<>();
        ctrl.addObserver(this);
    }

    @Override
    public int getRowCount() {
        return _bodies.size();
    }

    @Override
    public int getColumnCount() {
        return 5;
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0: return "ID";
            case 1: return "MASS";
            case 2: return "POSITION";
            case 3: return "FORCE";
            case 4: return "VELOCITY";
            default: return null;
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0: return _bodies.get(rowIndex).getId();
            case 1: return _bodies.get(rowIndex).getMass();
            case 2: return _bodies.get(rowIndex).getPosition();
            case 3: return _bodies.get(rowIndex).getForce();
            case 4: return _bodies.get(rowIndex).getVelocity();
            default: return null;
        }
    }

    private void update() {
        SwingUtilities.invokeLater(this::fireTableStructureChanged);
    }

    @Override
    public void onRegister(List<Body> bodies, double time, double dt, String fLawsDesc) {
        _bodies = bodies;
        update();
    }

    @Override
    public void onReset(List<Body> bodies, double time, double dt, String fLawsDesc) {
        _bodies = new ArrayList<>();
        update();
    }

    @Override
    public void onBodyAdded(List<Body> bodies, Body b) {
        _bodies.add(b);
        update();
    }

    @Override
    public void onAdvance(List<Body> bodies, double time) {
        _bodies = bodies;
        update();
    }

    @Override
    public void onDeltaTimeChanged(double dt) {

    }

    @Override
    public void onForceLawsChanged(String fLawsDesc) {

    }
}