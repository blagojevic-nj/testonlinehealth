package com.isamrs.onlinehealth.dto;

import java.util.ArrayList;

public class DataSetDTO {

    private String label;
    private ArrayList<Integer> data;
    private String backgroundColor = "rgba(75,192,192,1)";
    private String borderColor = "rgba(0,0,0,1)";
    private int borderWidth = 2;

    public DataSetDTO() {
        data = new ArrayList<Integer>();
    }

    public DataSetDTO(String label, ArrayList<Integer> data, ArrayList<String> colors) {
        this.label = label;
        this.data = data;
    }




    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public ArrayList<Integer> getData() {
        return data;
    }

    public void setData(ArrayList<Integer> data) {
        this.data = data;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(String borderColor) {
        this.borderColor = borderColor;
    }

    public int getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
    }
}
