
package bcu.wsdl.cotizaciones;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for datoscotizaciones complex type</p>.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 *
 * <pre>{@code
 * <complexType name="datoscotizaciones">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="datoscotizaciones.dato" type="{Cotiza}datoscotizaciones.dato" maxOccurs="unbounded" minOccurs="0"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 */
@XmlAccessorType (XmlAccessType.FIELD)
@XmlType (name = "datoscotizaciones", propOrder = {
        "datoscotizacionesDato"
})
public class Datoscotizaciones {
    
    @XmlElement (name = "datoscotizaciones.dato")
    protected List<DatoscotizacionesDato> datoscotizacionesDato;
    
    /**
     * Gets the value of the datoscotizacionesDato property.
     *
     * <p>This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the datoscotizacionesDato property.</p>
     *
     * <p>
     * For example, to add a new item, do as follows:
     * </p>
     * <pre>
     * getDatoscotizacionesDato().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DatoscotizacionesDato }
     * </p>
     *
     * @return The value of the datoscotizacionesDato property.
     */
    public List<DatoscotizacionesDato> getDatoscotizacionesDato() {
        if (datoscotizacionesDato == null) {
            datoscotizacionesDato = new ArrayList<>();
        }
        return this.datoscotizacionesDato;
    }
    
}
