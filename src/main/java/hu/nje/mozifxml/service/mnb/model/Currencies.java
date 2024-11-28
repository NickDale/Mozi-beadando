package hu.nje.mozifxml.service.mnb.model;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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
