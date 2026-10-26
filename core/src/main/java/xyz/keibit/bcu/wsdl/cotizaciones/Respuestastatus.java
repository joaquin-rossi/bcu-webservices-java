package xyz.keibit.bcu.wsdl.cotizaciones;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for respuestastatus complex type</p>.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 * 
 * <pre>{@code
 * <complexType name="respuestastatus">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="status" type="{http://www.w3.org/2001/XMLSchema}byte"/>
 *         <element name="codigoerror" type="{http://www.w3.org/2001/XMLSchema}short"/>
 *         <element name="mensaje" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "respuestastatus", propOrder = {
    "status",
    "codigoerror",
    "mensaje"
})
public class Respuestastatus {

    protected byte status;
    protected short codigoerror;
    @XmlElement(required = true)
    protected String mensaje;

    /**
     * Gets the value of the status property.
     * 
     */
    public byte getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     */
    public void setStatus(byte value) {
        this.status = value;
    }

    /**
     * Gets the value of the codigoerror property.
     * 
     */
    public short getCodigoerror() {
        return codigoerror;
    }

    /**
     * Sets the value of the codigoerror property.
     * 
     */
    public void setCodigoerror(short value) {
        this.codigoerror = value;
    }

    /**
     * Gets the value of the mensaje property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMensaje() {
        return mensaje;
    }

    /**
     * Sets the value of the mensaje property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMensaje(String value) {
        this.mensaje = value;
    }

}
