package com.saga.orchestrator.orchestrator.model;

public class Payment {

    String orderId;
    String paymentType;
    Double paymentValue;
    Datapayments dataPayments;

    String paymentId;

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public static Payment issueToPayment(Issue issue) {

        Payment payment = new Payment();

        payment.setOrderId(issue.getOrder().getCodPedido());
        payment.setPaymentValue(issue.getPayment().getPaymentValue());
        payment.setPaymentType(issue.getPayment().getPaymentType());

        Datapayments datapayments = new Datapayments();
        datapayments.setCardHolderName(issue.getPayment().getDataPayments().getCardHolderName());
        datapayments.setNumber(issue.getPayment().getDataPayments().getNumber());
        datapayments.setCvvCode(issue.getPayment().getDataPayments().getCvvCode());
        datapayments.setExpirationDate(issue.getPayment().getDataPayments().getExpirationDate());

        payment.setDataPayments(datapayments);

        return payment;

    }


    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public Double getPaymentValue() {
        return paymentValue;
    }

    public void setPaymentValue(Double paymentValue) {
        this.paymentValue = paymentValue;
    }

    public Datapayments getDataPayments() {
        return dataPayments;
    }

    public void setDataPayments(Datapayments dataPayments) {
        this.dataPayments = dataPayments;
    }
}
