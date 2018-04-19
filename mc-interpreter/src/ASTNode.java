//
//abstract public class ASTNode {
//	final int myNodeType;
//	
//	ASTNode(int nodeType) {
//		myNodeType = nodeType;
//	}
//}
//
//class RootNode extends ASTNode {
//	IntNode i;
//	IntNode j;
//	ASTNode list;
//
//	RootNode(IntNode i, IntNode j, ASTNode list) {
//		super(0);
//		this.i = i;
//		this.j = j;
//		this.list = list;
//	}
//}
//
//class CatNode extends ASTNode {
//	int myNodeType = 1;
//	VarNode v;
//	IntNode i;
//	IntNode j;
//	DirNode d;
//	
//	
//	CatNode(VarNode v, IntNode i, IntNode j, DirNode d) {
//		super(1);
//		this.v = v;
//		this.i = i;
//		this.j = j;
//		this.d = d;
//	}
//}
//
//class MouseNode extends ASTNode {
//	int myNodeType = 2;
//	VarNode v;
//	IntNode i;
//	IntNode j;
//	DirNode d;
//	
//	MouseNode(VarNode v, IntNode i, IntNode j, DirNode d) {
//		super(2);
//		this.v = v;
//		this.i = i;
//		this.j = j;
//		this.d = d;
//	}
//}
//
//class HoleNode extends ASTNode {
//	IntNode i;
//	IntNode j;
//	
//	HoleNode(IntNode i, IntNode j) {
//		super(3);
//		this.i = i;
//		this.j = j;
//	}
//}
//
//class SequenceNode extends ASTNode {
//	
//}
//
//class MoveNode extends ASTNode {
//	
//}
//
//class ClockwiseNode extends ASTNode {
//	
//}
//
//class RepeatNode extends ASTNode {
//	
//}
//
//class IntNode extends ASTNode {
//	
//}
//
//class VarNode extends ASTNode {
//	
//}
//
//class DirNode extends ASTNode {
//	
//}