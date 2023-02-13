package simulator.view;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class ForceTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 1L;
	private String[] columns = {"Key", "Value", "Description"};
	private List<LawsInfo> _data;



	public ForceTableModel(){
		_data = new ArrayList<>();


	}


	public void updateTable(JSONObject data)
	{
		_data = new ArrayList<>();

		for(String key: data.keySet())
		{
			LawsInfo l = new LawsInfo(key,"", data.getString(key));
			_data.add(l);
		}

		fireTableStructureChanged();
	}

	public String getColumnName(int column)
	{
		return columns[column];
	}

	@Override
	public int getRowCount() {
		return _data.size();
	}

	@Override
	public int getColumnCount() {
		return columns.length;

	}

	public boolean isCellEditable(int row, int col)
	{
		return(col==1);
	}


	public void setValueAt(Object o, int row, int col)
	{
		LawsInfo l = _data.get(row);
		l.setValue(o.toString());
		fireTableCellUpdated(row, col);
	}

	@Override
	public String getValueAt(int row, int col) {

		LawsInfo l = _data.get(row);
		String s="";

		switch(col)
		{
			case 0:
				s+=l.getKey();
				break;

			case 1:
				s+=l.getValue();
				break;

			case 2:
				s+=l.getDescription();
				break;

		}

		return s;
	}

	public JSONObject getLaw()
	{
		JSONObject law= new JSONObject();
		for(int i=0;i<getRowCount();i++)
		{
			String key=getValueAt(i,0);


			if(getValueAt(i, 1).contains(","))
			{
				JSONArray center= new JSONArray();
				String parse=getValueAt(i,1);
				String[] numbers = parse.split(",");
				String first= numbers[0].substring(1);
				String second= numbers[1].substring(0,numbers[1].length()-1);
				center.put( Double.parseDouble(first));
				center.put( Double.parseDouble(second));
				law.put(key,center);

			} else if(getValueAt(i, 1).length() == 0) {

			} else {

				Double value= Double.parseDouble(getValueAt(i,1));
				law.put(key, value);
			}
		}


		return law;
	}
}