package bsu.rfe.java.group8.lab3.KONONOK.varC3;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;

public class GornerTableCellRenderer implements TableCellRenderer
{
    private JPanel panel = new JPanel();
    private JLabel label = new JLabel();
    private JCheckBox check = new JCheckBox();
    private String needle = null;
    private String needleX = null;
    private String needleY = null;
    private boolean p = false;


    private DecimalFormat formatter = (DecimalFormat)NumberFormat.getInstance();

    GornerTableCellRenderer()
    {
        formatter.setMaximumFractionDigits(15);
        formatter.setGroupingUsed(false);
        DecimalFormatSymbols dottedDouble = formatter.getDecimalFormatSymbols();
        dottedDouble.setDecimalSeparator('.');
        formatter.setDecimalFormatSymbols(dottedDouble);
        panel.add(check);
        check.setSelected (true);
        check.setVisible(false);
        panel.add(label);
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
    }

    public Component getTableCellRendererComponent(JTable table,
                                                   Object value, boolean isSelected, boolean hasFocus, int row, int col) {
        String formattedDouble = formatter.format(value);
        label.setText(formattedDouble);

        if (col >= 1 && needle!=null && needle.equals(formattedDouble)) {
            check.setVisible(true);
            label.setText("");
        } else {
            check.setVisible(false);
            label.setText(formattedDouble);
        }

        if (p)
        {
            boolean polin = true;
            int size = 0;
            String ptext = label.getText();
            while (size < ptext.length() && ptext.charAt(size) != '.')
                size++;
            if (size < ptext.length()) {
                ptext = ptext.substring(0, size) + ptext.substring(size + 1);
            }
            size = ptext.length();
            for (int i = 0; i < size; i++)
            {
                if (ptext.charAt(i) != ptext.charAt(size - i - 1)) {
                    polin = false;
                    break;
                }
            }

            if (polin)
                panel.setBackground(Color.green);
            else
                panel.setBackground(Color.white);
        }
        else
            panel.setBackground(Color.white);

        return panel;
    }

    public String getNeedleX() {
        return needleX;
    }

    public void setNeedleX(String needleX) {
        this.needleX = needleX;
    }


    public String getNeedleY() {
        return needleY;
    }


    public void setNeedleY(String needleY) {
        this.needleY = needleY;
    }


    void setNeedle(String needle)
    {
        this.needle = needle;
    }

    void setPalindrom(boolean p)
    {
        if(p)
            formatter.setMaximumFractionDigits(2);
        else
            formatter.setMaximumFractionDigits(15);
        this.p = p;
    }

    public void setNeedle1(String needleX, String needleY)
    {
        this.needleX = needleX;
        this.needleY = needleY;
    }
}
