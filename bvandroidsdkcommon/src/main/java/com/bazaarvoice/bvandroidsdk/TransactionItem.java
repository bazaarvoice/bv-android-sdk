package com.bazaarvoice.bvandroidsdk;

/**
 * Created by Bazaarvoice on 3/29/16.
 */
public class TransactionItem {

    private Builder builder;
    private TransactionItem(Builder builder) {
        this.builder = builder;
    }

    protected String getSku(){return builder.sku;}
    protected String getName(){return builder.name;}
    protected String getImageUrl(){return builder.imageUrl;}
    protected String getCategory(){return builder.category;}
    protected String getPrice(){return builder.price;}
    protected String getQuantity(){return builder.quantity;}

    public static final class Builder{

        private String sku;
        private String name;
        private String imageUrl;
        private  String category;
        private String price;
        private String quantity;

        public Builder(String sku){
            assert (sku != null);
            this.sku = sku;
        }

        public Builder name(String name){
            this.name = name;
            return this;
        }

        public Builder imageUrl(String imageUrl){
            this.imageUrl = imageUrl;
            return this;
        }

        public  Builder category(String category){
            this.category = category;
            return this;
        }

        public Builder price(double price) {
            this.price = ConversionUtils.formatDouble(price);
            return this;
        }

        public Builder quantity(int quantity){
            this.quantity = "" + quantity;
            return this;
        }

        public TransactionItem build(){
            return new TransactionItem(this);
        }
    }
}
