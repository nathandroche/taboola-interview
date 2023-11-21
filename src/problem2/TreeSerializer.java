package problem2;

public interface TreeSerializer {
	String serialize(Node root);
	Node deserialize(String serial);
}
