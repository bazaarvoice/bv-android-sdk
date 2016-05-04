package com.bazaarvoice.bvandroidsdk;

import java.util.Map;

/**
 * Created by Bazaarvoice on 3/30/16.
 */
public class Conversion {

    private String type, value, label;
    private Map<String, Object> otherParams;

    public Conversion(String type, String value, String label, Map<String, Object> otherParams){
        this.type = type;
        this.value = value;
        this.label = label;
        this.otherParams = otherParams;
    }

    public String getType(){
        return type;
    }

    public String getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

    public Map<String, Object> getOtherParams(){
        return otherParams;
    }
}
