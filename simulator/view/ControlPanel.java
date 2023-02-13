package simulator.view;

import org.json.JSONObject;
import simulator.control.Controller;
import simulator.model.Body;
import simulator.model.SimulatorObserver;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

public class ControlPanel extends JPanel implements SimulatorObserver {

	private Controller _ctrl;
	private boolean _stopped;
	private int _steps = 1000;

	private JButton openFileButton;
	private JButton lawButton;
	private JButton startButton;
	private JButton stopButton;
	private JButton exitButton;

	private JSpinner steps;
	private JTextField tDeltaTime;

	private JLabel lsteps;
	private JLabel lDeltaTime;

	private JToolBar toolBar;

	private ForceSelectionDialog dialog;
	private JDialog ExitDialog;

	private List<JSONObject> _list;

	ControlPanel(Controller ctrl) {
		_ctrl = ctrl;
		_stopped = true;
		initGUI();
		_ctrl.addObserver(this);
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
	}

	private void initGUI() {
		// TODO build the toolbar by adding buttons, labels...
		toolBar = new JToolBar();

		initButtons();
		initJClasses();

		toolBar.add(openFileButton);
		toolBar.addSeparator();
		toolBar.add(lawButton);
		toolBar.addSeparator();
		toolBar.add(startButton);
		toolBar.addSeparator();
		toolBar.add(stopButton);

		toolBar.addSeparator();
		toolBar.add(lsteps);
		toolBar.add(steps);
		toolBar.addSeparator();

		toolBar.add(lDeltaTime);
		toolBar.add(tDeltaTime);

		toolBar.add(Box.createGlue());

		toolBar.add(exitButton);

		this.add(toolBar, BoxLayout.X_AXIS);
	}

	// other private/protected methods
	// ...
	// TODO revise
	private void initButtons() {
		openFileButton = new JButton();
		openFileButton.setIcon(new ImageIcon("resources/icons/open.png"));
		openFileButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO JFileChooser, _ctrl.reset(), _ctrl.loadBodies(...)
				fileChooser();
			}
		});
		lawButton = new JButton();
		lawButton.setIcon(new ImageIcon("resources/icons/physics.png"));
		lawButton.addActionListener(new ActionListener() {
			// TODO revise
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO JOptionPane.showMessageDialog, _ctrl.setForceLaws(...), _ctrl.getForceLawsInfo()
				// Revise
				physics();
			}
		});
		startButton = new JButton();
		startButton.setIcon(new ImageIcon("resources/icons/run.png"));
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				_stopped = false;
				Enable_Disable(false);
				_ctrl.setDeltaTime(Double.parseDouble(tDeltaTime.getText()));
				run_sim(Integer.parseInt(String.valueOf(steps.getValue())));
			}
		});
		stopButton = new JButton();
		stopButton.setIcon(new ImageIcon("resources/icons/stop.png"));
		stopButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				_stopped = true;
			}
		});
		exitButton = new JButton();
		exitButton.setIcon(new ImageIcon("resources/icons/exit.png"));
		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				exit();
			}
		});
	}

	private void Enable_Disable(boolean b) {
		openFileButton.setEnabled(b);
		lawButton.setEnabled(b);
		startButton.setEnabled(b);
		exitButton.setEnabled(b);
	}

	private void initJClasses() {

		steps = new JSpinner(new SpinnerNumberModel(_steps, 1, null, 1000));
		steps.setMaximumSize(new Dimension(1000, 50));

		tDeltaTime = new JTextField("1");
		tDeltaTime.setMaximumSize(new Dimension(1750, 50));

		lsteps = new JLabel("Steps:");
		lDeltaTime = new JLabel("Delta-Time:");
	}
	protected void physics()
	{

		if (dialog == null) {

			_list= _ctrl.getForceLawsInfo();
			dialog = new ForceSelectionDialog((Frame) SwingUtilities.getWindowAncestor(this),_list);
		}

		int status = dialog.open();

		if (status == 1) {

			try {
				JSONObject obj = dialog.getLaw();
				_ctrl.setForceLaws(obj);
			}
			catch(Exception e)
			{
				JOptionPane.showMessageDialog(this.getParent(),"Something want wrong" +
						e.getLocalizedMessage(),"ERROR",JOptionPane.ERROR_MESSAGE);
			}
		}

	}

	private void fileChooser() {
		String path = new File("").getAbsolutePath();
		JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new File(path+"\\resources\\examples"));
		fc.setFileFilter(new FileNameExtensionFilter("JSON Files", "json"));
		int v = fc.showOpenDialog(this);
		if(v == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			try {
				InputStream in = new FileInputStream(file);
				_ctrl.reset();
				_ctrl.loadBodies(in);
			} catch(Exception e) {
				System.out.println("Load cancelled by user");
			}
		}
	}
	private void exit() {
		ExitDialog = new JDialog();
		int i = JOptionPane.showConfirmDialog(ExitDialog,"Are you sure?", "Exit", JOptionPane.YES_NO_OPTION);
		if(i == 0)
			System.exit(0);
	}

	private void run_sim(int n) {
		if ( n>0 && !_stopped ) {
			try {
				_ctrl.run(1);

			} catch (Exception e) {
				// TODO show the error in a dialog box
				JOptionPane.showOptionDialog(null, "Something went wrong" + e.getMessage(), "EXCEPTION CAUGHT", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, new String[]{"OK"}, "OK");
				// TODO enable all buttons
				Enable_Disable(true);
				_stopped = true;
				return;
			}
			SwingUtilities.invokeLater( new Runnable() {
				@Override
				public void run() {
					run_sim(n-1);
				}
			});
		} else {
			_stopped = true;
			// TODO enable all buttons
			Enable_Disable(true);
		}

	}

	private void setDeltaTime(double DeltaTime) {tDeltaTime.setText(""+DeltaTime);}

	// TODO
	@Override
	public void onRegister(List<Body> bodies, double time, double dt, String fLawsDesc) {
		setDeltaTime(dt);
	}

	@Override
	public void onReset(List<Body> bodies, double time, double dt, String fLawsDesc) {
		setDeltaTime(dt);
	}

	@Override
	public void onBodyAdded(List<Body> bodies, Body b) {

	}

	@Override
	public void onAdvance(List<Body> bodies, double time) {

	}

	@Override
	public void onDeltaTimeChanged(double dt) {

	}

	@Override
	public void onForceLawsChanged(String fLawsDesc) {

	}

}
