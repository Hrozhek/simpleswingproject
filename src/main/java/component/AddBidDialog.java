package component;

import core.ApplicationConstants;
import dto.bid.BidDto;
import service.bid.BidService;
import utils.PurchaseCostCalculator;

import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.math.BigDecimal;

public class AddBidDialog extends JDialog {
    private static final int SPRING_LAYOUT_CONSTRAINT = 10;
    private static final String STOCK_NAME_PATTERN = "[A-Z]{1,4}";
    private static final Dimension ADD_BID_DIMENSION = new Dimension(500, 300);
    private final Long accountId;
    private final BidService bidService;

    private JTextField stockNameTextField;
    private JTextField stockQuantityTextField;
    private JTextField bidPriceTextField;
    private JTextField purchaseCostValue;
    private JLabel errorMessageLabel;
    private JButton cancelButton;
    private JButton sendButton;

    private String stockName;
    private Integer stockQuantity;
    private Double bidPrice;
    private boolean newBidAvailable;

    public AddBidDialog(JFrame owner, Long accountId, BidService bidService) {
        super(owner, "Add new bid", true);
        this.accountId = accountId;
        this.bidService = bidService;
        prepareTextFieldsAndButtons();
        initListeners();
        initUI();
    }

    private void prepareTextFieldsAndButtons() {
        stockNameTextField = new JTextField();
        stockNameTextField.setInputVerifier(new StockNameInputVerifier());
        stockNameTextField.setPreferredSize(ApplicationConstants.TEXT_FIELD_DIMENSION);
        stockNameTextField.setToolTipText("Enter name of stock");

        stockQuantityTextField = new JTextField();
        stockQuantityTextField.setInputVerifier(new StockQuantityInputVerifier());
        stockQuantityTextField.setPreferredSize(ApplicationConstants.TEXT_FIELD_DIMENSION);
        stockQuantityTextField.setToolTipText("Enter quantity of stocks");

        bidPriceTextField = new JTextField();
        bidPriceTextField.setInputVerifier(new BidPriceInputVerifier());
        bidPriceTextField.setPreferredSize(ApplicationConstants.TEXT_FIELD_DIMENSION);
        bidPriceTextField.setToolTipText("Enter price for one stock");

        purchaseCostValue = new JTextField("Enter stock quantity and bid price");
        purchaseCostValue.setEditable(false);
        purchaseCostValue.setPreferredSize(ApplicationConstants.TEXT_FIELD_DIMENSION);

        sendButton = new JButton("Send");
        cancelButton = new JButton("Cancel");
    }

    private void initListeners() {
        sendButton.addActionListener(e -> {
            if (stockName == null || stockQuantity == null || bidPrice == null) {
                String msg = "Please enter stock name, stock quantity and bid price before press \"Send\"";
                errorMessageLabel.setText(msg);
                return;
            }
            BidDto dto = new BidDto(accountId, stockName, BigDecimal.valueOf(bidPrice), stockQuantity);
            bidService.save(dto);
            newBidAvailable = true;
            dispose();
        });
        cancelButton.addActionListener(e -> dispose());
    }

    private void initUI() {
        Container rootContainer = getContentPane();
        SpringLayout layout = new SpringLayout();

        JPanel stockNamePanel = new JPanel();
        JLabel stockNameLabel = new JLabel("Enter name of stock:");
        stockNameLabel.setLabelFor(stockNameTextField);
        stockNamePanel.add(stockNameLabel);
        stockNamePanel.add(stockNameTextField);
        putBordersBetweenFarElements(layout, rootContainer, stockNamePanel, true);

        JPanel stockQuantityPanel = new JPanel();
        JLabel stockQuantityLabel = new JLabel("Enter quantity of stocks:");
        stockQuantityLabel.setLabelFor(stockQuantityTextField);
        stockQuantityPanel.add(stockQuantityLabel);
        stockQuantityPanel.add(stockQuantityTextField);
        putVerticalLayoutConstraints(layout, stockQuantityPanel, stockNamePanel);

        JPanel bidPricePanel = new JPanel();
        JLabel bidPriceLabel = new JLabel("Enter price for one stock:");
        bidPriceLabel.setLabelFor(bidPriceTextField);
        bidPricePanel.add(bidPriceLabel);
        bidPricePanel.add(bidPriceTextField);
        putVerticalLayoutConstraints(layout, bidPricePanel, stockQuantityPanel);

        JPanel purchaseCostPanel = new JPanel();
        JLabel purchaseCostLabel = new JLabel("Purchase cost:");
        purchaseCostLabel.setLabelFor(purchaseCostValue);
        purchaseCostPanel.add(purchaseCostLabel);
        purchaseCostPanel.add(purchaseCostValue);
        putVerticalLayoutConstraints(layout, purchaseCostPanel, bidPricePanel);

        errorMessageLabel = new JLabel();
        putVerticalLayoutConstraints(layout, errorMessageLabel, purchaseCostPanel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(cancelButton);
        buttonPanel.add(sendButton);
        putVerticalLayoutConstraints(layout, buttonPanel, errorMessageLabel);
        putBordersBetweenFarElements(layout, buttonPanel, rootContainer, false);

        rootContainer.setLayout(layout);
        rootContainer.add(stockNamePanel);
        rootContainer.add(stockQuantityPanel);
        rootContainer.add(bidPricePanel);
        rootContainer.add(purchaseCostPanel);
        rootContainer.add(errorMessageLabel);
        rootContainer.add(buttonPanel);
        rootContainer.setPreferredSize(ADD_BID_DIMENSION);
        setResizable(false);
        pack();
        setVisible(true);
    }

    public boolean isNewBidAvailable() {
        return newBidAvailable;
    }

    private String getCurrentPurchaseCostValue() {
        BigDecimal value = PurchaseCostCalculator.calculate(BigDecimal.valueOf(bidPrice), stockQuantity);
        return value.toString();
    }

    private void putBordersBetweenFarElements(SpringLayout layout, Container from, Container to, boolean isFirst) {
        String border = SpringLayout.SOUTH;
        if (isFirst)
            border = SpringLayout.NORTH;
        layout.putConstraint(border, from, SPRING_LAYOUT_CONSTRAINT, border, to);
    }

    private void putVerticalLayoutConstraints(SpringLayout layout, Container from, Container to) {
        layout.putConstraint(SpringLayout.NORTH, from, SPRING_LAYOUT_CONSTRAINT, SpringLayout.SOUTH, to);
    }

    private void addVerifierError(String exampleOfValue) {
        String message = "Enter correct value, for example %s";
        errorMessageLabel.setText(String.format(message, exampleOfValue));
    }

    private void clearVerifierError() {
        errorMessageLabel.setText(ApplicationConstants.EMPTY_STRING);
    }

    private class StockNameInputVerifier extends InputVerifier {
        private String stockNameTempValue;
        private boolean emptyStringProvided;

        @Override
        public boolean shouldYieldFocus(JComponent input) {
            clearVerifierError();
            emptyStringProvided = false;
            if (!verify(input)) {
                addVerifierError("GOOG");
                stockNameTextField.setText(ApplicationConstants.EMPTY_STRING);
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
            if (ApplicationConstants.EMPTY_STRING.equals(stockNameTempValue)) {
                emptyStringProvided = true;
                return true;
            }
            return stockNameTempValue.matches(STOCK_NAME_PATTERN);
        }

    }

    private class StockQuantityInputVerifier extends InputVerifier {
        private Integer stockQuantityTempValue;
        private boolean emptyStringProvided;

        @Override
        public boolean shouldYieldFocus(JComponent input) {
            clearVerifierError();
            emptyStringProvided = false;
            if (!verify(input)) {
                addVerifierError("12");
                stockQuantityTextField.setText(ApplicationConstants.EMPTY_STRING);
                return true;
            }
            if (emptyStringProvided)
                return true;
            stockQuantity = stockQuantityTempValue;
            if (bidPrice != null)
                purchaseCostValue.setText(getCurrentPurchaseCostValue());
            return true;
        }

        @Override
        public boolean verify(JComponent input) {
            try {
                String stockQuantityTextRepresentation = stockQuantityTextField.getText();
                if (ApplicationConstants.EMPTY_STRING.equals(stockQuantityTextRepresentation)) {
                    emptyStringProvided = true;
                    return true;
                }
                stockQuantityTempValue = Integer.parseInt(stockQuantityTextRepresentation);
                if (stockQuantityTempValue < 0)
                    return false;
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
            clearVerifierError();
            emptyStringProvided = false;
            if (!verify(input)) {
                addVerifierError("12.34");
                bidPriceTextField.setText(ApplicationConstants.EMPTY_STRING);
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
            if (ApplicationConstants.EMPTY_STRING.equals(bidPriceText)) {
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


