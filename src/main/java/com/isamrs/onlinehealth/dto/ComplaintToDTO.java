package com.isamrs.onlinehealth.dto;

import java.util.List;

public class ComplaintToDTO {
    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }

    private List<String> items;

    public ComplaintToDTO() {
    }

    public ComplaintToDTO(List<String> items) {
        this.items = items;
    }
}
