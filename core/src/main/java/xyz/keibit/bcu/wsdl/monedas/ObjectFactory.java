package xyz.keibit.bcu.wsdl.monedas;

import jakarta.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the xyz.keibit.bcu.wsdl.monedas package. 
 * <p>An ObjectFactory allows you to programmatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: xyz.keibit.bcu.wsdl.monedas
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link WsbcumonedasExecute }
     * 
     * @return
     *     the new instance of {@link WsbcumonedasExecute }
     */
    public WsbcumonedasExecute createWsbcumonedasExecute() {
        return new WsbcumonedasExecute();
    }

    /**
     * Create an instance of {@link Wsmonedasin }
     * 
     * @return
     *     the new instance of {@link Wsmonedasin }
     */
    public Wsmonedasin createWsmonedasin() {
        return new Wsmonedasin();
    }

    /**
     * Create an instance of {@link WsbcumonedasExecuteResponse }
     * 
     * @return
     *     the new instance of {@link WsbcumonedasExecuteResponse }
     */
    public WsbcumonedasExecuteResponse createWsbcumonedasExecuteResponse() {
        return new WsbcumonedasExecuteResponse();
    }

    /**
     * Create an instance of {@link Wsmonedasout }
     * 
     * @return
     *     the new instance of {@link Wsmonedasout }
     */
    public Wsmonedasout createWsmonedasout() {
        return new Wsmonedasout();
    }

    /**
     * Create an instance of {@link WsmonedasoutLinea }
     * 
     * @return
     *     the new instance of {@link WsmonedasoutLinea }
     */
    public WsmonedasoutLinea createWsmonedasoutLinea() {
        return new WsmonedasoutLinea();
    }

}
