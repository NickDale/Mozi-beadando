package hu.nje.mozifxml.service;

import hu.nje.mozifxml.service.mnb.NMBDSoapClient;
import hu.nje.mozifxml.service.mnb.model.Day;
import hu.nje.mozifxml.service.mnb.model.MNBExchangeRates;
import hu.nje.mozifxml.util.Constant;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;

import static java.util.Optional.ofNullable;

public class MNBService {

    private static final String MNB_EXPORT_FILE_NAME = "MNB.txt";

    private final NMBDSoapClient soapClient;
    private final BiFunction<Window, String, File> chooseFileFunction = (window, fileName) -> {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Text Files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setInitialFileName(fileName);

        return fileChooser.showSaveDialog(window);
    };

    public MNBService() {
        this.soapClient = new NMBDSoapClient();
    }

    public List<String> currencies() {
        return soapClient.getCurrencies();
    }

    public List<Day> mnbExchangeRates(final Collection<String> currencies) {
        return this.mnbExchangeRates(null, null, currencies);
    }

    public List<Day> mnbExchangeRates(final LocalDate startDate, final LocalDate endDate, final Collection<String> currencies) {
        MNBExchangeRates allExchangeRates = soapClient.getAllExchangeRates(null, null, currencies);
        return allExchangeRates.getDays().stream()
                .filter(
                        day -> !LocalDate.parse(day.getDate(), DateTimeFormatter.ISO_DATE).isBefore(
                                ofNullable(startDate).orElse(LocalDate.now().minusMonths(1))
                        )
                                && !LocalDate.parse(day.getDate(), DateTimeFormatter.ISO_DATE).isAfter(
                                ofNullable(endDate).orElse(LocalDate.now())
                        )
                ).toList();
    }

    public void downloadAll(final Window window) {
        final String allExchangeRatesAsXML = soapClient.getAllExchangeRatesAsString();

        File file = this.chooseFileFunction.apply(window, MNB_EXPORT_FILE_NAME);
        if (file != null) {
            try {
                this.writeXML(file, allExchangeRatesAsXML);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public void downloadByInputs(final Window window, final String fileName,
                                 final LocalDate startDate, final LocalDate endDate,
                                 final Collection<String> currencies) {
        final String xmlResponse = soapClient.getAllExchangeRatesAsStringByFilter(startDate, endDate, currencies);
        File file = this.chooseFileFunction.apply(window, ofNullable(fileName).filter(Constant::isNotEmpty).orElse(MNB_EXPORT_FILE_NAME));
        if (file != null) {
            try {
                this.writeXML(file, xmlResponse);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private void writeXML(final File file, final String xml) throws ParserConfigurationException,
            TransformerException, IOException, SAXException {
        final Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                .parse(new ByteArrayInputStream(xml.getBytes()));

        final Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xalan}indent-amount", "2");

        transformer.transform(new DOMSource(doc), new StreamResult(file));
    }

}
