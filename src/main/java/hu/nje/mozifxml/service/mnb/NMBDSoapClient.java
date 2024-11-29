package hu.nje.mozifxml.service.mnb;

import hu.nje.mnb.generated.MNBArfolyamServiceSoap;
import hu.nje.mnb.generated.MNBArfolyamServiceSoapGetCurrenciesStringFaultFaultMessage;
import hu.nje.mnb.generated.MNBArfolyamServiceSoapGetCurrentExchangeRatesStringFaultFaultMessage;
import hu.nje.mnb.generated.MNBArfolyamServiceSoapGetExchangeRatesStringFaultFaultMessage;
import hu.nje.mnb.generated.MNBArfolyamServiceSoapImpl;
import hu.nje.mozifxml.service.mnb.model.MNBCurrencies;
import hu.nje.mozifxml.service.mnb.model.MNBExchangeRates;
import hu.nje.mozifxml.util.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;

import static java.util.Optional.ofNullable;

public class NMBDSoapClient {
    private static final Logger logger = LoggerFactory.getLogger(NMBDSoapClient.class);

    private final MNBArfolyamServiceSoap service;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.systemDefault());

    public NMBDSoapClient() {
        service = new MNBArfolyamServiceSoapImpl().getCustomBindingMNBArfolyamServiceSoap();
    }

    public List<String> getCurrencies() {
        try {
            final MNBCurrencies mnbCurrencies = this.unmarshal(
                    MNBCurrencies.class,
                    service.getCurrencies()
            );
            return mnbCurrencies.getCurrencies().getCurr();
        } catch (MNBArfolyamServiceSoapGetCurrenciesStringFaultFaultMessage ex) {
            throw new RuntimeException(ex);
        }
    }

    public MNBExchangeRates getAllExchangeRates(final LocalDate startDate,
                                      final LocalDate endDate,
                                      final Collection<String> currencies) {
       final String allExchangeRatesAsStringByFilter = this.getAllExchangeRatesAsStringByFilter(startDate, endDate, currencies);
        return this.unmarshal(
                MNBExchangeRates.class,
                allExchangeRatesAsStringByFilter
        );
    }

    public String getAllExchangeRatesAsString() {
        return this.getAllExchangeRatesAsStringByFilter(null, null, null);
    }

    public String getAllExchangeRatesAsStringByFilter(final LocalDate startDate,
                                                      final LocalDate endDate,
                                                      final Collection<String> currencies) {
        try {
            final var today = LocalDate.now();
            final LocalDate start = ofNullable(startDate).orElse(LocalDate.ofYearDay(today.getYear(), 1));
            final LocalDate end = ofNullable(endDate).orElse(LocalDate.of(today.getYear(), 12, 31));

            final String currencyNames = String.join(",",
                    ofNullable(currencies).filter(Constant::isNotEmpty).orElse(this.getCurrencies())
            );
            return service.getExchangeRates(
                    start.format(dateFormatter),
                    end.format(dateFormatter),
                    currencyNames
            );
        } catch (MNBArfolyamServiceSoapGetExchangeRatesStringFaultFaultMessage ex) {
            throw new RuntimeException(ex);
        }
    }

    public String getCurrentExchangeRatesAsXML() {
        try {
            return service.getCurrentExchangeRates();
        } catch (MNBArfolyamServiceSoapGetCurrentExchangeRatesStringFaultFaultMessage e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("all")
    private <T> T unmarshal(Class<T> tClass, String message) {
        try {
            JAXBContext context = JAXBContext.newInstance(tClass);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return (T) unmarshaller.unmarshal(new StringReader(message));
        } catch (JAXBException ex) {
            logger.error("Exception in getCurrentExchangeRates", ex);
            throw new RuntimeException(ex);
        }
    }
}
