package com.basicsqledu.www.vo;

public class Animal
{
	private int rownum;
	private String th_code;
	private String de_code;
	
	private String animal_size;
	private String animal_species;
	private String animal_legs;
	private String animal_color; // 수정
	private String animal_habitat;
	
	public Animal()
	{
		// TODO Auto-generated constructor stub
	}

	public int getRownum() {
		return rownum;
	}

	public void setRownum(int rownum) {
		this.rownum = rownum;
	}

	public String getTh_code() {
		return th_code;
	}

	public void setTh_code(String th_code) {
		this.th_code = th_code;
	}

	public String getDe_code() {
		return de_code;
	}

	public void setDe_code(String de_code) {
		this.de_code = de_code;
	}

	public String getAnimal_size() {
		return animal_size;
	}

	public void setAnimal_size(String animal_size) {
		this.animal_size = animal_size;
	}

	public String getAnimal_species() {
		return animal_species;
	}

	public void setAnimal_species(String animal_species) {
		this.animal_species = animal_species;
	}

	public String getAnimal_legs() {
		return animal_legs;
	}

	public void setAnimal_legs(String animal_legs) {
		this.animal_legs = animal_legs;
	}

	public String getAnimal_color() {
		return animal_color;
	}

	public void setAnimal_color(String animal_color) {
		this.animal_color = animal_color;
	}

	public String getAnimal_habitat() {
		return animal_habitat;
	}

	public void setAnimal_habitat(String animal_habitat) {
		this.animal_habitat = animal_habitat;
	}

	@Override
	public String toString() {
		return "Animal [rownum=" + rownum + ", th_code=" + th_code + ", de_code=" + de_code + ", animal_size="
				+ animal_size + ", animal_species=" + animal_species + ", animal_legs=" + animal_legs
				+ ", animal_color=" + animal_color + ", animal_habitat=" + animal_habitat + "]";
	}

	
	
}
