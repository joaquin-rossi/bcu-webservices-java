
package bcu.wsdl.cotizaciones;

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
 *         <element name="Salida" type="{Cotiza}wsbcucotizacionesout"/>
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
@XmlRootElement (name = "wsbcucotizaciones.ExecuteResponse")
public class WsbcucotizacionesExecuteResponse {
    
    @XmlElement (name = "Salida", required = true)
    protected Wsbcucotizacionesout salida;
    
    /**
     * Gets the value of the salida property.
     *
     * @return possible object is
     * {@link Wsbcucotizacionesout }
     */
    public Wsbcucotizacionesout getSalida() {
        return salida;
    }
    
    /**
     * Sets the value of the salida property.
     *
     * @param value allowed object is
     *              {@link Wsbcucotizacionesout }
     */
    public void setSalida(Wsbcucotizacionesout value) {
        this.salida = value;
    }
    
}
