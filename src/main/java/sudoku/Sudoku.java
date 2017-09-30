package sudoku;

import java.util.ArrayList;
import java.util.List;

public class Sudoku {
	
	private int rows;
	private int cols;
	private int mat[][];
	
	public Sudoku(int rows, int cols) {
		if(rows < 1 || cols < 1)
			throw new IllegalArgumentException();
		
		this.rows = rows;
		this.cols = cols;
		this.mat = new int[rows*cols][rows*cols];
		
		erase();
	}
	
	void erase() {
		for(int i = 0; i < rows*cols; i++) {
			for(int j = 0; j < rows*cols; j++) {
				mat[i][j] = 0;
			}
		}
	}
	
	void insert(int val, int row, int col) {
		if(val < 1 || val > rows*cols || row < 1 || row > rows*cols || col < 1 || col > rows*cols)
			throw new IllegalArgumentException();
		
		mat[row-1][col-1] = val;
	}
	
	void insert(Point p) {
		if(p != null)
			insert(p.getValue(), p.getX(), p.getY());
	}
	
	void remove(int row, int col) {
		if(row < 1 || row > rows*cols || col < 1 || col > rows*cols)
			throw new IllegalArgumentException();
		
		mat[row-1][col-1] = 0;
	}
	
	int get(int row, int col) {
		if(row < 1 || col < 1 || row > rows || col > cols)
			throw new IllegalArgumentException();
		return mat[row-1][col-1];
	}
	
	Integer[] possibleValues(int row, int col) {
		if(mat[row-1][col-1] != 0)
			return new Integer[]{};
		
		List<Integer> values = new ArrayList<>();
		
		for(int i = 1; i <= rows*cols; i++)
			values.add(i);
		
		for(int j = 0; j < rows*cols; j++) {
			int val = mat[row-1][j];
			if(val != 0) {
				values.remove((Integer) val);
			}
		}
		
		for(int i = 0; i < rows*cols; i++) {
			int val = mat[i][col-1];
			if(val != 0) {
				values.remove((Integer) val);
			}
		}
		
		int x = Math.floorDiv(col-1, cols);
		int y = Math.floorDiv(row-1, rows);
		
		for(int i = y*rows; i < (y+1)*rows; i++) {
			for(int j = x*cols; j < (x+1)*cols; j++) {
				int val = mat[i][j];
				if(val != 0) {
					values.remove((Integer) val);
				}
			}
		}
		
		return values.toArray(new Integer[0]);
	}
	
	Point getFirstEmpty() throws PointNotFoundException {
		for(int i = 0; i < rows*cols; i++)
			for(int j = 0; j < rows*cols; j++)
				if(mat[i][j] == 0)
					return new Point(0, i, j);
		
		throw new PointNotFoundException();
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		
		for(int i = 0; i < rows*cols; i++) {
			if(i % rows == 0) {
				addLine(str);
			}
			
			for(int j = 0; j < rows*cols; j++) {
				if(j % cols == 0) {
					str.append("|");
				}
				
				str.append(" ");
				if(mat[i][j] != 0)
					str.append(mat[i][j]);
				else
					str.append(" ");
				str.append(" ");
			}
			
			str.append("|\n");
		}
		
		addLine(str);
		
		return str.toString();
	}
	
	private void addLine(StringBuilder str) {
		for(int i = 0; i < rows; i++) {
			str.append("—");
			for(int j = 0; j < cols; j++)
				str.append("———");
		}
		str.append("—\n");
	}
	
	public Sudoku copy() {
		Sudoku copy = new Sudoku(rows, cols);
		copy.mat = this.mat.clone();
		return copy;
	}
	
	public static void main(String[] args) {
		Sudoku s = new Sudoku(3, 3);
		s.insert(3, 1, 4);
		s.insert(2, 7, 8);
		s.insert(9, 5, 5);
		System.out.println(s);
		
		for(int i: s.possibleValues(6, 4))
			System.out.print(i + " ");
		System.out.println("\n");
		for(int i: s.possibleValues(3, 8))
			System.out.print(i + " ");
	}
	
}
