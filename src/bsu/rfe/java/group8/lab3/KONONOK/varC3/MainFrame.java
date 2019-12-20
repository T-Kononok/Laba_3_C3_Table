package bsu.rfe.java.group8.lab3.KONONOK.varC3;

import com.opencsv.CSVWriter;
import java.awt.BorderLayout;
// import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import javax.swing.*;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {
    private static final int WIDTH = 700;
    private static final int HEIGHT = 500;
    private Double[] coefficients;
    private JFileChooser fileChooser = null;

    private JMenuItem saveToTextMenuItem;
    private JMenuItem saveToGraphicsMenuItem;
    private JMenuItem searchValueMenuItem;
    private JMenuItem palindromMenuItem;

    private JTextField textFieldFrom;
    private JTextField textFieldTo;
    private JTextField textFieldStep;
    private Box hBoxResult;
    private GornerTableCellRenderer renderer = new
            GornerTableCellRenderer();
    private GornerTableModel data;

    private DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance();

    private MainFrame(Double[] coefficients) {
        super("Табулирование многочлена на отрезке по схеме Горнера");
        this.coefficients = coefficients;
        setSize(WIDTH, HEIGHT);
        Toolkit kit = Toolkit.getDefaultToolkit();
        setLocation((kit.getScreenSize().width - WIDTH)/2,
                (kit.getScreenSize().height - HEIGHT)/2);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu fileMenu = new JMenu("Файл");
        menuBar.add(fileMenu);
        JMenu sprafMenu = new JMenu("Справка");
        menuBar.add(sprafMenu);
        ImageIcon foto = new ImageIcon("foto.jpg");
        Action OprogAction = new AbstractAction("О программе") {
            public void actionPerformed(ActionEvent event) {
                JOptionPane.showMessageDialog(MainFrame.this, "Кононок Тимофей, 8 группа",
                        "Программу написал:", JOptionPane.INFORMATION_MESSAGE, foto);
            }
        };
        sprafMenu.add(OprogAction);
        JMenu tableMenu = new JMenu("Таблица");
        menuBar.add(tableMenu);

        Action saveToTextAction = new AbstractAction("Сохранить в текстовый файл") {
            public void actionPerformed(ActionEvent event) {
                if (fileChooser==null) {
                    fileChooser = new JFileChooser();
                    fileChooser.setCurrentDirectory(new File("."));
                }
                if (fileChooser.showSaveDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION)
                    saveToTextFile(fileChooser.getSelectedFile());
            }
        };
        saveToTextMenuItem = fileMenu.add(saveToTextAction);
        saveToTextMenuItem.setEnabled(false);
        Action saveToGraphicsAction = new AbstractAction("Сохранить данные для построения графика") {
            public void actionPerformed(ActionEvent event) {
                if (fileChooser==null) {
                    fileChooser = new JFileChooser();
                    fileChooser.setCurrentDirectory(new File("."));
                }
                if (fileChooser.showSaveDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION)
                    saveToGraphicsFile(fileChooser.getSelectedFile());
            }
        };
        saveToGraphicsMenuItem = fileMenu.add(saveToGraphicsAction);
        saveToGraphicsMenuItem.setEnabled(false);
        Action saveToCVSAction = new AbstractAction("Сохранить в CSV-файл")
        {
            public void actionPerformed(ActionEvent arg0) {
                if (fileChooser == null){
                    fileChooser = new JFileChooser();
                    fileChooser.setCurrentDirectory(new File("."));
                }
                if (fileChooser.showSaveDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION)
                    saveToCSVFile(fileChooser.getSelectedFile());
            }
        };
        JMenuItem commaSeparatedValues = fileMenu.add(saveToCVSAction);
        commaSeparatedValues.setEnabled(false);

        Action searchValueAction = new AbstractAction("Найти значение многочлена") {
            public void actionPerformed(ActionEvent event) {
                String value =
                        JOptionPane.showInputDialog(MainFrame.this, "Введите значение для поиска",
                                "Поиск значения", JOptionPane.QUESTION_MESSAGE);
                renderer.setNeedle(value);
                getContentPane().repaint();
            }
        };
        Action searchPalindromAction = new AbstractAction("Найти полиндромы") {
            public void actionPerformed(ActionEvent event) {
                renderer.setPalindrom(true);
                getContentPane().repaint();
            }
        };

        palindromMenuItem = tableMenu.add(searchPalindromAction);
        searchValueMenuItem = tableMenu.add(searchValueAction);
        palindromMenuItem.setEnabled(false);
        searchValueMenuItem.setEnabled(false);
        commaSeparatedValues.setEnabled(false);
        JLabel labelForFrom = new JLabel("X изменяется на интервале от:");
        textFieldFrom = new JTextField("0.0", 10);
        textFieldFrom.setMaximumSize(textFieldFrom.getPreferredSize());
        JLabel labelForTo = new JLabel("до:");
        textFieldTo = new JTextField("1.0", 10);
        textFieldTo.setMaximumSize(textFieldTo.getPreferredSize());
        JLabel labelForStep = new JLabel("с шагом:");
        textFieldStep = new JTextField("0.1", 10);
        textFieldStep.setMaximumSize(textFieldStep.getPreferredSize());
        Box hboxRange = Box.createHorizontalBox();
        hboxRange.setBorder(BorderFactory.createBevelBorder(1));
        hboxRange.add(Box.createHorizontalGlue());
        hboxRange.add(labelForFrom);
        hboxRange.add(Box.createHorizontalStrut(10));
        hboxRange.add(textFieldFrom);
        hboxRange.add(Box.createHorizontalStrut(20));
        hboxRange.add(labelForTo);
        hboxRange.add(Box.createHorizontalStrut(10));
        hboxRange.add(textFieldTo);
        hboxRange.add(Box.createHorizontalStrut(20));
        hboxRange.add(labelForStep);
        hboxRange.add(Box.createHorizontalStrut(10));
        hboxRange.add(textFieldStep);
        hboxRange.add(Box.createHorizontalGlue());
        hboxRange.setPreferredSize(new Dimension( new Double(hboxRange.getMaximumSize().getWidth()).intValue(),
                new Double(hboxRange.getMinimumSize().getHeight()).intValue()*2));
        getContentPane().add(hboxRange, BorderLayout.NORTH);
        JButton buttonCalc = new JButton("Вычислить");
        buttonCalc.addActionListener(ev -> {
            try {
                renderer.setPalindrom(false);
                Double from = Double.parseDouble(textFieldFrom.getText());
                Double to = Double.parseDouble(textFieldTo.getText());
                Double step = Double.parseDouble(textFieldStep.getText());
                data = new GornerTableModel(from, to, step, MainFrame.this.coefficients);
                JTable table = new JTable(data);
                table.setDefaultRenderer(Double.class, renderer);
                table.setRowHeight(30);
                hBoxResult.removeAll();
                hBoxResult.add(new JScrollPane(table));
                getContentPane().validate();
                saveToTextMenuItem.setEnabled(true);
                saveToGraphicsMenuItem.setEnabled(true);
                searchValueMenuItem.setEnabled(true);
                palindromMenuItem.setEnabled(true);
                commaSeparatedValues.setEnabled(true);
                getContentPane().repaint();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(MainFrame.this,
                        "Ошибка в формате записи числа с плавающей точкой", "Ошибочный формат числа",
                        JOptionPane.WARNING_MESSAGE);
            }
        });
        JButton buttonReset = new JButton("Очистить поля");
        buttonReset.addActionListener(ev -> {
            renderer.setPalindrom(false);
            textFieldFrom.setText("0.0");
            textFieldTo.setText("1.0");
            textFieldStep.setText("0.1");

            hBoxResult.removeAll();
            hBoxResult.add(new JPanel());
            saveToTextMenuItem.setEnabled(false);
            saveToGraphicsMenuItem.setEnabled(false);
            searchValueMenuItem.setEnabled(false);
            getContentPane().validate();
            getContentPane().repaint();
        });
        Box hboxButtons = Box.createHorizontalBox();
        hboxButtons.setBorder(BorderFactory.createBevelBorder(1));
        hboxButtons.add(Box.createHorizontalGlue());
        hboxButtons.add(buttonCalc);
        hboxButtons.add(Box.createHorizontalStrut(30));
        hboxButtons.add(buttonReset);
        hboxButtons.add(Box.createHorizontalGlue());
        hboxButtons.setPreferredSize(new Dimension(new
                Double(hboxButtons.getMaximumSize().getWidth()).intValue(), new
                Double(hboxButtons.getMinimumSize().getHeight()).intValue()*2));
        getContentPane().add(hboxButtons, BorderLayout.SOUTH);
        hBoxResult = Box.createHorizontalBox();
        hBoxResult.add(new JPanel());
        getContentPane().add(hBoxResult, BorderLayout.CENTER);
    }
    private void saveToGraphicsFile(File selectedFile) {
        try {
            DataOutputStream out = new DataOutputStream(new
                    FileOutputStream(selectedFile));
            for (int i = 0; i<data.getRowCount(); i++) {
                out.writeDouble((Double)data.getValueAt(i,0));
                out.writeDouble((Double)data.getValueAt(i,1));
            }
            out.close();
        } catch (Exception ignored) {
        }
    }
    private void saveToTextFile(File selectedFile) {
        try {
            PrintStream out = new PrintStream(selectedFile);

            out.println("Результаты табулирования многочлена по схеме Горнера");
            out.print("Многочлен: ");
            for (int i=0; i<coefficients.length; i++) {
                out.print(coefficients[i] + "*X^" +
                        (coefficients.length-i-1));
                if (i!=coefficients.length-1)
                    out.print(" + ");
            }
            out.println("");
            out.println("Интервал от " + data.getFrom() + " до " +
                    data.getTo() + " с шагом " + data.getStep());
            out.println("====================================================");
            for (int i = 0; i<data.getRowCount(); i++) {
                out.println("Значение в точке " + data.getValueAt(i,0)
                        + " равно " + data.getValueAt(i,1));
            }
            out.close();
        } catch (FileNotFoundException ignored) {
        }
    }

    private void saveToCSVFile(File selectedFile)
    {
        try{
            CSVWriter writer = new CSVWriter(new FileWriter(selectedFile));
            String [] record = "Результаты табулирования многочлена по схеме Горнера".split(" ");
            writer.writeNext(record);
            String record1 = "Интервал от " + data.getFrom() + " до " + data.getTo() + " с шагом " +  data.getStep();
            record=record1.split(" ");
            writer.writeNext(record);
            record1="";
            for (int i = 0; i < data.getRowCount(); i++){
                for(int k = 0; k < data. getColumnCount(); k++)
                {
                    record1 = record1 + formatter.format(data.getValueAt(i, k)) + ", ";
                }
                record = record1.split(", ");
                writer.writeNext(record);
                record1="";
            }
            writer.close();
        }catch(Exception ignored){

        }
    }

    public static void main(String[] args) {
        if (args.length==0) {
            System.out.println("Невозможно табулировать многочлен, для которого не задано ни одного коэффициента!");
            System.exit(-1);
        }
        Double[] coefficients = new Double[args.length];
        int i = 0;
        try {
            for (String arg: args) {
                coefficients[i++] = Double.parseDouble(arg);
            }
        }
        catch (NumberFormatException ex) {
            System.out.println("Ошибка преобразования строки '" +
                    args[i] + "' в число типа Double");
            System.exit(-2);
        }
        MainFrame frame = new MainFrame(coefficients);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
