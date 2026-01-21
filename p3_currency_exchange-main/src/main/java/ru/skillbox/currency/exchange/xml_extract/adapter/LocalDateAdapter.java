package ru.skillbox.currency.exchange.xml_extract.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateAdapter extends XmlAdapter<String, LocalDate> {

    private final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @Override
    public LocalDate unmarshal(String s){
        return LocalDate.parse(s, dateFormat);
    }

    @Override
    public String marshal(LocalDate localDate) {
        return localDate.format(dateFormat);
    }
}
