package uihelpers;

import lombok.Setter;
import model.Bid;

import javax.swing.table.AbstractTableModel;
import java.util.List;

@Setter
public class BidTableModel extends AbstractTableModel {
    private static final int NUMBER_OF_COLUMNS = 4;
    private List<Bid> bids;

    public BidTableModel(List<Bid> bids) {
        this.bids = bids;
    }

    @Override
    public String getColumnName(int columnNumber) {
        switch (columnNumber) {
        case (0):
            return "id";
        case (1):
            return "Stock name";
        case (2):
            return "Stock quantity";
        case (3):
            return "Cost";
        default:
            return "Unknown column";
        }

    }

    @Override
    public int getRowCount() {
        return bids.size();
    }

    @Override
    public int getColumnCount() {
        return NUMBER_OF_COLUMNS;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Bid bid = bids.get(rowIndex);
        switch (columnIndex) {
        case (0):
            return bid.getId();
        case (1):
            return bid.getStockName();
        case (2):
            return bid.getStockQuantity();
        case (3):
            return bid.getPurchaseCost();
        default:
            return null;
        }
    }
}
