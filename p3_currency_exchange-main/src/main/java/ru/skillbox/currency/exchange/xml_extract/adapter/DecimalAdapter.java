package ru.skillbox.currency.exchange.xml_extract.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.math.BigDecimal;

public class DecimalAdapter extends XmlAdapter<String, BigDecimal> {
    @Override
    public BigDecimal unmarshal(String s){
        return new BigDecimal(s.replace(",", "."));
    }

    @Override
    public String marshal(BigDecimal bigDecimal){
        return bigDecimal.toString().replace(".", ",");
    }
}
