package mirosha.game;

/**
 * ����� ��� ���������� ���� � �������� ���� �� �����������
 * @author Mirosha
 * @version 1.0
 */
public class Spot { 
	
	/** ���� ������*/
	public int row; 
	
	/** ���� �������*/
	public int col; 
	
	/** 
     * ����������� - �������� ������ ������� ����� ���������� ������-�������
     * @param row - ������
     * @param col - �������
     */
	public Spot(int row, int col) {
		this.row = row; 
		this.col = col; 
	}
}