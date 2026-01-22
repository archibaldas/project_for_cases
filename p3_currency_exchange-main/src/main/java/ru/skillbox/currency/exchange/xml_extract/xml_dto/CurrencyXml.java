package ru.skillbox.currency.exchange.xml_extract.xml_dto;

import lombok.Data;
import ru.skillbox.currency.exchange.xml_extract.adapter.DecimalAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigDecimal;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class CurrencyXml {

    @XmlElement(name = "NumCode")
    private Long isoNumCode;

    @XmlElement(name = "CharCode")
    private String isoCharCode;

    @XmlElement(name = "Nominal")
    private Long nominal;

    @XmlElement(name = "Name")
    private String name;

    @XmlElement(name = "Value")
    @XmlJavaTypeAdapter(DecimalAdapter.class)
    private BigDecimal value;
}
