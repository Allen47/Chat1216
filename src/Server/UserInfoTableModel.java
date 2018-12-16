package Server;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.List;

public class UserInfoTableModel implements TableModel {
    public List<ServerThread> sts = ChatTools.getAllThread();
    public UserInfoTableModel(List<ServerThread> sts) {
        this.sts=sts;

    }
    public void addTableModelListener(TableModelListener l) {
        // TODO Auto-generated method stub

    }

//    @Override
    public Class<?> getColumnClass(int columnIndex) {
        // TODO Auto-generated method stub
        return String.class;
    }

//    @Override
    public int getColumnCount() {
        // TODO Auto-generated method stub
        return 1;
    }

//    @Override
    public String getColumnName(int columnIndex) {
        // TODO Auto-generated method stub
        return null;
    }

//    @Override
    public int getRowCount() {
        // TODO Auto-generated method stub
        return sts.size();
    }

//    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        // TODO Auto-generated method stub
        return sts.get(rowIndex).getOwerUser().getName();
    }

//    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        // TODO Auto-generated method stub
        return false;
    }

//    @Override
    public void removeTableModelListener(TableModelListener l) {
        // TODO Auto-generated method stub

    }

//    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        // TODO Auto-generated method stub

    }

}
