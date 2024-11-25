package hu.nje.mozifxml.service;

import hu.nje.mozifxml.service.mnb.NMBDSoapClient;
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
import java.nio.file.Files;
import java.util.function.Function;

public class MNBService {

    private static final String MNB_EXPORT_FILE_NAME = "MNB.txt";

    private final NMBDSoapClient soapClient;
    private final Function<Window, File> chooseFileFunction = window -> {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Text Files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        fileChooser.setInitialFileName(MNB_EXPORT_FILE_NAME);

        return fileChooser.showSaveDialog(window);
    };

    public MNBService() {
        this.soapClient = new NMBDSoapClient();
    }

    public void downloadAll(Window window) {
        final String allExchangeRatesAsXML = soapClient.getAllExchangeRatesAsString();

        File file = this.chooseFileFunction.apply(window);
        if (file != null) {
            try {
                this.writeXML(file, allExchangeRatesAsXML);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public void downloadByInputs(Window window) {
        //TODO: implement

        File file = this.chooseFileFunction.apply(window);
        if (file != null) {
            try {
                this.writeXML(file, null);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private void writeXML(final File file, final String xml) throws ParserConfigurationException,
            TransformerException, IOException, SAXException {
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                .parse(new ByteArrayInputStream(xml.getBytes()));

        final Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xalan}indent-amount", "2");

        transformer.transform(new DOMSource(doc), new StreamResult(file));
    }

    private void writeToFile(final File file) {
        try {
            Files.writeString(file.toPath(), "tsfdsfsdfdsfdsfdsfdsfdsf");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
