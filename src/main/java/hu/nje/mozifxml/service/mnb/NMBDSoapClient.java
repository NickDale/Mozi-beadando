package hu.nje.mozifxml.service.mnb;

import hu.mnb.webservices.GetCurrenciesRequestBody;
import hu.mnb.webservices.GetCurrentExchangeRatesRequestBody;
import hu.mnb.webservices.GetExchangeRatesRequestBody;
import hu.mnb.webservices.MNBArfolyamServiceSoap;
import hu.mnb.webservices.MNBArfolyamServiceSoapGetCurrenciesStringFaultFaultMessage;
import hu.mnb.webservices.MNBArfolyamServiceSoapGetCurrentExchangeRatesStringFaultFaultMessage;
import hu.mnb.webservices.MNBArfolyamServiceSoapGetExchangeRatesStringFaultFaultMessage;
import hu.mnb.webservices.ObjectFactory;
import hu.nje.mozifxml.service.mnb.model.MNBCurrencies;
import hu.nje.mozifxml.util.Constant;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tempuri.MNBArfolyamServiceSoapImpl;

import java.io.StringReader;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;

import static java.util.Optional.ofNullable;

public class NMBDSoapClient {
    private static final Logger logger = LoggerFactory.getLogger(NMBDSoapClient.class);

    private final ObjectFactory objectFactory;
    private final MNBArfolyamServiceSoap service;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.systemDefault());

    public NMBDSoapClient() {
        objectFactory = new ObjectFactory();
        service = new MNBArfolyamServiceSoapImpl().getCustomBindingMNBArfolyamServiceSoap();
    }

    public List<String> getCurrencies() {
        try {
            final MNBCurrencies mnbCurrencies = this.unmarshal(
                    MNBCurrencies.class,
                    service.getCurrencies(new GetCurrenciesRequestBody()).getGetCurrenciesResult().getValue()
            );
            return mnbCurrencies.getCurrencies().getCurr();
        } catch (MNBArfolyamServiceSoapGetCurrenciesStringFaultFaultMessage ex) {
            throw new RuntimeException(ex);
        }
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
            final var exchangeRatesRequestBody = new GetExchangeRatesRequestBody();
            exchangeRatesRequestBody.setCurrencyNames(
                    objectFactory.createGetExchangeRatesRequestBodyCurrencyNames(
                            String.join(",", ofNullable(currencies).filter(Constant::isNotEmpty).orElse(this.getCurrencies())
                            )
                    )
            );
            exchangeRatesRequestBody.setStartDate(
                    objectFactory.createGetExchangeRatesRequestBodyStartDate(start.format(dateFormatter))
            );
            exchangeRatesRequestBody.setStartDate(
                    objectFactory.createGetExchangeRatesRequestBodyEndDate(end.format(dateFormatter))
            );
            return service.getExchangeRates(exchangeRatesRequestBody).getGetExchangeRatesResult().getValue();
        } catch (MNBArfolyamServiceSoapGetExchangeRatesStringFaultFaultMessage ex) {
            throw new RuntimeException(ex);
        }
    }

    public String getCurrentExchangeRatesAsXML() {
        try {
            return service.getCurrentExchangeRates(new GetCurrentExchangeRatesRequestBody())
                    .getGetCurrentExchangeRatesResult().getValue();
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
