package hu.nje.mozifxml.service.mnb.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

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