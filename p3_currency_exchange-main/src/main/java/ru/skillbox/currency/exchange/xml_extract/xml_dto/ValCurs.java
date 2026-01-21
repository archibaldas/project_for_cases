package ru.skillbox.currency.exchange.xml_extract.xml_dto;

import lombok.Data;
import ru.skillbox.currency.exchange.xml_extract.adapter.LocalDateAdapter;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;
import java.util.List;

@Data
@XmlRootElement(name = "ValCurs")
@XmlAccessorType(XmlAccessType.FIELD)
public class ValCurs {
    @XmlAttribute(name = "Date")
    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate date;

    @XmlAttribute(name ="name")
    private String name;

    @XmlElement(name = "Valute")
    private List<Valute> valutes;
}
