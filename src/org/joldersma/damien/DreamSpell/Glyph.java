package org.joldersma.damien.DreamSpell;

public class Glyph {
	String name,action,essence,power;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getEssence() {
		return essence;
	}
	public void setEssence(String essence) {
		this.essence = essence;
	}
	public String getPower() {
		return power;
	}
	public void setPower(String power) {
		this.power = power;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	int number = 0;
}
