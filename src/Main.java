import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

/**
 * The driver for the compressed literature assignment.
 * The classes uses the CodingTree class to carry out the
 * Huffman Coding.
 * 
 * @author Brandon Semba
 * @version 2/19/15
 *
 */
public class Main 
{
	/** Contains the string of text from the input file. */
	private static final StringBuilder STR_BLD = new StringBuilder();

	/**
	 * The driver of the program.
	 * 
	 * @param theArgs - this is ignored
	 */
	public static void main(final String[] theArgs) throws IOException
	{
		final CodingTree huffTree;
		final Scanner scan;
		
		File inputData = new File("bible.txt");
		try
		{
			scan = new Scanner(inputData);
			while(scan.hasNextLine())
			{
				STR_BLD.append(scan.nextLine());
				STR_BLD.append('\r');
			}
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}
		
		String book = STR_BLD.toString();
		huffTree = new CodingTree(book);
		createCodeDirFile(huffTree.myCodeDir, "codes.txt");
		
		long start = System.currentTimeMillis();
		DataOutputStream output = compressMessageToFile("compressed.txt", huffTree.myBits);
        long end = System.currentTimeMillis();
		
        System.out.println("The statistics for my Huffman Coding implemenation");
        System.out.println("--------------------------------------------------------");
		System.out.println("The original file size: " + inputData.length() * 8+ " bits");
		System.out.println("The compressed file size: " + output.size() * 8 + " bits");
		System.out.println("The compression ratio: " 
						   + Math.round((float)output.size() / (float)inputData.length() * 100) 
						   + "%");
        System.out.println("The elapsed time for the compression: " + (end - start) / 1000 + " seconds");
        System.out.println("--------------------------------------------------------");
		output.close();
	}
	
	/**
	 * Compresses the message and creates an output file.
	 * 
	 * @param theOutputFileName - the name for the output file being created
	 * @return the DataOutputStream the DataOutputStream being written to.
	 * @throws IOException
	 */
	public static DataOutputStream compressMessageToFile(final String theOutputFileName, final String theBits) throws IOException
	{
	    final DataOutputStream out = new DataOutputStream(new FileOutputStream(theOutputFileName));
    	int counter = 0;
    	STR_BLD.setLength(0);
	    for(int i = 0; i < theBits.length(); i++)
	    {
	    	if(counter < 8)
	    	{
	    		STR_BLD.append(theBits.charAt(i));
	    		counter++;
	    		if(i == theBits.length() - 1)
	    		{
	    			out.write(Integer.parseInt(STR_BLD.toString(), 2));
	    		}
	    	}
	    	else
	    	{
	    		out.write(Integer.parseInt(STR_BLD.toString(), 2));
	    		STR_BLD.setLength(0);
	    		STR_BLD.append(theBits.charAt(i));
	    		counter = 1;
	    		if(i == theBits.length() - 1)
	    		{
	    			out.write(Integer.parseInt(STR_BLD.toString(), 2));
	    		}
	    	}
	    }
	    out.close();
	    return out;
	}
	
	/**
	 * Creates an output file that contains the directory of Huffman codes.
	 * 
	 * @param theCodeDir - The Map containing the characters and their associated codes.
	 * @throws IOException 
	 */
	public static void createCodeDirFile(final Map<Character, String> theCodeDir, final String theOutputFileName) throws IOException
	{
	    final DataOutputStream output = new DataOutputStream(new FileOutputStream(theOutputFileName));
		for (Map.Entry<Character, String> entry : theCodeDir.entrySet())
		{
		    Character key = entry.getKey();
			String value = entry.getValue();
			output.write(key);
			output.writeByte('=');
			output.writeBytes(value);
			output.writeByte('\n');
		}
		output.close();
	}
}
