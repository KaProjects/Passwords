package org.kaleta.passman.data.model;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Stanislav Kaleta
 * Date: 23.6.2015
 */
@XmlType(name = "documenType")
@XmlRootElement(name = "document")
@XmlAccessorType(XmlAccessType.FIELD)
public class Document {

    @XmlAttribute(required = true,name = "password")
    private String password;

    @XmlElement(name = "data")
    private List<Document.Data> dataList;

    public List<Document.Data> getDataList() {
        if (dataList == null) {
            dataList = new ArrayList<>();
        }
        return this.dataList;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "dataType")
    public static class Data{
        @XmlAttribute(required = true)
        public String hash;

        public String getHash() {
            return hash;
        }

        public void setHash(String hash) {
            this.hash = hash;
        }
    }
}
