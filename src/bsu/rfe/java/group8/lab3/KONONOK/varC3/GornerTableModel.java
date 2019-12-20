package bsu.rfe.java.group8.lab3.KONONOK.varC3;

import javax.swing.table.AbstractTableModel;
@SuppressWarnings("serial")

public class GornerTableModel extends AbstractTableModel {
    private Double[] coefficients;
    private Double from;
    private Double to;
    private Double step;
    private Double[] result = new Double[1];

    GornerTableModel(Double from, Double to, Double step,
                     Double[] coefficients) {
        this.from = from;
        this.to = to;
        this.step = step;
        this.coefficients = coefficients;
    }
    Double getFrom() {
        return from;
    }
    Double getTo() {
        return to;
    }
    Double getStep() {
        return step;
    }
    public int getColumnCount() {
        return 2;
    }
    public int getRowCount() {
        return new Double(Math.ceil((to-from)/step)).intValue()+1;
    }
    public Object getValueAt(int row, int col) {
        double x = from + step*row;
        switch (col){
            case 0:
                return x;
            case 1:
            {
                result[0] = 0.0;
                for(int i = 0; i < coefficients.length; i++){
                    result[0] += Math.pow(x, coefficients.length-1-i)*coefficients[i];
                }
                return result[0];
            }
            //case 2:
            default:
            {
                /*result[1] = 0.0;
                for(int i = 0; i < coefficients.length; i++){
                    result[1] += Math.pow(x, coefficients.length-1-i)*coefficients[i].floatValue();
                }
                return result[1].floatValue();*/
                return result[0]+10*Math.sin(result[0]);
            }
            /*default: {
                result[2] = Math.abs(result[0] - result[1]);
                return result[2].floatValue();
            }*/
        }
    }
    public String getColumnName(int col) {
        switch (col) {
            case 0:
                return "Значение X";
            case 1:
                return "Значение многочлена";
            case 2:
                return "Значение многочлена (float)";
            default:
                return "Разница";
        }
    }
    public Class<?> getColumnClass(int col) {
        return Double.class;
    }
}
