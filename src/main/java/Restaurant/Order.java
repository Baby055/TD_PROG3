package Restaurant;

import java.time.Instant;

public class Order {
    private Integer id;
    private String reference;
    private Instant creationDatetime;
    private PaymentStatusEnum paymentStatus;
    private Sale sale;

    public Order(Integer id, String reference, Instant creationDatetime, PaymentStatusEnum paymentStatus, Sale sale) {
        this.id = id;
        this.reference = reference;
        this.creationDatetime = creationDatetime;
        this.paymentStatus = paymentStatus;
        this.sale = sale;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Instant getCreationDatetime() {
        return creationDatetime;
    }

    public void setCreationDatetime(Instant creationDatetime) {
        this.creationDatetime = creationDatetime;
    }

    public PaymentStatusEnum getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatusEnum paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Sale getSale() {
        return sale;
    }

    public void setSale(Sale sale) {
        this.sale = sale;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", reference='" + reference + '\'' +
                ", creationDatetime=" + creationDatetime +
                ", paymentStatus=" + paymentStatus +
                ", sale=" + (sale != null ? "Sale{id=" + sale.getId() + "}" : "null") +
                '}';
    }
}

