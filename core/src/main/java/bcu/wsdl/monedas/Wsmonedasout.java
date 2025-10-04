
package bcu.wsdl.monedas;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for wsmonedasout complex type</p>.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 *
 * <pre>{@code
 * <complexType name="wsmonedasout">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="wsmonedasout.Linea" type="{Cotiza}wsmonedasout.Linea" maxOccurs="unbounded" minOccurs="0"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 */
@XmlAccessorType (XmlAccessType.FIELD)
@XmlType (name = "wsmonedasout", propOrder = {
        "wsmonedasoutLinea"
})
public class Wsmonedasout {
    
    @XmlElement (name = "wsmonedasout.Linea")
    protected List<WsmonedasoutLinea> wsmonedasoutLinea;
    
    /**
     * Gets the value of the wsmonedasoutLinea property.
     *
     * <p>This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the wsmonedasoutLinea property.</p>
     *
     * <p>
     * For example, to add a new item, do as follows:
     * </p>
     * <pre>
     * getWsmonedasoutLinea().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link WsmonedasoutLinea }
     * </p>
     *
     * @return The value of the wsmonedasoutLinea property.
     */
    public List<WsmonedasoutLinea> getWsmonedasoutLinea() {
        if (wsmonedasoutLinea == null) {
            wsmonedasoutLinea = new ArrayList<>();
        }
        return this.wsmonedasoutLinea;
    }
    
}
