package test.problem2;

import problem2.Node;
import problem2.TreeSerializer_Q1;
import problem2.TreeSerializer_Q2;
import org.junit.Test;
import static org.junit.Assert.*;

public class TreeSerializerTest {

    @Test
    public void simpleTest_Q1(){
        Node head = new Node(3);
        head.left = new Node(7);
        head.right = new Node(6);
        head.left.right = new Node(9);
        head.left.left = new Node(12);
        head.right.right = new Node(24);
        head.right.left = new Node(65);
        String serial = TreeSerializer_Q1.serialize(head);
        Node postHead = TreeSerializer_Q1.deserialize(serial);
        assertEquals(postHead.num, head.num);
        assertEquals(postHead.left.num, head.left.num);
        assertEquals(postHead.left.left.num, head.left.left.num);
        assertEquals(postHead.left.right.num, head.left.right.num);
        assertEquals(postHead.right.num, head.right.num);
        assertEquals(postHead.right.left.num, head.right.left.num);
        assertEquals(postHead.right.right.num, head.right.right.num);
    }
    @Test
    public void simpleTest_Q2(){
    	Node head = new Node(3);
    	head.left = new Node(7);
    	head.right = new Node(6);
    	head.left.right = new Node(9);
    	head.left.left = new Node(12);
    	head.right.right = new Node(24);
    	head.right.left = new Node(65);
    	String serial = TreeSerializer_Q2.serialize(head);
    	Node postHead = TreeSerializer_Q2.deserialize(serial);
    	assertEquals(postHead.num, head.num);
    	assertEquals(postHead.left.num, head.left.num);
    	assertEquals(postHead.left.left.num, head.left.left.num);
    	assertEquals(postHead.left.right.num, head.left.right.num);
    	assertEquals(postHead.right.num, head.right.num);
    	assertEquals(postHead.right.left.num, head.right.left.num);
    	assertEquals(postHead.right.right.num, head.right.right.num);
    }
    @Test
    public void cycleTest_Q2(){
    	Node head = new Node(3);
    	head.left = new Node(7);
    	head.right = head;
    	try {
    		TreeSerializer_Q2.serialize(head);
    		fail();
    	} catch (Exception e) {
    	}
    }

}
