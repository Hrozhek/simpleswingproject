package uidelegates;

import core.ApplicationConstants;
import model.ui.BidTableModel;
import model.ui.SwitchAccountComboBoxModel;
import model.data.Account;
import service.account.AccountService;
import service.bid.BidService;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridBagLayout;

public class RootWindow extends JFrame {
    private static final String APP_TITLE = "Simple Swing application";

    private final AccountService accountService;
    private final BidService bidService;
    private long currentAccountId;
    private BidTableModel bidTableModel;
    private SwitchAccountComboBoxModel switchAccountComboBoxModel;

    private JComboBox<Account> switchAccountBox;
    private JButton addAccountButton;
    private JTable bidTable;
    private JButton addBidButton;

    public RootWindow(AccountService accountService, BidService bidService) {
        super(APP_TITLE);
        currentAccountId = ApplicationConstants.EMPTY_ENTITY_ID;
        this.accountService = accountService;
        this.bidService = bidService;
        initUI();
        initActionListeners();
    }

    private void initUI() {
        Container rootContainer = getContentPane();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        rootContainer.setLayout(new BorderLayout());

        JPanel userPanel = new JPanel();
        switchAccountComboBoxModel = new SwitchAccountComboBoxModel();
        switchAccountBox = new JComboBox<>(switchAccountComboBoxModel);
        switchAccountBox.setToolTipText("Select account");
        switchAccountBox.setPreferredSize(ApplicationConstants.TEXT_FIELD_DIMENSION);
        userPanel.add(switchAccountBox);

        addAccountButton = new JButton("Add new account");
        userPanel.add(addAccountButton);

        bidTableModel = new BidTableModel(bidService.getAllByAccountId(currentAccountId));
        bidTable = new JTable(bidTableModel);
        bidTable.setShowHorizontalLines(true);
        bidTable.setShowVerticalLines(true);
        bidTable.getTableHeader().setReorderingAllowed(false);
        JScrollPane bidTablePane = new JScrollPane(bidTable);
        addBidButton = new JButton("Add new bid");

        rootContainer.add(userPanel, BorderLayout.NORTH);
        rootContainer.add(bidTablePane, BorderLayout.CENTER);
        rootContainer.add(addBidButton, BorderLayout.SOUTH);
        pack();
        setVisible(true);
    }

    private void initActionListeners() {
        addAccountButton.addActionListener(e -> {
            AddAccountDialog addAccountDialog = new AddAccountDialog(this, accountService);
            if (addAccountDialog.isNewAccountAvailable())
                switchAccountComboBoxModel.updateComboBox(accountService.getAll());
        });

        addBidButton.addActionListener(event -> {
            if (currentAccountId == ApplicationConstants.EMPTY_ENTITY_ID) {
                showWarning();
                return;
            }
            AddBidDialog addBidDialog = new AddBidDialog(this, currentAccountId, bidService);
            if (addBidDialog.isNewBidAvailable()) {
                bidTableModel.setBids(bidService.getAllByAccountId(currentAccountId));
                bidTableModel.fireTableDataChanged();
            }
        });

        switchAccountBox.addActionListener(e -> {
            Account currentAccount = (Account) switchAccountBox.getSelectedItem();
            if (currentAccount == null)
                return;
            currentAccountId = currentAccount.getId();
            bidTableModel.setBids(bidService.getAllByAccountId(currentAccountId));
            bidTableModel.fireTableDataChanged();
        });
    }

    private void showWarning() {
        JDialog selectAccountWarning = new JDialog(this);
        JLabel warning = new JLabel("Please select account");
        warning.setVisible(true);
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> selectAccountWarning.dispose());

        selectAccountWarning.setLayout(new GridBagLayout());
        selectAccountWarning.getContentPane().add(warning);
        selectAccountWarning.getContentPane().add(okButton);
        selectAccountWarning.setModal(true);
        selectAccountWarning.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        selectAccountWarning.setResizable(false);
        selectAccountWarning.pack();
        selectAccountWarning.setVisible(true);
    }
}