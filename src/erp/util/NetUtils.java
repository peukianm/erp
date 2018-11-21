/* Copyright 2004 for General Secretariat of the Council of the European Union (GSC). */
/* This code belongs to the GSC.                                                      */
package erp.util ; 

import java.net.* ; 
import java.io.* ; 
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** 
 * This class contains basic methods for NET tasks. Apart from reading 
 * simple HTTP stream (i.e. HTML source) or binary data (MIME data through HTTP)
 * it encompasses methods for connecting to a secured URL through BASIC authentication.
 * Have in mind that no additional attributes present for connecting through a proxy server. 
 * Should this is necessary, use the standard java options for proxy setting. 
 * 
 * @author FADO Development team.
 */

public class NetUtils 
{
    /** The Logger instance of the class */
    private final static Logger logger = LogManager.getLogger(NetUtils.class) ;     
    
    /**
     * Default constructor, every utility class (i.e. class with many static methods should
     * not expose a public constructor. 
     */
    private NetUtils()
    {
        super() ; 
    }
    
    /**
     * Returns the hostname of the machine that the current JVM is running
     * 
     * @return the name of the host machine
     */
    public static String getHostname()
    { 
        String result = new String() ; 
        try
        {
            InetAddress localaddr = InetAddress.getLocalHost();                    
            result = localaddr.getHostName() ;
        }
        catch ( UnknownHostException e )
        {
            result = "" ; 
            logger.warn("Cannot retrieve the hostname due to : " + e.toString() ) ;
            logger.warn(e, e) ;             
        }
        
        return result ; 
        
    }
}

