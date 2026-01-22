package ru.skillbox.currency.exchange.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.skillbox.currency.exchange.configuration.CurrencyConfig;
import ru.skillbox.currency.exchange.xml_extract.xml_dto.CurrencyXmlList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class CurrencyXmlExtractor {

    private final CurrencyConfig currencyConfig;
    private final RestTemplate restTemplate = new RestTemplate();

    public CurrencyXmlList extract(){

        CurrencyXmlList currencyXmlList = new CurrencyXmlList();
        try {
            String xmlResponse = restTemplate
                    .getForObject(currencyConfig.getCbrUrl(),
                            String.class);
            JAXBContext jaxbContext = JAXBContext.newInstance(CurrencyXmlList.class);

            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

             currencyXmlList = (CurrencyXmlList) unmarshaller
                    .unmarshal(new StringReader(Objects.requireNonNull(xmlResponse)));
        } catch (JAXBException e){
            log.error("Ошибка извлечения курса валют из ЦБ");
        }

        return currencyXmlList;
    }
}
