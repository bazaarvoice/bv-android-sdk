package com.bazaarvoice.bvandroidsdk;

import java.util.List;
import java.util.Map;

/**
 * Created by Bazaarvoice on 3/29/16.
 */
public class Transaction {

    private Builder builder;

    private Transaction(Builder builder) {
        this.builder = builder;
    }

    protected String getOrderId(){return builder.orderId;}
    protected String getTotal(){return builder.total;}
    protected List<TransactionItem> getItems(){return builder.items;}
    protected Map<String, Object> getOtherParams(){return builder.otherParams;}
    protected String getTax(){return builder.tax;}
    protected String getShipping(){return builder.shipping;}
    protected String getCity(){return builder.city;}
    protected String getState(){return builder.state;}
    protected String getCountry(){return builder.country;}
    protected String getCurrency(){return builder.currency;}

    public static final class Builder {
        private String orderId;
        private String total;
        private List<TransactionItem> items;
        private Map<String, Object> otherParams;
        private String tax;
        private String shipping;
        private String city;
        private String state;
        private String country;
        private String currency;

        public Builder(String orderId, double total, List<TransactionItem> items, Map<String, Object> otherParams) {
            assert (orderId != null);
            assert (items != null);

            this.orderId = orderId;
            this.total = ConversionUtils.formatDouble(total);
            this.items = items;
            this.otherParams = otherParams;
        }

        public Builder tax(double tax){
            this.tax = ConversionUtils.formatDouble(tax);
            return this;
        }

        public Builder shipping(double shipping){
            this.shipping = ConversionUtils.formatDouble(shipping);
            return this;
        }

        public Builder city(String city){
            this.city = city;
            return this;
        }

        public Builder state(String state){
            this.state = state;
            return this;
        }

        public Builder country(String country){
            this.country = country;
            return this;
        }

        public Builder currency(String currency){
            this.currency = currency;
            return this;
        }

        public Transaction build(){
            return new Transaction(this);
        }
    }
}
