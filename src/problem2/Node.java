package problem2;

public class Node {
	public Node left;
	public Node right;
	int num;
	boolean visited;
	Node(int val){
		num   = val;
		left  = null;
		right = null;
		visited = false;
	}
}
