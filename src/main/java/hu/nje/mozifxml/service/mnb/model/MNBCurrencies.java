package hu.nje.mozifxml.service.mnb.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "MNBCurrencies")
@XmlAccessorType(XmlAccessType.FIELD)
public class MNBCurrencies {

    @XmlElement(name = "Currencies")
    private Currencies currencies;

    public Currencies getCurrencies() {
        return currencies;
    }

    public void setCurrencies(Currencies currencies) {
        this.currencies = currencies;
    }
}