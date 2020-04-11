package mirosha.game;

/**
 * Класс для размещения срок и столбцов поля по координатам
 * @author Mirosha
 * @version 1.0
 */
public class Spot { 
	
	/** Поле строка*/
	public int row; 
	
	/** Поле столбец*/
	public int col; 
	
	/** 
     * Конструктор - создание нового объекта точка координаты строка-столбец
     * @param row - строка
     * @param col - столбец
     */
	public Spot(int row, int col) {
		this.row = row; 
		this.col = col; 
	}
}