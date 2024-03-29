package sudoku;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SudokuSolver {
	
	private SudokuTree tree;
	private int steps = 0;
	
	public SudokuSolver(Sudoku s) {
		if(s == null)
			throw new IllegalArgumentException();
		this.tree = new SudokuTree(s);
	}
	
	public Sudoku solve() {
		int count = 0;
		
		while(!tree.frontier.isEmpty()) {
			count++;
			
			sudoku.SudokuSolver.SudokuTree.Node node = tree.nextNode();
			
			Optional<Boolean> result = tree.expand(node);
			
			if(result.isPresent()) {
				if(!result.get()) {
					tree.contract(node);
				}
			} else {
				steps = count;
				return tree.s.copy();
			}
		}
		
		return null;
	}
	
	public int steps() {
		return steps;
	}
	
	private class SudokuTree {
		
		private Sudoku s;
		private List<Node> frontier;
		
		public SudokuTree(Sudoku s) {
			if(s == null)
				throw new IllegalArgumentException();
			
			this.frontier = new ArrayList<>();
			frontier.add(null);
			this.s = s.copy();
		}
		
		Node nextNode() {
			return frontier.get(0);
		}
		
		Optional<Boolean> expand(Node parent) {
			if(frontier.contains(parent)) {
				if(parent != null) {
					s.insert(parent.data);
				}
				
				int pos = frontier.indexOf(parent);
				
				List<Node> children = new ArrayList<>();
				Point p;
				
				try {
					p = s.getFirstEmpty();
				} catch(PointNotFoundException e) {
					return Optional.empty();
				}
				
				List<Integer> possible = s.possibleValues(p.getRow(), p.getCol());
				
				if(possible.isEmpty()) {
					return Optional.of(false);
				}
				
				for(int n: possible) {
					children.add(new Node(new Point(n, p.getRow(), p.getCol()), parent));
				}
				
				if(!children.isEmpty()) {
					frontier.remove(parent);
					frontier.addAll(pos, children);
					return Optional.of(true);
				}
			}
			
			throw new IllegalArgumentException();
		}
		
		void contract(Node child) {
			Node current = child;
			
			while(current != null && current.childrencount == 0) {
				s.remove(current.data.getRow(), current.data.getCol());
				frontier.remove(current);
				current = current.parent;
				
				if(current.parent != null)
					current.childrencount--;
			}
		}
		
		@Override
		public String toString() {
			return s.toString() + "\n" + frontier.toString();
		}
		
		private class Node {
			
			private Point data;
			private Node parent;
			private int childrencount;
			
			private Node(Point data, Node parent) {
				if(data == null)
					throw new IllegalArgumentException();
				
				this.data = data;
				this.parent = parent;
				this.childrencount = 0;
				
				if(parent != null)
					parent.childrencount++;
			}
			
			@Override
			public String toString() {
				return data.toString();
			}
			
		}
	}
	
}
