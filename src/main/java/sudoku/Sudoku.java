package sudoku;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Sudoku implements Serializable {
	
	private static final long serialVersionUID = 2792775316744026313L;
	
	public final int rows;
	public final int cols;
	private int[][] mat;
	
	public Sudoku(int rows, int cols) {
		if(rows < 1 || cols < 1)
			throw new IllegalArgumentException();
		
		this.rows = rows;
		this.cols = cols;
		this.mat = new int[rows*cols][rows*cols];
		
		clear();
	}
	
	public void clear() {
		for(int i = 0; i < rows*cols; i++) {
			for(int j = 0; j < rows*cols; j++) {
				mat[i][j] = 0;
			}
		}
	}
	
	public void checkConstraints(int row, int col) {
		if(row < 1 || row > rows*cols || col < 1 || col > rows*cols)
			throw new IllegalArgumentException();
	}
	
	public void checkConstraints(int val, int row, int col) {
		checkConstraints(row, col);
		
		if(val < 1 || val > rows*cols)
			throw new IllegalArgumentException();
	}
	
	public void insert(int val, int row, int col) {
		checkConstraints(val, row, col);
		
		if(mat[row-1][col-1] != 0)
			throw new IllegalArgumentException();
		
		if(!possibleValues(row, col).contains(val))
			throw new IllegalArgumentException();
		
		mat[row-1][col-1] = val;
	}
	
	public void insert(Point p) {
		if(p != null)
			insert(p.getValue(), p.getRow(), p.getCol());
	}
	
	public void remove(int row, int col) {
		checkConstraints(row, col);
		
		mat[row-1][col-1] = 0;
	}
	
	public int get(int row, int col) {
		checkConstraints(row, col);
		
		return mat[row-1][col-1];
	}
	
	public List<Integer> possibleValues(int row, int col) {
		if(mat[row-1][col-1] != 0)
			return new ArrayList<>();
		
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
		
		return values;
	}
	
	public Point getFirstEmpty() throws PointNotFoundException {
		for(int i = 0; i < rows*cols; i++)
			for(int j = 0; j < rows*cols; j++)
				if(mat[i][j] == 0)
					return new Point(0, i+1, j+1);
		
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
		str.append("|");
		for(int i = 0; i < rows; i++) {
			if(i != 0)
				str.append("—");
			for(int j = 0; j < cols; j++)
				str.append("———");
		}
		str.append("|\n");
	}
	
	public Sudoku copy() {
		Sudoku copy = new Sudoku(rows, cols);
		copy.mat = this.mat.clone();
		return copy;
	}
	
}
