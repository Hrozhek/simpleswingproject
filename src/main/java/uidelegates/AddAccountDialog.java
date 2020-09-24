package uidelegates;

import core.Constants;
import core.exceptions.DuplicateAccountNameException;
import dto.account.AccountDto;
import service.account.AccountService;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddAccountDialog extends JDialog {
    private AccountService accountService;

    private JTextField nameTextField;
    private JButton cancelButton;
    private JButton saveButton;

    private boolean hasNewAccount = false;

    public AddAccountDialog(JFrame owner, AccountService accountService) {
        super(owner, "Add new account", true);
        this.accountService = accountService;
        initUI();
    }

    private void initUI() {
        Container rootContainer = getContentPane();
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        rootContainer.setLayout(new BoxLayout(rootContainer, BoxLayout.Y_AXIS));

        nameTextField = new JTextField();
        nameTextField.setPreferredSize(Constants.TEXT_FIELD_DIMENSION);
        nameTextField.setToolTipText("Enter account name");

        JPanel buttonPanel = new JPanel();
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());
        buttonPanel.add(cancelButton);

        ActionListener saveActionListener = new SaveActionListener();
        saveButton = new JButton("Save");
        saveButton.addActionListener(saveActionListener);
        buttonPanel.add(saveButton);

        rootContainer.add(nameTextField);
        rootContainer.add(buttonPanel);
        pack();
        setVisible(true);
    }

    public boolean hasNewAccount() {
        return hasNewAccount;
    }

    private class SaveActionListener implements ActionListener {
        private JLabel errorMessage = new JLabel();

        @Override
        public void actionPerformed(ActionEvent event) {
            String name = nameTextField.getText();
            if (Constants.EMPTY_STRING.equals(name)) {
                String message = "Please provide correct name for the account";
                showIncorrectNameWarning(message);
                return;
            }
            AccountDto accountDto = new AccountDto(name);
            try {
                accountService.save(accountDto);
                hasNewAccount = true;
                dispose();
            } catch (DuplicateAccountNameException exception) {
                String message = "Account " + name + " already exist. Please, pick another name";
                showIncorrectNameWarning(message);
            }
        }

        private void showIncorrectNameWarning(String message) {
            errorMessage.setText(message);
            getContentPane().add(errorMessage);
            pack();
        }
    }
}
