package erp.servlets;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import erp.util.StringToolbox;
import erp.util.SystemParameters;


public class BootstrapServlet extends HttpServlet 
{
    //private static final String CONTENT_TYPE = "text/html; charset=UTF-8"; 
    private static String SYSTEM_PROPERTIES_FILE = new String() ;
    
//    @Inject  
//    ApplicationBean applicationBean;
    
    public void init(ServletConfig config) throws ServletException
    {       
        super.init(config) ;          
           
//        applicationBean.getActions(); 
//        applicationBean.getRoles();
//        applicationBean.getCompanies();       
        SYSTEM_PROPERTIES_FILE = StringToolbox.replaceAll(this.getServletContext().getRealPath(""), "\\", "/") + "/config/system.properties" ;                                
        
        //Initiliazing Propertyfile
        SystemParameters.getInstance(SYSTEM_PROPERTIES_FILE, this.getServletContext().getRealPath(""), true) ;                 
        //Initializing Scheduler
        //ScheduleLocator.getInstance();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}

}
