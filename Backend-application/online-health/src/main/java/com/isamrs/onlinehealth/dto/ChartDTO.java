package com.isamrs.onlinehealth.dto;

import java.util.ArrayList;

public class ChartDTO {
    private String[] labels;
    private DataSetDTO[] datasets;


    public ChartDTO(String[] labels, DataSetDTO[] dataSet) {
        this.labels = labels;
        this.datasets = dataSet;
    }

    public ChartDTO() {
    }

    public ChartDTO(String[] labels, int numOfDatasets) {
        this.labels = labels;
        this.datasets = new DataSetDTO[numOfDatasets];
        for(int i = 0; i < numOfDatasets; i++)
            datasets[i]= new DataSetDTO();

    }

    public String[] getLabels() {
        return labels;
    }

    public void setLabels(String[] labels) {
        this.labels = labels;
    }

    public DataSetDTO[] getdatasets() {
        return datasets;
    }

    public void setdatasets(DataSetDTO[] dataSet) {
        this.datasets = dataSet;
    }
}
