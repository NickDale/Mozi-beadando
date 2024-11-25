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
import hu.nje.mozifxml.service.mnb.model.MNBExchangeRates;
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
import java.util.List;

public class NMBDSoapClient {
    private static final Logger logger = LoggerFactory.getLogger(NMBDSoapClient.class);

    private final ObjectFactory objectFactory;
    private final MNBArfolyamServiceSoap service;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.systemDefault());

    public NMBDSoapClient() {
        objectFactory = new ObjectFactory();
        service = new MNBArfolyamServiceSoapImpl().getCustomBindingMNBArfolyamServiceSoap();
    }

    public String getAllExchangeRatesAsXML() {
        try {

            GetExchangeRatesRequestBody exchangeRatesRequestBody = new GetExchangeRatesRequestBody();
            exchangeRatesRequestBody.setCurrencyNames(objectFactory.createGetExchangeRatesRequestBodyCurrencyNames(""));
            exchangeRatesRequestBody.setStartDate(objectFactory.createGetExchangeRatesRequestBodyStartDate("2024-01-01"));
            exchangeRatesRequestBody.setStartDate(objectFactory.createGetExchangeRatesRequestBodyEndDate("2024-11-05"));
            return service.getExchangeRates(exchangeRatesRequestBody).getGetExchangeRatesResult().getValue();

        } catch (Exception ex) {
            throw new RuntimeException("");
        }
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
        try {
            List<String> currencies = this.getCurrencies();
            final var today = LocalDate.now();
            final LocalDate start = LocalDate.ofYearDay(today.getYear(), 1);
            final LocalDate end = LocalDate.of(today.getYear(), 12, 31);
            final var exchangeRatesRequestBody = new GetExchangeRatesRequestBody();
            exchangeRatesRequestBody.setCurrencyNames(
                    objectFactory.createGetExchangeRatesRequestBodyCurrencyNames(String.join(",", currencies))
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

    public MNBExchangeRates getCurrentExchangeRates() {
        try {
            return this.unmarshal(MNBExchangeRates.class, this.getCurrentExchangeRatesAsXML());
        } catch (Exception ex) {
            logger.error("Exception in getCurrentExchangeRates", ex);
        }
        return null;
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
