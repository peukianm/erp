/* Copyright 2004 for General Secretariat of the Council of the European Union (GSC). */
/* This code belongs to the GSC.                                                      */
package erp.util;

import java.text.DecimalFormat;
import java.util.* ; 


/** Provides String utility methods.
 * @author FADO development team
 */

public class StringToolbox 
{
    /** The Logger instance of the class */         

    /** Replaces all occurences of 's1' on 's' with 's2'.
        @param s Passed string.
        @param s1 String to be replaced.
        @param s2 Replacement string.
        @return Returns a String with all occurences of 's1' replaced with 's2'.
     */    
    public static String replaceAll(String s, String s1, String s2) 
    {
        StringBuffer stringbuffer = new StringBuffer();
        int i = s.length();
        int j = s1.length();
        if (j == 0)
        {            
            return new String() ; 
        }
            
        boolean flag = false;
        int k = 0;
        do 
        {
            int l = s.indexOf(s1, k);
            if(l < 0) 
            {
                if(!flag)
                    return s;
                stringbuffer.append(s.substring(k));
                break;
            }
            flag = true;
            stringbuffer.append(s.substring(k, l));
            stringbuffer.append(s2);
            k = l + j;
        } while(k < i);
        if (!flag)
            return s;
        else
            return stringbuffer.toString();
    }

    /** It safely returns the substring of a string with specific length. It also 
        appends three dots "..." at the end of the substring it the length of the
        original string exceeds the substring length. 
        @param theString is the original String.
        @param theLength is the length of the substring to be returned.
        @return the substring with trailing dots if necessary. 
     */    
	public static String getSubstring(String theString, int theLength)
	{
		if  (( theString == null ) || (  theString.isEmpty() ) ) 
			return  new String() ; 
            
		if ( theLength < 3 ) theLength = 3 ; 
        
		if  (theString.length() <= theLength ) 	return theString ; 
		else return theString.substring(0, theLength - 3)  + "..." ; 
	}
    
    /** Creates a seperated String of the elements of a Vector. 
        @param v is the input Vector
        @param separator is the separator to be used.
        @return the separated string from the Vector.
     */    
    public static String listFromVector(java.util.Vector v, String separator)
    {
        StringBuffer result = new StringBuffer() ; 
        if ( ( v == null ) || ( v.isEmpty() )  )
            return result.toString() ;  ; 
    
        for ( int i = 0 ; i < v.size() - 1 ; i++ ) 
            result.append((String)v.elementAt(i) + separator + " ") ; 

        result.append((String)v.elementAt(v.size() - 1)) ;

        return result.toString() ;  
    }

  /** Escapes a string so that it can be included (safely) in HTML form parts. 
      @param s input string
      @return the escaped string.
   */    
   
  public static String escapeHTML(String s)
  {
       if ( ( s == null ) || ( s.isEmpty() ) )
            return "" ; 

        
       String retVal = replaceAll(s, "'", "&#039;");    
       retVal = replaceAll(retVal, "\"", "&quot;");
       retVal = replaceAll(retVal, ">", "&gt;");
       retVal = replaceAll(retVal, "<", "&lt;");

   return (retVal);
  }
    

  /** Escapes a string so that it can be included (safely) in HTML form parts. 
      @param s input string
      @return the escaped string.
   */    
  public static String escapeHTML(String s, boolean truncateSingleQuotes)
  {
       if ( ( s == null ) || ( s.isEmpty() ) )
            return "" ; 

       String retVal = new String() ; 

       if ( truncateSingleQuotes )
           retVal = replaceAll(s, "'", "");    
        else
          retVal = replaceAll(s, "'", "&#039;");    
       
       retVal = replaceAll(retVal, "\"", "&quot;");
       retVal = replaceAll(retVal, ">", "&gt;");
       retVal = replaceAll(retVal, "<", "&lt;");

       return (retVal);
    }

    /**
     * Returns a comma separated String from a String array
     * 
     * @param input the String array
     * 
     * @return the comma separated String, the form of the String is input[0],input[1],......
     */
    public static String getCommaSeparatedStringFromArray(String[] input)
    {
        StringBuffer result = new StringBuffer() ; 
        if ( input != null )
        {
            for ( int i = 0 ; i < input.length ; i++ ) 
            {
               result.append(input[i] + ",") ; 
            }
            
            if ( result.toString().endsWith(",") )
            {
                result = result.deleteCharAt(result.length() -1 ) ; 
            }
        }
        
        return result.toString() ; 
    }
    
    /**
     * Creates an ArrayList from a String which has tokens separated from the separator, note that
     * the method uses the StringTokenizer object so you have to use a single character separator. 
     * 
     * @param charSeparatedString a string which with tokens separated by the separator character
     * @param separator the separator character
     * 
     * @return an ArrayList with the tokens of the input String
     */
    public static ArrayList getArrayListFromCharSeparatedString(String charSeparatedString, String separator)
    {
        ArrayList result = new ArrayList() ; 
        
        StringTokenizer st = new StringTokenizer(charSeparatedString, separator) ;
        
        while ( st.hasMoreTokens() )
        {
            result.add(st.nextToken()) ;
        }
        
        return result ; 
    }
    
    /**
     * Checks whether a String is contained within a String array.
     * @param list The string array
     * @param target The string to be looked up.
     * @return TRUE if it exists, FALSE if it does not exist.
     */
    public static boolean isMemberOfArray(String[] list, String target)
    {
        boolean retVal = false;
        
        for (int i=0; i<list.length; i++)
        {
            if (target.equals(list[i]))
            {
                retVal = true;
                break;
            }
        }
        
        return (retVal);
    }
    
    /**
     * Returns an (string) object as string. If the object is null an empty
     * string is returned.
     * @param s Object to be converted to string.
     * @return A string with the passed object's string representation
     */
    public static String objToStr(Object s)
    {
        String retVal = "";
        
        if (s != null) 
        { 
            retVal = (String)s;
        }
        
        return (retVal);
    }


    /**
     * Converts a string with a specified delimiter to string array.
     * @param in String with specified delimiter
     * @param delimiter Delimiter
     * @return A String array
     */
    public static String[] stringToStringArray(String in, String delimiter)
    {
        String[] retVal = null;
        
        ArrayList tmpAl = new ArrayList();
        StringTokenizer st = new StringTokenizer(in, delimiter);
        while (st.hasMoreElements())
        {
            String nextElement = (String)st.nextElement();
            tmpAl.add(nextElement);
        }
        
        retVal = (String[])tmpAl.toArray(new String[tmpAl.size()]); 
        
        return (retVal);
    }
    
   
    
    /**
     * Overloaded version of the above, (inputs are now integer values)
     * 
     * @param a the input value
     * @param thePad the padding to be used
     * 
     * @return the padded value
     */
	public static String pad(int a, int thePad)
    {
		String help = new String(); 
		for ( int i = 1 ; i <= thePad ; i++ ) 
        {
			help += "0" ; 		
        }
		
        return ( (new DecimalFormat(help)).format(a) ) ;
	}
    
   
    
}
