package ru.skillbox.currency.exchange.mapper;

import org.mapstruct.*;
import ru.skillbox.currency.exchange.dto.CurrencyDto;
import ru.skillbox.currency.exchange.dto.CurrencyLiteDto;
import ru.skillbox.currency.exchange.entity.Currency;
import ru.skillbox.currency.exchange.xml_extract.xml_dto.CurrencyXml;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CurrencyMapper {

    CurrencyDto convertToDto(Currency currency);
    CurrencyLiteDto convertToLiteDto(Currency currency);

    Currency convertToEntity(CurrencyDto currencyDto);
    @Mapping(target = "value", expression = "java(currencyXml.getValue().doubleValue())")
    Currency convertToEntityFromXml(CurrencyXml currencyXml);

    Currency updateEntity(Currency currency, @MappingTarget Currency newCurrency);
}
