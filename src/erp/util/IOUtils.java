/* Copyright 2004 for General Secretariat of the Council of the European Union (GSC). */
/* This code belongs to the GSC.                                                      */
package erp.util ;

import java.io.* ;
import java.util.zip.* ;
import java.util.* ; 


/**
 * This utility class contains basic method for IO and zip/unzip operations.
 * Most of the methods of this class are static in order to be accesible without the
 * creation of an instance of the class. This is due to the heavy use of methods.     
 *   
 * @author FADO Development team.
*/
public class IOUtils
{		
       

    /** The buffer size for IO reading appr. 100Kbytes */
    private static final int BUFFERSIZE = 100000 ; // appr. 100Kbytes
    
    /** The UTF8 encoding for java */    
    public static final String UTF8 = "UTF-8" ; 

    /**
     * Private constructor, used for every utility class. 
     */
    private IOUtils() {
        super() ; 
    }
    
   /**  
    * Saves an InputStream in a binary file.
    * 
    * @param filename the full path name of the file to be created.
    * @param input the input stream.
    */
    public static void saveBinaryFile(String filename, InputStream input) 
    {        
        try 
        {
            IOUtils.writeFile("",filename);        
            File file = new File(filename);
            FileOutputStream fos = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            BufferedInputStream bis = new BufferedInputStream(input) ; 
            
            int aByte;
            while ((aByte=bis.read()) != -1) 
            {
                bos.write(aByte);
            }
            
            bos.flush();
            bos.close();
            bis.close();
            fos.flush();
            fos.close();
        }
        catch (IOException e) {
//            logger.error("saveBinaryFile() : " + e.toString() ) ;
//            logger.error(e, e) ;
        }
    }
    
   /**  
    * Reads a binary file in a byte array.
    * 
    * @param filename the full path name of the file to be read.
    * @return a byte array containing the file contents
    */
    public static byte[] readBinaryFile(String filename) 
    {        
        try
        {
            InputStream is = new FileInputStream(filename);          
            ByteArrayOutputStream  baos = new ByteArrayOutputStream();
            DataInputStream in = new DataInputStream(is);
            int byte1 = -1;
            while ((byte1 = in.read() /* throws IOException */
            ) != -1) {
                baos.write(byte1);
            }
            if(baos!=null)
            {
                return baos.toByteArray();
            }
        }
        catch (IOException e) 
        { 
            //logger.warn("Exception while reading " + filename + " due to " + e.toString() ) ;              
        }
        return null;
    }
    /** 
     * Reads the content of a text file. Returns a String with the contents of the file.
     * @param filename The full path name.
     * @return A String with all file's content.
     */              
    public static String readFile(String filename)
    {
        String full = null ; 
        try
        {
            BufferedReader input = new BufferedReader(new FileReader(filename)) ;
      		StringBuffer sb = new StringBuffer() ;		
            char[] buffer = new char[32000] ; 
            int i = -1 ;
            while ( (i = input.read(buffer)) != -1 ) 
            {                
      			sb.append(buffer, 0, i) ;
			}
    			
            full = new String(sb) ;
            
            input.close() ;
            
        }
    	catch (IOException e) 
        { 
            full = null ; 
            //logger.warn("Exception while reading " + filename + " due to " + e.toString() ) ;              
        }
        
        return full ;
        
    }

  
    /** 
     * Reads the content of a text file with a specific encoding. Returns a 
     * String with the contents of the file
     * 
     * @param filename The full path name.
     * @param encoding the name of the encoding of the file. 
     * @return A String with all file's content.
     */                  
     public static String readFile(String filename, String encoding) 
     {
        String full = null ; 
        try 
        {
            InputStreamReader input = new InputStreamReader(new FileInputStream(filename), encoding);
            StringBuffer sb = new StringBuffer();
            char[] buffer = new char[32000] ; 
            int i = -1 ;
            while ((i = input.read(buffer)) != -1 ) 
            {                
      			sb.append(buffer, 0, i) ; 					                	
			}
            full = sb.toString() ;            
            input.close();            
        }
        catch (IOException e) 
        {
            full = null ; 
            //logger.warn(filename + " " + e.toString()) ;             
        }
        
        return full ; 
    }

    /** 
     * Writes a String to a given file
     * 
     * @param fileContents the String to be written
     * @param fileName the full path of the filename to write the contents
     */
    public static void writeFile(String fileContents, String fileName)
    {
        String thePath = (new File(fileName)).getParent() ;
        if ( ( thePath != null ) && ( ! thePath.isEmpty() ) )
            (new File(thePath)).mkdirs() ;
        
        FileOutputStream out; // declare a file output object
        PrintStream p; // declare a print stream object
        try
        {
            out = new FileOutputStream(fileName);
            p = new PrintStream( out );
            p.print(fileContents);
            p.flush();
            p.close();
        }
        catch(Exception e)
        {
//            logger.error("Cannot writeFile " + fileName + " " + e.toString() ) ; 
//            logger.error(e, e) ; 
        }
    }

    /** 
     * Writes a String to a given file in a given encoding.
     * 
     * @param fileContents the String to be written
     * @param fileName the full path of the filename to write the contents
     * @param encoding the name of the encoding. 
     */
     
    public static void writeFile(String fileContents, String fileName, String encoding)
    {
        String thePath = (new File(fileName)).getParent() ;
        if ( ( thePath != null ) && ( ! thePath.isEmpty() ) )
            (new File(thePath)).mkdirs() ;
        
        try 
        {
            FileOutputStream fos = new FileOutputStream(fileName) ; 
            OutputStreamWriter osw = new OutputStreamWriter(fos, encoding) ;         
            osw.write(fileContents) ; 
            osw.flush() ; 
            osw.close() ; 
        }
        catch ( Exception e ) 
        {            
//            logger.error("Cannot writeFile " + fileName + " in encoding " + encoding) ; 
//            logger.error(e, e) ; 
        }
    }
    
    /** 
     * Creates a zip file uzing standard zip.
    * It is usefull before inserting a BLOB entry into the database.
    * 
    * @param Vector v a Vector with full path names of the files to be inserted
    * @param String discardPath the path under which the 
    * @param String targetFile the full path name of the target file
    * 
    * @return true/false in case of success/failure respectively
    */
    public static boolean createZipFile(Vector v, String discardPath, 
        String targetFile) 
    {
        boolean result = true ; 
        try 
        {
        	if ( ( ! discardPath.isEmpty() ) && (    ( discardPath.lastIndexOf("/") != ( discardPath.length() - 1) ) 
                                                   && ( discardPath.lastIndexOf("\\") != ( discardPath.length() - 1) )  ) )
            {
    			discardPath += "/" ; 
            }
            
            FileOutputStream fos = new FileOutputStream(targetFile) ;
            ZipOutputStream zos = new ZipOutputStream(fos) ;
            for ( int i = 0 ; i < v.size() ; i++ )             
            {                
                appendZipEntry(zos, (String)v.elementAt(i), discardPath) ;
            }
		
        	zos.flush() ;
            zos.close() ;
    		fos.close() ;
        } 
        catch ( IOException e ) 
        {         
            //logger.error(e, e) ;  
            result = false ; 
        } 
        
        return result ;
    }

    /** Appends a file to a ZipOutputStream object. 
        @param zos the ZipOutputStream object.
        @param filename the full path name of the file to be appended.
        @param discardPath the path to be discarded from the file's info in the zip file.
    */
	private static void appendZipEntry(ZipOutputStream zos, String fileName, String discardPath) throws IOException
    {
        FileInputStream fis = new FileInputStream(fileName) ;
		BufferedInputStream bis = new BufferedInputStream(fis) ;        
        
        // Create and append the ZipEntry
        if ( ! discardPath.isEmpty() ) 
            fileName = fileName.substring(discardPath.length()) ; 
                        
        ZipEntry fileEntry = new ZipEntry(fileName) ; 
		
        zos.putNextEntry(fileEntry) ;        
        
        // Create a byte array object named data and declare byte count variable.
		byte[] data = new byte[BUFFERSIZE] ;
		int byteCount ;
		while ((byteCount = bis.read(data, 0, BUFFERSIZE)) > -1) 
			zos.write(data, 0, byteCount) ;
	}

    /** Unzip a zip file and extracts the content in a directory.
        @param zipFileName is the name of the file to be exctracted from the zip file.
        @param targetDir is the directory where the file will be exctracted. 
        @throws IOException in case of error during the saving of the file.
    */
    public static void unzipFile(String zipFileName, String targetDir ) throws IOException
    {
       	if ( ( ! targetDir.isEmpty() ) && ( targetDir.lastIndexOf("/") != ( targetDir.length() - 1) ) )
   			targetDir += "/" ; 

        if ( ! targetDir.isEmpty() ) 
            new File(targetDir).mkdirs() ;
      
       // Added to check if ZIP file name has any non latin characters. -- start
      
       try{     
            //ZipFile zFile = new ZipFile(zipFileName);
            if(!isLatinName(zipFileName))
                    throw new ZipException();
       }catch (ZipException e)
       {
          throw new IOException("Invalid Characters in the ZIP File Name"); 
       }
       
       // Added to check if ZIP file name has any non latin characters. -- end
 
       InputStream in = new BufferedInputStream(new FileInputStream(zipFileName)) ;
	   ZipInputStream zin = new ZipInputStream(in) ;    
      
      try
      {
        ZipEntry e = zin.getNextEntry() ;
        while ( e != null )
        {        
            new File(new File(targetDir + e.getName()).getParent()).mkdirs() ; 

            if ( ! e.isDirectory() )
            {   
            	unzipEntry(zin, targetDir + e.getName());
            }

            e = zin.getNextEntry() ; 
        }

      }catch (IllegalArgumentException e)
      {
          zin.close();
          throw new IOException("Invalid Characters in the File Names");
      }
		zin.close();
    }

    /** Unzip an entry of a zip file.
        @param zin the ZipInputStream.
        @param s is the name of the zip entry.
        @throws IOException from the exctract operation.
    */
	private static void unzipEntry(ZipInputStream zin, String s) throws IOException 
	{
		FileOutputStream out = new FileOutputStream(s) ;
		byte [] b = new byte[BUFFERSIZE] ;
		int len = -1 ;
		while ( (len=zin.read(b))!= -1 ) 
			out.write(b,0,len) ;
        out.flush() ; 
		out.close() ;
	}

    
    /** Deletes a file.
        @param fileName the name of the directory. 
    */
	public static void deleteFile(String fileName) 
	{		
		File f = new File(fileName) ;
		if (f.exists() && f.isFile()) 
        {
            f.delete();
        }
	}
    
    /** Deletes the contents of a directory (not recursively), that means that this method
        deletes only the files inside this directory.
        @param directoryName the name of the directory. 
    */
	public static void deleteDirectory(String directoryName) 
	{		
		if ( directoryName.lastIndexOf("/") != directoryName.length() - 1 ) 
			directoryName += "/" ; 	
		File f = new File(directoryName) ;
		String[] list = f.list();		        
		if ( list != null ) 
		{
			for(int i = 0; i < list.length; i++)
			{
				File f1 = new File(directoryName + list[i]); 
				f1.delete() ; 	
			}  			
		}
		f.delete() ;
	}

    /** Traverse a directory and all subdirectories and return a vector with filenames 
        @param initDir the directory to be traversed.
        @return a Vector with all files under this directory (recursively). The result Vector
                contains the fill path names of the files. 
    */
    
	public static Vector traverseDirectory(String initDir)
	{
        Vector v = new Vector(20, 10) ; 
   		if ( initDir.lastIndexOf("/") != ( initDir.length() - 1) )
			initDir += "/" ; 

		File f = new File(initDir) ; 		
        // Check if directory  exists or else return the vector as is
        if (!f.exists())
            return v;

		traverse(f, v, initDir) ;
		return v ;
	}	
    
    /**
     * Recursive methos for traversing a directory structure and obtain the files. 
     * This method used from the traverseDirectory method.
     * @param f the java.io.File to traverse
     * @param v the Vector where files of folders are stored
     * @param initDir the counterpart initial directory for each recursion
     * 
     * @see #traverseDirectory
     */
     
	private static void traverse(File f, Vector v, String initDir)
	{				
		if ( f == null ) 					
			return ; // Interruption of the recursive process
	
		if ( ! f.isDirectory() ) 
		{
    		String source = f.getAbsolutePath() ;
			v.addElement(source) ; 
			return ; 	
		}
        
		String[] list = f.list() ; 
		if ( list == null )
			return ; // Interruption of the recursive process
	
		if ( initDir.lastIndexOf("/") != ( initDir.length() - 1) )
			initDir += "/" ; 
		for ( int i = 0 ; i < list.length ; i++ ) 
		{
			String newInitDir = new String() ;
			if (new File(initDir + list[i]).isDirectory() ) 
				newInitDir = initDir + list[i] + "/" ; 
			traverse(new File(initDir + list[i]), v, newInitDir) ; 
		}
	}
    
    
    
    public static byte[] getBytesFromFile (File file) throws FileNotFoundException, IOException
    {
        
        InputStream is = new FileInputStream(file);
    
        // Get the size of the file
        long length = file.length();
            
        // Before converting to an int type, check
        // to ensure that file is not larger than Integer.MAX_VALUE.
        if (length > Integer.MAX_VALUE) 
        {
            // File is too large
            throw new IOException("File too large");
        }
    
        // Create the byte array to hold the data
        byte[] bytes = new byte[(int)length];
    
        // Read in the bytes
        int offset = 0;
        int numRead = is.read(bytes, offset, bytes.length-offset);
        while (offset < bytes.length && numRead >= 0) 
        {
            offset += numRead;
            numRead = is.read(bytes, offset, bytes.length-offset);
        }
    
        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file "+file.getName());
        }
    
        // Close the input stream and return bytes
        is.close();
        return bytes;
    }

    /** Deletes an entire tree structure (recursively). The root directory will be deleted too.
        @param dir is the directory to be deleted. 
        @return true if succeeded or false otherwise.
    */
    public static boolean deleteTree(String dir)
    {
        return deleteTree(new File(dir)) ;         
    }
    
    /** Deletes an entire tree structure (recursively). The root directory will be deleted too.
        @param dir is the directory to be deleted. 
        @return true if succeeded or false otherwise.
    */
    
    public static boolean deleteTree(String dir, long hours)
    {
        return deleteTree(new File(dir), hours) ;         
    }
    
    
    /** Overloaded methos of the previous one. Deletes an entire tree structure (recursively). The root directory will be deleted too.
        @param dir java.io.File object of a directory
        @return true if succeeded or false otherwise.
    */    
    
    public static boolean deleteTree(File dir)
    {
        deleteTreeStructure(dir) ; 
        return dir.delete() ;
    }
    
    
    /** Overloaded methos of the previous one. Deletes an entire tree structure (recursively). The root directory will be deleted too.
        @param dir java.io.File object of a directory
        @return true if succeeded or false otherwise.
    */
    
    public static boolean deleteTree(File dir, long hours)
    {
        deleteTreeStructure(dir, hours) ; 
        return dir.delete() ;
    }
    
    
    /** Overloaded methos of the preious one. It deletes an entire directory structure recursively.
        @param dir java.io.File object of a directory
        @param includeDir is true then dir is deleted too.
        @return true if succeeded or false otherwise.
    */    			
    public static boolean deleteTree(File dir, boolean includeDir)
    {
        deleteTreeStructure(dir) ; 
        if ( includeDir ) 
            return dir.delete() ;
        else
            return true ;
    }

    /** Overloaded methos of the preious one. It deletes an entire directory structure recursively.
        @param dir java.io.File object of a directory
        @param includeDir is true then dir is deleted too.
        @param hours delete only files last modified before hours ago.
        @return true if succeeded or false otherwise.
    */    			
    public static boolean deleteTree(File dir, boolean includeDir, long hours)
    {
        deleteTreeStructure(dir, hours) ; 
        if ( includeDir ) 
            return dir.delete() ;
        else
            return true ;
    }

    /** Recursive method for the deletion of a directory structure. Used from the deleteTree methods (all overloads).
        @param dir java.io.File object of a directory.
    */    			
    private static void deleteTreeStructure(File dir)
    {        
    	File [] fileArray = dir.listFiles() ;
        try 
        {
    		if ( fileArray != null )
                for ( int i=0; i < fileArray.length; i++ )
                {
                    if ( fileArray[i].isFile() )
                    	fileArray[i].delete() ;
                    else 
                    {
                        deleteTreeStructure(fileArray[i]) ;
                        fileArray[i].delete() ;
                    }
                }
        } 
        catch ( SecurityException se ) 
        {
            //logger.warn("Could not delete tree " + dir.getAbsolutePath() + " " + se.toString()) ;
        }
    }
    
    
    private static void deleteTreeStructure(File dir, long hours)
    {        
    	File [] fileArray = dir.listFiles() ;
        try 
        {
    		if ( fileArray != null )
                for ( int i=0; i < fileArray.length; i++ )
                {
                    if ( fileArray[i].isFile() )
                    {
                        if ( fileArray[i].lastModified() < ( System.currentTimeMillis() - ( hours * 3600000 ) ) )
                        	fileArray[i].delete() ;
                    }
                    else 
                    {
                        deleteTreeStructure(fileArray[i], hours) ;
                        fileArray[i].delete() ;
                    }
                }
        }
        catch ( SecurityException se ) 
        {
            //logger.warn("Could not delete tree " + dir.getAbsolutePath() + " " + se.toString()) ;
        }
    }

    /**
     * Returns the file extension of the filename. It is based solely on String manipulation
     * (reads the portion of the filename after the last ".")
     * 
     * @param filename the filename
     * @return the extension of the filename. 
     */
    public static String getFileExtension(String filename)
    {
        String result = new String() ; 
        if ( filename != null )
        {
            int i = filename.lastIndexOf(".") ; 
            if ( i != -1 )
            {
                result = filename.substring(i + 1) ; 
            }
        }
        
        return result ;
    }
    
    /**
     * Return true if string contains only latin characters, otherwise false.
     * @param name , string value to be checked
     * @return  boolean value, true or false.
     */
     
    public static final boolean isLatinName(String name)
    {
        boolean flag = true;
        
        if((name == null) || (name.isEmpty()))
          return true;
        
        for(int i=0;i<name.length();i++)
        {
            int charValue = name.charAt(i);
            if((charValue <= 31) || (charValue > 127))
            {
                flag = false;
                break; 
            }
        }
        return (flag);
    }
  
}
