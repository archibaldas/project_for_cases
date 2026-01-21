package ru.skillbox.currency.exchange.xml_extract.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.math.BigDecimal;

public class DecimalAdapter extends XmlAdapter<String, BigDecimal> {
    @Override
    public BigDecimal unmarshal(String s) throws Exception {
        return new BigDecimal(s.replace(",", "."));
    }

    @Override
    public String marshal(BigDecimal bigDecimal) throws Exception {
        return bigDecimal.toString().replace(".", ",");
    }
}
