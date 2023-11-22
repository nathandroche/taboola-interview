package problem2;

public class TreeSerializer_Q2 {
	
	public static String serialize(Node root) {
        StringBuilder sb = new StringBuilder();
        serHelper(root, sb);
        return sb.toString();
    }

    private static void serHelper(Node curNode, StringBuilder sb) {
        if (curNode == null) {
            sb.append("null,");
        } else {
        	if(curNode.visited)
        		throw new RuntimeException("cycle detected");
        	curNode.visited = true;
	        sb.append(curNode.num).append(",");
	        serHelper(curNode.left, sb);
	        serHelper(curNode.right, sb);
        }
    }

    public static Node deserialize(String data) {
        String[] nodes = data.split(",");
        int[] nodeCount = {0};
        return desHelper(nodes, nodeCount);
    }

    private static Node desHelper(String[] nodes, int[] nodeCount) {
        if (nodeCount[0] >= nodes.length || nodes[nodeCount[0]].equals("null")) {
        	nodeCount[0]++;
            return null;
        }

        Node node = new Node(Integer.parseInt(nodes[nodeCount[0]]));
        nodeCount[0]++;
        node.left = desHelper(nodes, nodeCount);
        node.right = desHelper(nodes, nodeCount);
        return node;
    }
}