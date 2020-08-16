
package me.brook.wonder.entities.location;

public class Coords   {

	private int x, z;

	public Coords(int x, int z) {
		this.x = x;
		this.z = z;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}

	@Override
	public String toString() {
		return "Coords[x=" + x + ", z=" + z + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + z;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		Coords other = (Coords) obj;
		if(x != other.x)
			return false;
		if(z != other.z)
			return false;
		return true;
	}

	public Coords add(Coords coords) {
		return new Coords(this.x + coords.x, this.z + coords.z);
	}
	
	

}
