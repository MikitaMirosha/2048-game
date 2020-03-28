package mirosha.game;

public class Spot { // класс для размещения срок и столбцов по координатам
	
	public int row; // строка
	public int col; // столбец 
	
	public Spot(int row, int col) {
		this.row = row; // явный указатель на строку
		this.col = col; // явный указатель на столбец
	}
}