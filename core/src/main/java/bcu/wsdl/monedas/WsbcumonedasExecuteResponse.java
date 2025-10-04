
package bcu.wsdl.monedas;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type</p>.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 *
 * <pre>{@code
 * <complexType>
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="Salida" type="{Cotiza}wsmonedasout"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 */
@XmlAccessorType (XmlAccessType.FIELD)
@XmlType (name = "", propOrder = {
        "salida"
})
@XmlRootElement (name = "wsbcumonedas.ExecuteResponse")
public class WsbcumonedasExecuteResponse {
    
    @XmlElement (name = "Salida", required = true)
    protected Wsmonedasout salida;
    
    /**
     * Gets the value of the salida property.
     *
     * @return possible object is
     * {@link Wsmonedasout }
     */
    public Wsmonedasout getSalida() {
        return salida;
    }
    
    /**
     * Sets the value of the salida property.
     *
     * @param value allowed object is
     *              {@link Wsmonedasout }
     */
    public void setSalida(Wsmonedasout value) {
        this.salida = value;
    }
    
}
