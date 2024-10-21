package org.kaleta.passman.data;

import org.kaleta.passman.data.model.Document;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;


/**
 * User: Stanislav Kaleta
 * Date: 23.6.2015
 */
public class XmlManager implements Manager{
    private final String schemaUri = "/schema.xsd";
    private final File file;


    public XmlManager(){
        file = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath() + "data/db.xml");
    }

    @Override
    public void updateDocument(Document document) throws ManagerException{
        try {
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory.newSchema(this.getClass().getResource(schemaUri));

            JAXBContext context = JAXBContext.newInstance(org.kaleta.passman.data.model.Document.class);

            Marshaller marshaller = context.createMarshaller();
            marshaller.setSchema(schema);
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            marshaller.marshal(document, file);
        } catch (SAXException | JAXBException e) {
            throw new ManagerException(e.getCause());
        }
    }

    @Override
    public Document retrieveDocument() throws ManagerException{
        Document document = null;
        try {
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory.newSchema(this.getClass().getResource(schemaUri));

            JAXBContext context = JAXBContext.newInstance(org.kaleta.passman.data.model.Document.class);

            Unmarshaller unmarshaller = context.createUnmarshaller();
            unmarshaller.setSchema(schema);
            document = (Document) unmarshaller.unmarshal(file);
        } catch (JAXBException | SAXException e) {
            throw new ManagerException(e.getCause());
        }
        return document;
    }

}
