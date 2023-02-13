package simulator.view;

import javax.swing.BoxLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.json.JSONObject;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;


public class ForceSelectionDialog extends JDialog {


	private static final long serialVersionUID = 1L;
	private int status;
	private JComboBox<String> _laws;
	private ForceTableModel forceModel;
	private int index;
	private JPanel bottom;
	private BoxLayout boxlayout;
	private JLabel label;
	private final String label_desc="Select a force law and provides the values for the parameters in the Value Column";
	private JPanel layoutPanel;
	private JSONObject forceselected;

	public ForceSelectionDialog(Frame parent, List<JSONObject> _list) {
		super(parent,true);
		this.setTitle("Force selection");
		this.setBounds(600, 600, 600, 350);
		this.index=0;
		this.status=0;
		this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		initGUI(_list);
		/*this.setBounds(600, 600, 600, 350);
		this.setVisible(true);
		this.pack();*/
	}

	private void initGUI( List<JSONObject> _list) {

		JPanel mainPanel = new JPanel();
		BorderLayout border= new BorderLayout();
		mainPanel.setLayout(border);


		//JLabel
		layoutPanel= new JPanel();
		label= new JLabel(label_desc);
		layoutPanel.add(label);
		mainPanel.add(layoutPanel,BorderLayout.NORTH);


		//ForceTable
		forceModel= new ForceTableModel();
		JTable table= new JTable(forceModel);
		JScrollPane scroll = new JScrollPane(table);
		mainPanel.add(scroll,BorderLayout.CENTER);




		//Combo box
		bottom = new JPanel();
		boxlayout = new BoxLayout(bottom,BoxLayout.Y_AXIS);
		bottom.setLayout(boxlayout);

		_laws=new JComboBox<>();


		for (JSONObject jsonObject : _list) {
			_laws.addItem(jsonObject.getString("desc"));
		}

		_laws.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e)
			{
				index= _laws.getSelectedIndex();
				forceselected = _list.get(index);
				forceModel.updateTable(forceselected.getJSONObject("data"));
			}

		});

		index = 0;
		forceselected = _list.get(index);
		forceModel.updateTable(forceselected.getJSONObject("data"));

		bottom.add(_laws);
		mainPanel.add(bottom,BorderLayout.SOUTH);
		//


		//Buttons

		JPanel botones= new JPanel(new FlowLayout());

		JButton ok= new JButton("Ok");
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				if(_laws.getSelectedItem()!=null)
				{
					status=1;
					ForceSelectionDialog.this.setVisible(false);
				}
			}
		});


		JButton cancel= new JButton("Cancel");
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				if(_laws.getSelectedItem()!=null)
				{
					status=0;
					ForceSelectionDialog.this.setVisible(false);
				}
			}
		});

		botones.add(ok);
		botones.add(cancel);
		bottom.add(botones);

		this.add(mainPanel);
	}



	public int open() {
		setBounds(300, 400, 400, 350);
		//setLocation(getParent().getLocation().x + 10, getParent().getLocation().y + 10);
		pack();
		setVisible(true);
		return status;
	}

	JSONObject getLaw() {

		JSONObject force= new JSONObject();
		force.put("type",forceselected.getString("type"));
		force.put("data",forceModel.getLaw());

		return force;
	}

}

