package hu.nje.mozifxml.service.mnb.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;

import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class Currencies {

    @XmlElement(name = "Curr")
    private List<String> curr;

    // Getters and setters
    public List<String> getCurr() {
        return curr;
    }

    public void setCurr(List<String> curr) {
        this.curr = curr;
    }
}
