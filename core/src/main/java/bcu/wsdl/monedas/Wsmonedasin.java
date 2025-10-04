
package bcu.wsdl.monedas;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for wsmonedasin complex type</p>.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 *
 * <pre>{@code
 * <complexType name="wsmonedasin">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="Grupo" type="{http://www.w3.org/2001/XMLSchema}byte"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 */
@XmlAccessorType (XmlAccessType.FIELD)
@XmlType (name = "wsmonedasin", propOrder = {
        "grupo"
})
public class Wsmonedasin {
    
    @XmlElement (name = "Grupo")
    protected byte grupo;
    
    /**
     * Gets the value of the grupo property.
     */
    public byte getGrupo() {
        return grupo;
    }
    
    /**
     * Sets the value of the grupo property.
     */
    public void setGrupo(byte value) {
        this.grupo = value;
    }
    
}
