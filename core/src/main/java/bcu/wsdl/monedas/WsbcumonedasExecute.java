
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
 *         <element name="Entrada" type="{Cotiza}wsmonedasin"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 */
@XmlAccessorType (XmlAccessType.FIELD)
@XmlType (name = "", propOrder = {
        "entrada"
})
@XmlRootElement (name = "wsbcumonedas.Execute")
public class WsbcumonedasExecute {
    
    @XmlElement (name = "Entrada", required = true)
    protected Wsmonedasin entrada;
    
    /**
     * Gets the value of the entrada property.
     *
     * @return possible object is
     * {@link Wsmonedasin }
     */
    public Wsmonedasin getEntrada() {
        return entrada;
    }
    
    /**
     * Sets the value of the entrada property.
     *
     * @param value allowed object is
     *              {@link Wsmonedasin }
     */
    public void setEntrada(Wsmonedasin value) {
        this.entrada = value;
    }
    
}
