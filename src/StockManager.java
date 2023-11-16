import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StockManager extends JFrame {
    private JButton chackStocksButton;
    private JButton addNewOrderButton;

    public StockManager() {
        addNewOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new NewOrderStocks().setVisible(true);
            }
        });
    }
}
