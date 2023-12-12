package com.saga.orchestrator.orchestrator.model;

public class Payment {

    String paymentType;
    Double value;
    Datapayments dataPayments;

    public static Payment issueToPayment(Issue issue) {

        Payment payment = new Payment();

        payment.setValue(issue.getPayment().getValue());
        payment.setPaymentType(issue.getPayment().getPaymentType());

        Datapayments datapayments = new Datapayments();
        datapayments.setCardHolderName(issue.getPayment().getDataPayments().getCardHolderName());
        datapayments.setNumber(issue.getPayment().getDataPayments().getNumber());
        datapayments.setCvvCode(issue.getPayment().getDataPayments().getCvvCode());
        datapayments.setExpirationDate(issue.getPayment().getDataPayments().getExpirationDate());

        payment.setDataPayments(datapayments);

        return payment;

    }


    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Datapayments getDataPayments() {
        return dataPayments;
    }

    public void setDataPayments(Datapayments dataPayments) {
        this.dataPayments = dataPayments;
    }
}
