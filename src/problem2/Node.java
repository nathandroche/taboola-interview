package problem2;

public class Node {
	public Node left;
	public Node right;
	public int num;
	public boolean visited;
	public Node(int val){
		num   = val;
		left  = null;
		right = null;
		visited = false;
	}
}
