/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ideaversion3;
	import java.io.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author BD Y TOPBD
 */
public class IdeaVersion3 {

    /**
     * @param args the command line arguments
     */
 
    public static void main(String[] args) {
        // TODO code application logic here
    IdeaVersion3 nuevo=new IdeaVersion3();
    nuevo.contenido();
    }
public void contenido(){

  Scanner read = new Scanner(System.in);
         System.out.println("ingrese el mensaje : ");
         String mensaje = read.nextLine();
        int dataSize1 = ((int) (mensaje.length() + 7 ) / 8) * 8;
        byte[] datas = new byte[(int) dataSize1];
        
         System.out.println("ingrese un key : ");
         String keyss = read.nextLine();
        byte [] byteKey = keyss.getBytes();
	byteKey = Arrays.copyOf(byteKey, 16);

////////////////


	final byte[] key = { (byte) 0x00, (byte) 0x00, (byte) 0x27, (byte) 0xED, 
							 (byte) 0x8F, (byte) 0x5C, (byte) 0x3E, (byte) 0x8B, 
							 (byte) 0xAF, (byte) 0x16, (byte) 0x56, (byte) 0x0D, 
							 (byte) 0x14, (byte) 0xC9, (byte) 0x0B, (byte) 0x43 
							 };
        
       
        
        
		Transmit idea = new Transmit(byteKey);
		printKey("Key: ", byteKey);
		
		// Read the block of text to be encrypted
    	FileInputStream fileInputStream = null;
        File file = new File("Original.txt");
        //////
        
        //Escritura

	try{

	FileWriter w = new FileWriter(file);

	BufferedWriter bw = new BufferedWriter(w);

	PrintWriter wr = new PrintWriter(bw);  

	wr.write(mensaje);//escribimos en el archivo
	        //ahora cerramos los flujos de canales de datos, al cerrarlos el archivo quedará guardado con información escrita

	        //de no hacerlo no se escribirá nada en el archivo

	wr.close();

	bw.close();
	}catch(IOException e){};
        
        long filesize = file.length();
		int dataSize = ((int) (file.length() + 7 ) / 8) * 8;
        byte[] data = new byte[(int) dataSize];
        
        try {
            //convert file into array of bytes
        	fileInputStream = new FileInputStream(file);
        	fileInputStream.read(data);
        	fileInputStream.close();
	       
        	System.out.println("Filesize = " + filesize + "; dataSize = " + dataSize);
        	printData("Original  Data: ", data);
        	printText("Original  Text: ", data);
        } catch(Exception e){
        	e.printStackTrace();
        }
		
		// Encrypt the text
                
    	System.out.println("");
       
		for (int i = 0; i < dataSize/8; i++) {
			idea.encrypt(data, i*8);
			printData("Encrypted Data: ", data);
		}
		printText("Encrypted Text: ", data);
      
                
        try {
        	FileOutputStream fileOuputStream = 
                  new FileOutputStream("Encrypted.txt"); 
        	fileOuputStream.write(data);
        	fileOuputStream.close();
        } catch(Exception e){
            e.printStackTrace();
        }
             // String peque= new String(data);
              String peque="";
        try {
            //peque = new String(data, "UTF-8");
        peque = new String(data);
        
        } catch (Exception ex) {
           ex.printStackTrace();
        }
   
            //System.out.println("texto encriptado es: "+peque);
            
    	       System.out.println("");
		for (int i = 0; i < dataSize/8; i++) {
			idea.decrypt(data, i*8);
			printData("Decrypted Data: ",data);
		}
		printText("Decrypted Text: ", data);
            
		try {
        	FileOutputStream fileOuputStream = 
                    new FileOutputStream("Decrypted.txt"); 
          	fileOuputStream.write(data);
          	fileOuputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}


}

	private static void printKey(String label, byte[] data) {
		System.out.print(label);
    	for (int i = 0; i < data.length; i++) {
    		System.out.printf("%02x", data[i]);
        }
    	System.out.println("\n");
	}

	public void printText(String label, byte[] data) {
		System.out.print(label);
    	for (int i = 0; i < data.length; i++) {
    		System.out.print((char) data[i]);
              
        }
    	System.out.println("");
	}

	private static void printData(String label, byte[] data) {
		System.out.print(label);
    	for (int i = 0; i < data.length; i+=8) {
    		System.out.printf("%02x%02x %02x%02x %02x%02x %02x%02x | ", data[i], data[i+1], data[i+2], data[i+3],
    															data[i+4], data[i+5], data[i+6], data[i+7]);
        }
    	System.out.println("");
	}
    public IdeaVersion3() {
    }
    
}
