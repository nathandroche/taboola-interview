package problem2;

public class TreeSerializer_Q1 implements TreeSerializer {
	
	public String serialize(Node root) {
        StringBuilder sb = new StringBuilder();
        serHelper(root, sb);
        return sb.toString();
    }

    private void serHelper(Node child, StringBuilder sb) {
        if (child == null) {
            sb.append("null|");
        } else {
	        sb.append(child.num).append("|");
	        serHelper(child.left, sb);
	        serHelper(child.right, sb);
        }
    }

    public Node deserialize(String data) {
        String[] nodes = data.split("|");
        int[] nodeCount = {0};
        return desHelper(nodes, nodeCount);
    }

    private Node desHelper(String[] nodes, int[] nodeCount) {
        if (nodeCount[0] >= nodes.length || nodes[nodeCount[0]].equals("null")) {
        	nodeCount[0]++;
            return null;
        } else {
	        Node node = new Node(Integer.parseInt(nodes[nodeCount[0]]));
	        nodeCount[0]++;
	        node.left = desHelper(nodes, nodeCount);
	        node.right = desHelper(nodes, nodeCount);
	        return node;
        }
    }
}