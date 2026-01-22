package ru.skillbox.currency.exchange.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.skillbox.currency.exchange.dto.CurrencyDto;
import ru.skillbox.currency.exchange.dto.CurrencyDtoList;
import ru.skillbox.currency.exchange.entity.Currency;
import ru.skillbox.currency.exchange.mapper.CurrencyMapper;
import ru.skillbox.currency.exchange.repository.CurrencyRepository;
import ru.skillbox.currency.exchange.xml_extract.xml_dto.CurrencyXml;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyService {
    private final CurrencyMapper mapper;
    private final CurrencyRepository repository;
    private final CurrencyXmlExtractor extractor;

    public void updateCurrencies(){
            log.info("Начинаю обновление курсов валют");
            List<CurrencyXml> xmlCurrencies = extractor.extract().getValutes();
        if (xmlCurrencies.isEmpty()) {
            log.error("Не удалось извлечь курсы валют");
        } else {
            xmlCurrencies.forEach(v ->
                    repository.findByIsoCharCode(v.getIsoCharCode()).ifPresentOrElse(existingCurrency -> {
                        Currency updateCurrency = mapper.convertToEntityFromXml(v);
                        repository.save(mapper.updateEntity(existingCurrency,updateCurrency));
                        log.debug("Обновлена валюта: {}", v.getIsoCharCode());
                    }, () -> {
                        Currency currency = repository.save(mapper.convertToEntityFromXml(v));
                        log.debug("Создана новая валюта: {}", currency.getIsoCharCode());
                    })
            );
            log.info("Обновление курсов валют завершено");
        }
    }

    public CurrencyDtoList getList(){
        return new CurrencyDtoList(repository.findAll().stream()
                .map(mapper::convertToLiteDto)
                .collect(Collectors.toList()));
    }

    public CurrencyDto getById(Long id) {
        log.info("CurrencyService method getById executed");
        Currency currency = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Currency not found with id: " + id));
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
