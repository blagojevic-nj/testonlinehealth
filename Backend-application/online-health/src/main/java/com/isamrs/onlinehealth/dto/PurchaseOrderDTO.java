package com.isamrs.onlinehealth.dto;

import com.isamrs.onlinehealth.model.Medicine;
import com.isamrs.onlinehealth.model.PurchaseOrder;
import com.isamrs.onlinehealth.model.PurchaseOrderStatus;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PurchaseOrderDTO {

    private List<PurchaseOrderItemDTO> medicines;
    private LocalDate rok;
    private boolean myOrder;
    private PurchaseOrderStatus status;
    private Long id;


    public PurchaseOrderDTO() {
        this.myOrder=false;
    }

    public PurchaseOrderDTO(PurchaseOrder order, boolean isMyOrder) {
        Map<Medicine,Integer> orderMap = order.getMedicineQuantity();
        this.medicines=new ArrayList<PurchaseOrderItemDTO>();
        this.id = order.getId();

        for(Medicine m :orderMap.keySet())
        {
            PurchaseOrderItemDTO item = new PurchaseOrderItemDTO(m,orderMap.get(m));
            this.medicines.add(item);
        }
        this.myOrder=isMyOrder;
        this.rok=order.getDueDate().toLocalDate();
        this.status = order.getStatus();
    }

    public PurchaseOrderDTO(List<PurchaseOrderItemDTO> medicines, LocalDate rok, boolean myOrder) {
        this.medicines = medicines;
        this.rok = rok;
        this.myOrder = myOrder;
    }

    public PurchaseOrderStatus getStatus() {
        return status;
    }

    public void setStatus(PurchaseOrderStatus status) {
        this.status = status;
    }

    public List<PurchaseOrderItemDTO> getMedicines() {
        return medicines;
    }

    public void setMedicines(List<PurchaseOrderItemDTO> medicines) {
        this.medicines = medicines;
    }

    public LocalDate getRok() {
        return rok;
    }

    public void setRok(LocalDate rok) {
        this.rok = rok;
    }

    public boolean isMyOrder() {
        return myOrder;
    }

    public void setMyOrder(boolean myOrder) {
        this.myOrder = myOrder;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PurchaseOrderDTO that = (PurchaseOrderDTO) o;
        return myOrder == that.myOrder &&
                Objects.equals(medicines, that.medicines) &&
                Objects.equals(rok, that.rok) &&
                status == that.status &&
                Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(medicines, rok, myOrder, status, id);
    }
}
