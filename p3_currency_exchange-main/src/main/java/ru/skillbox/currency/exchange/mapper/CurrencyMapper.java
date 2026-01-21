package ru.skillbox.currency.exchange.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.skillbox.currency.exchange.dto.CurrencyDto;
import ru.skillbox.currency.exchange.dto.CurrencyLiteDto;
import ru.skillbox.currency.exchange.entity.Currency;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CurrencyMapper {

    CurrencyDto convertToDto(Currency currency);
    CurrencyLiteDto convertToLiteDto(Currency currency);

    Currency convertToEntity(CurrencyDto currencyDto);
}
