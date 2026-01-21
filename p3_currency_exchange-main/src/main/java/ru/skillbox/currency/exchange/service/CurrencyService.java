package ru.skillbox.currency.exchange.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.skillbox.currency.exchange.configuration.CurrencyConfig;
import ru.skillbox.currency.exchange.dto.CurrencyDto;
import ru.skillbox.currency.exchange.dto.CurrencyDtoList;
import ru.skillbox.currency.exchange.dto.CurrencyLiteDto;
import ru.skillbox.currency.exchange.entity.Currency;
import ru.skillbox.currency.exchange.mapper.CurrencyMapper;
import ru.skillbox.currency.exchange.repository.CurrencyRepository;
import ru.skillbox.currency.exchange.xml_extract.xml_dto.ValCurs;
import ru.skillbox.currency.exchange.xml_extract.xml_dto.Valute;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyService {
    private final CurrencyMapper mapper;
    private final CurrencyRepository repository;
    private final CurrencyConfig currencyConfig;
    private final RestTemplate restTemplate = new RestTemplate();

    public void updateCurrencies(){
        try {
            log.info("Начинаю обновление курсов валют");

            String xmlResponse = restTemplate
                    .getForObject(currencyConfig.getCbrUrl(),
                            String.class);

            JAXBContext jaxbContext = JAXBContext.newInstance(ValCurs.class);

            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            ValCurs valCurs = (ValCurs) unmarshaller.unmarshal(new StringReader(xmlResponse));

            for(Valute valute : valCurs.getValutes()){
                updateOrCreateCurrency(valute);
            }

            log.info("Обновление курсов валют завершено");
        } catch (JAXBException e) {
            log.error("Ошибка при обновлении курсов валют: {}", e.getMessage());
        }
    }

    private void updateOrCreateCurrency(Valute valute) {
        BigDecimal rateForOne = valute.getValue()
                .divide(BigDecimal.valueOf(valute.getNominal()), 6, RoundingMode.HALF_UP);

        repository.findByIsoCharCode(valute.getCharCode()).ifPresentOrElse(existingCurrency -> {
            existingCurrency.setValue(rateForOne.doubleValue());
            existingCurrency.setName(valute.getName());
            existingCurrency.setNominal((long) valute.getNominal());
            existingCurrency.setIsoNumCode(Long.valueOf(valute.getNumCode()));
            existingCurrency.setIsoCharCode(valute.getCharCode());
            repository.save(existingCurrency);
            log.debug("Обновлена валюта: {}", valute.getCharCode());
        }, () -> {
            Currency newCurrency = Currency.builder()
                    .isoCharCode(valute.getCharCode())
                    .value(valute.getValue().doubleValue())
                    .name(valute.getName())
                    .nominal((long)valute.getNominal())
                    .isoNumCode(Long.valueOf(valute.getNumCode()))
                    .isoCharCode(valute.getCharCode())
                    .build();
            repository.save(newCurrency);
            log.debug("Создана новая валюта: {}", valute.getCharCode());
        });
    }

    public List<CurrencyDto> getAll(){
        return repository.findAll().stream()
                .map(mapper::convertToDto)
                .collect(Collectors.toList());
    }

    public CurrencyDtoList getList(){
        return new CurrencyDtoList(repository.findAll().stream()
                .map(mapper::convertToLiteDto)
                .collect(Collectors.toList()));
    }

    public CurrencyDto getById(Long id) {
        log.info("CurrencyService method getById executed");
        Currency currency = repository.findById(id).orElseThrow(() -> new RuntimeException("Currency not found with id: " + id));
        return mapper.convertToDto(currency);
    }

    public Double convertValue(Long value, Long numCode) {
        log.info("CurrencyService method convertValue executed");
        Currency currency = repository.findByIsoNumCode(numCode);
        return value * currency.getValue();
    }

    public CurrencyDto create(CurrencyDto dto) {
        log.info("CurrencyService method create executed");
        return  mapper.convertToDto(repository.save(mapper.convertToEntity(dto)));
    }
}
