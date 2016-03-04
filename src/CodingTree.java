import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * This creates the Coding Tree and provides supporting functions.
 * 
 * The following source was used to implement some of the following code:
 * http://algs4.cs.princeton.edu/55compression/Huffman.java.html
 * 
 * @author Brandon Semba
 * @version 2/18/15
 *
 */
public class CodingTree 
{
	/** The message being encoded. */
	private final String myMessage;
	
	/** The root of the CodingTree. */
	private final Node myRoot;
	
	/** A map containing a directory of Huffman codes. */
	public Map<Character, String> myCodeDir;
	
	/** The encoded message. */
	public final String myBits;
	
	/**
	 * Constructs a CodingTree.
	 * 
	 * @param theMessage the message to be encoded
	 */
	public CodingTree(final String theMessage) 
	{
		myMessage = theMessage;
		myRoot = buildTree(myMessage);
		String code = "";
		myCodeDir = new HashMap<Character, String>();
		myCodeDir = buildDirectory(myCodeDir, myRoot, code);
		myBits = encodeMessage(myMessage);
	}
	
	/**
	 * Encodes the message using the Huffman codes.<br>
	 * Returns a String containing the encoded message.
	 * 
	 * @param theMessage - the message to encode
	 * @return the encoded message.
	 */
	private String encodeMessage(final String theMessage)
	{
		final StringBuilder encoded = new StringBuilder();
		
		for(int i = 0; i < theMessage.length(); i++)
		{
			encoded.append(myCodeDir.get(theMessage.charAt(i)));
		}
		return encoded.toString();
	}
	
	/**
	 * Builds the  CodingTree by creating nodes for the 
	 * characters with their frequency included<br>
	 * Then, using the weighted nodes to produce sub trees, placing the new
	 * subtrees with adjust frequencies back into the priorityQ,<br>
	 * and then removing from the priorityQ to generate the full tree.
	 * 
	 * @param theMessage - the message being encoded
	 * @return the CodingTree.
	 */
	private Node buildTree(final String theMessage)
	{
		final Map<Character, Integer> freqMap = new HashMap<Character, Integer>();
		final PriorityQueue<Node> priQ = new PriorityQueue<Node>();
		
		Character bookChar;
		for(int i = 0; i < theMessage.length() - 1; i++)
		{
			bookChar = theMessage.charAt(i);
			if(freqMap.containsKey(bookChar))
			{
				Integer freqUpdate = freqMap.get(bookChar);
				freqUpdate = freqUpdate + 1;
				freqMap.put(bookChar, freqUpdate);
			}
			else
			{
				Integer freq = 0;
				freqMap.put(bookChar, ++freq);
			}
		}
		
        for (Map.Entry<Character, Integer> entry : freqMap.entrySet())
        {
            Character key = entry.getKey();
            Integer value = entry.getValue();
            
            Node node = new Node(key, value, null, null);
            priQ.add(node);
        }

		while(priQ.size() > 1)
		{
			Node left = priQ.remove();
			Node right = priQ.remove();
			Node parent = new Node('\0', left.freq + right.freq, left, right);
			priQ.add(parent);
		}
		return priQ.remove();
	}
    
	/**
	 * Method recurses through the tree and collects all the code strings<br> 
	 * and then puts them into a Map paired with the Character being the key.
	 * 
	 * @param codeMap - The map that contains a directory of codes
	 * @param theRoot - the root of the tree
	 * @param theCode - the Huffman code for a specific Character
	 * @return A Map containing all the Huffman codes.
	 */
    private Map<Character, String> buildDirectory(final Map<Character, String> codeMap,
    											  final Node theRoot, final String theCode) 
    {
        if (theRoot.isLeaf()) 
        {
            codeMap.put(theRoot.chr, theCode.toString());
        }
        else 
        {
            buildDirectory(codeMap, theRoot.left,  theCode + "0");
            buildDirectory(codeMap, theRoot.right, theCode + "1");
        }
        return codeMap;
    }
	
	/**
	 * Private inner class that creates Nodes that are you used by the 
	 * outer class to create the CodingTree.
	 */
	private static class Node implements Comparable<Node>
	{	
		/** Stores the frequency of the Character's occurrence. */
		final private Integer freq;
		
		/** Stores the Character from the input text file. */
		final private Character chr;
		
		/** The left child of the parent node. */
		private Node left;
		
		/** The right child of the parent node. */
		private Node right;
		
		/**
		 * Creates a weighted leaf for the coding Tree.
		 * 	
		 * @param theFreq - the weight of the Node
		 * @param theChar - the Character of that Node
		 */
		public Node(final Character theChar, final Integer theFreq,
						final Node theLeft, final Node theRight)
		{
			freq = theFreq;
			chr = theChar;
			left = theLeft;
			right = theRight;
		}
		
		/**
		 * Checks to see whether a Node is a leaf or not.
		 * 
		 * @return true if the node is a leaf; false otherwise.
		 */
        private boolean isLeaf() 
        {
            assert (left == null && right == null) || (left != null && right != null);
            return (left == null && right == null);
        }

		/**
		 * Compares Nodes to one another based on their
		 * character's weight.
		 * 
		 * @param theNode - the Node to compare weights with
		 */
		@Override
		public int compareTo(final Node theNode) 
		{
			if(this.freq < theNode.freq)
				return -1;
			else if(this.freq > theNode.freq)
				return 1;
			else
				return 0;
		}	
	}
}
