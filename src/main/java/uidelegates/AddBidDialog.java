package uidelegates;

import core.Constants;
import dto.bid.BidDto;
import service.bid.BidService;
import utils.PurchaseCostCalculator;

import javax.swing.BoxLayout;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import java.awt.Container;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.math.BigDecimal;

public class AddBidDialog extends JDialog {
    private Long accountId;
    private BidService bidService;

    private JTextField stockNameTextField;
    private JTextField stockQuantityTextField;
    private JTextField bidPriceTextField;
    private JTextField purchaseCostValue;
    private JLabel errorMessage;
    private JButton cancelButton;
    private JButton sendButton;

    private String stockName;
    private Integer stockQuantity;
    private Double bidPrice;
    private boolean hasNewBid = false;

    public AddBidDialog(JFrame owner, Long accountId, BidService bidService) {
        super(owner, "Add new bid", true);
        this.accountId = accountId;
        this.bidService = bidService;
        errorMessage = new JLabel();
        prepareTextFieldsAndButtons();
        initListeners();
        initUI();
    }

    private void prepareTextFieldsAndButtons() {
        stockNameTextField = new JTextField();
        stockNameTextField.setInputVerifier(new StockNameInputVerifier());
        stockNameTextField.setPreferredSize(Constants.TEXT_FIELD_DIMENSION);
        stockNameTextField.setToolTipText("Enter name of stock");

        stockQuantityTextField = new JTextField();
        stockQuantityTextField.setInputVerifier(new StockQuantityInputVerifier());
        stockQuantityTextField.setPreferredSize(Constants.TEXT_FIELD_DIMENSION);
        stockQuantityTextField.setToolTipText("Enter quantity of stocks");

        bidPriceTextField = new JTextField();
        bidPriceTextField.setInputVerifier(new BidPriceInputVerifier());
        bidPriceTextField.setPreferredSize(Constants.TEXT_FIELD_DIMENSION);
        bidPriceTextField.setToolTipText("Enter price for one stock");

        purchaseCostValue = new JTextField("Enter stock quantity and bid price");
        purchaseCostValue.setEditable(false);
        purchaseCostValue.setPreferredSize(Constants.TEXT_FIELD_DIMENSION);

        sendButton = new JButton("Send");
        cancelButton = new JButton("Cancel");
    }

    private void initListeners() {
        sendButton.addActionListener(e -> {
            if (stockName == null || stockQuantity == null || bidPrice == null) {
                errorMessage.setText("Please enter stock name, stock quantity and bid price before press \"Send\"");
                getContentPane().add(errorMessage);
                pack();
                return;
            }
            BidDto dto = new BidDto(accountId, stockName, BigDecimal.valueOf(bidPrice), stockQuantity);
            bidService.save(dto);
            hasNewBid = true;
            dispose();
        });
        cancelButton.addActionListener(e -> dispose());
    }

    private void initUI() {
        Container rootContainer = getContentPane();
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        rootContainer.setLayout(new BoxLayout(rootContainer, BoxLayout.Y_AXIS));

        JPanel stockNamePanel = new JPanel();
        JLabel stockNameLabel = new JLabel("Enter name of stock:");
        stockNamePanel.add(stockNameLabel);
        stockNamePanel.add(stockNameTextField);

        JPanel stockQuantityPanel = new JPanel();
        JLabel stockQuantityLabel = new JLabel("Enter quantity of stocks:");
        stockQuantityPanel.add(stockQuantityLabel);
        stockQuantityPanel.add(stockQuantityTextField);

        JPanel bidPricePanel = new JPanel();
        JLabel bidPriceLabel = new JLabel("Enter price for one stock:");
        bidPricePanel.add(bidPriceLabel);
        bidPricePanel.add(bidPriceTextField);

        JPanel purchaseCostPanel = new JPanel();
        JLabel purchaseCostLabel = new JLabel("Purchase cost:");
        purchaseCostPanel.add(purchaseCostLabel);
        purchaseCostPanel.add(purchaseCostValue);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(cancelButton);
        buttonPanel.add(sendButton);

        rootContainer.add(stockNamePanel);
        rootContainer.add(stockQuantityPanel);
        rootContainer.add(bidPricePanel);
        rootContainer.add(purchaseCostPanel);
        rootContainer.add(buttonPanel);
        pack();
        setVisible(true);
    }

    public boolean hasNewBid() {
        return hasNewBid;
    }

    private String getCurrentPurchaseCostValue() {
        BigDecimal value = PurchaseCostCalculator.calculate(BigDecimal.valueOf(bidPrice), stockQuantity);
        return value.toString();
    }

    private class StockNameInputVerifier extends InputVerifier {
        private String stockNameTempValue;
        private boolean emptyStringProvided;

        @Override
        public boolean shouldYieldFocus(JComponent input) {
            emptyStringProvided = false;
            if (!verify(input)) {
                stockNameTextField.setText("Enter correct value, for example GOOG");
                return true;
            }
            if (emptyStringProvided) {
                return true;
            }
            stockName = stockNameTempValue;
            return true;
        }

        @Override
        public boolean verify(JComponent input) {
            stockNameTempValue = stockNameTextField.getText();
            if (Constants.EMPTY_STRING.equals(stockNameTempValue)) {
                emptyStringProvided = true;
                return true;
            }
            return stockNameTempValue.matches(Constants.STOCK_NAME_PATTERN);
        }

    }

    private class StockQuantityInputVerifier extends InputVerifier {
        private Integer stockQuantityTempValue;
        private boolean emptyStringProvided;

        @Override
        public boolean shouldYieldFocus(JComponent input) {
            emptyStringProvided = false;
            if (!verify(input)) {
                stockQuantityTextField.setText("Enter correct value, for example 12");
                return true;
            }
            if (emptyStringProvided)
                return true;
            stockQuantity = stockQuantityTempValue;
            if (bidPrice != null) {
                purchaseCostValue.setText(getCurrentPurchaseCostValue());
            }
            return true;
        }

        @Override
        public boolean verify(JComponent input) {
            try {
                String stockQuantityTextRepresentation = stockQuantityTextField.getText();
                if (Constants.EMPTY_STRING.equals(stockQuantityTextRepresentation)) {
                    emptyStringProvided = true;
                    return true;
                }
                stockQuantityTempValue = Integer.parseInt(stockQuantityTextRepresentation);
                if (stockQuantityTempValue < 0) {
                    return false;
                }
                return true;
            } catch (NumberFormatException numberFormatException) {
                return false;
            }
        }
    }

    private class BidPriceInputVerifier extends InputVerifier {
        private static final int ALLOWED_SCALE = 2;
        private Double bidPriceTempValue;
        private boolean emptyStringProvided;

        @Override
        public boolean shouldYieldFocus(JComponent input) {
            emptyStringProvided = false;
            if (!verify(input)) {
                bidPriceTextField.setText("Enter correct value, for example 12.34");
                return true;
            }
            if (emptyStringProvided)
                return true;
            bidPrice = bidPriceTempValue;
            if (stockQuantity != null)
                purchaseCostValue.setText(getCurrentPurchaseCostValue());
            return true;
        }

        @Override
        public boolean verify(JComponent bidPriceTextField) {
            String bidPriceText = reformatDecimalPoint(((JTextField) bidPriceTextField).getText());
            if (Constants.EMPTY_STRING.equals(bidPriceText)) {
                emptyStringProvided = true;
                return true;
            }
            try {
                bidPriceTempValue = Double.parseDouble(bidPriceText);
                if (bidPriceTempValue < 0)
                    return false;
                if (BigDecimal.valueOf(bidPriceTempValue).scale() > ALLOWED_SCALE)
                    return false;
                if (bidPriceTempValue.isInfinite() || bidPriceTempValue.isNaN())
                    return false;
                return true;
            } catch (NumberFormatException numberFormatException) {
                return false;
            }
        }

        private String reformatDecimalPoint(String bidPriceText) {
            return bidPriceText.replace(',', '.');
        }
    }
}


