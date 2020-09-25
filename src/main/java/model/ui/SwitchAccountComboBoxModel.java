package model.ui;

import model.data.Account;

import javax.swing.DefaultComboBoxModel;
import java.util.List;

public class SwitchAccountComboBoxModel extends DefaultComboBoxModel<Account> {
    private static final int NOT_FOUND = -1;

    public void updateComboBox(List<Account> accounts) {
        for (Account account : accounts) {
            if (getIndexOf(account) == NOT_FOUND)
                addElement(account);
        }
    }
}
