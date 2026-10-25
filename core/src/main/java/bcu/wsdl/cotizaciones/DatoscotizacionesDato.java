package bcu.wsdl.cotizaciones;

import javax.xml.datatype.XMLGregorianCalendar;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for datoscotizaciones.dato complex type</p>.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 * 
 * <pre>{@code
 * <complexType name="datoscotizaciones.dato">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="Fecha" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         <element name="Moneda" type="{http://www.w3.org/2001/XMLSchema}short"/>
 *         <element name="Nombre" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         <element name="CodigoISO" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         <element name="Emisor" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         <element name="TCC" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         <element name="TCV" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         <element name="ArbAct" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *         <element name="FormaArbitrar" type="{http://www.w3.org/2001/XMLSchema}byte"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "datoscotizaciones.dato", propOrder = {
    "fecha",
    "moneda",
    "nombre",
    "codigoISO",
    "emisor",
    "tcc",
    "tcv",
    "arbAct",
    "formaArbitrar"
})
public class DatoscotizacionesDato {

    @XmlElement(name = "Fecha", required = true, nillable = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar fecha;
    @XmlElement(name = "Moneda")
    protected short moneda;
    @XmlElement(name = "Nombre", required = true)
    protected String nombre;
    @XmlElement(name = "CodigoISO", required = true)
    protected String codigoISO;
    @XmlElement(name = "Emisor", required = true)
    protected String emisor;
    @XmlElement(name = "TCC")
    protected double tcc;
    @XmlElement(name = "TCV")
    protected double tcv;
    @XmlElement(name = "ArbAct")
    protected double arbAct;
    @XmlElement(name = "FormaArbitrar")
    protected byte formaArbitrar;

    /**
     * Gets the value of the fecha property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFecha() {
        return fecha;
    }

    /**
     * Sets the value of the fecha property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFecha(XMLGregorianCalendar value) {
        this.fecha = value;
    }

    /**
     * Gets the value of the moneda property.
     * 
     */
    public short getMoneda() {
        return moneda;
    }

    /**
     * Sets the value of the moneda property.
     * 
     */
    public void setMoneda(short value) {
        this.moneda = value;
    }

    /**
     * Gets the value of the nombre property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Sets the value of the nombre property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombre(String value) {
        this.nombre = value;
    }

    /**
     * Gets the value of the codigoISO property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoISO() {
        return codigoISO;
    }

    /**
     * Sets the value of the codigoISO property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoISO(String value) {
        this.codigoISO = value;
    }

    /**
     * Gets the value of the emisor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmisor() {
        return emisor;
    }

    /**
     * Sets the value of the emisor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmisor(String value) {
        this.emisor = value;
    }

    /**
     * Gets the value of the tcc property.
     * 
     */
    public double getTCC() {
        return tcc;
    }

    /**
     * Sets the value of the tcc property.
     * 
     */
    public void setTCC(double value) {
        this.tcc = value;
    }

    /**
     * Gets the value of the tcv property.
     * 
     */
    public double getTCV() {
        return tcv;
    }

    /**
     * Sets the value of the tcv property.
     * 
     */
    public void setTCV(double value) {
        this.tcv = value;
    }

    /**
     * Gets the value of the arbAct property.
     * 
     */
    public double getArbAct() {
        return arbAct;
    }

    /**
     * Sets the value of the arbAct property.
     * 
     */
    public void setArbAct(double value) {
        this.arbAct = value;
    }

    /**
     * Gets the value of the formaArbitrar property.
     * 
     */
    public byte getFormaArbitrar() {
        return formaArbitrar;
    }

    /**
     * Sets the value of the formaArbitrar property.
     * 
     */
    public void setFormaArbitrar(byte value) {
        this.formaArbitrar = value;
    }

}
