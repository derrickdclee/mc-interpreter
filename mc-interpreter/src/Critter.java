
public abstract class Critter {
	private String name;
	private int x;
	private int y;
	private Direction dir;
	private boolean isAlive;
	
	public Critter(String name, int x, int y, DirNode dirNode) {
		this.name = name;
		this.x = x;
		this.y = y;
		this.isAlive = true;
		this.dir = dirNode.dir;
	}
	
	public Direction getDir() {
		return this.dir;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public void setX(int newX) {
		this.x = newX;
	}
	
	public void setY(int newY) {
		this.y = newY;
	}
}

class Cat extends Critter {	
	public Cat(String name, int x, int y, DirNode dirNode) {
		super(name, x, y, dirNode);
	}
	
}

class Mouse extends Critter {
	public Mouse(String name, int x, int y, DirNode dirNode) {
		super(name, x, y, dirNode);
	}
}
