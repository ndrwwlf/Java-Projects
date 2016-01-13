import java.util.ArrayList;

public class LineList {
	private ArrayList<String> list;

	public LineList() {
		list = new ArrayList<String>();
	}

	public int size() {
		return list.size();
	}

	public String get(int k) {
		return list.get(k);
	}

	public void add(String line) {
		list.add(line);
	}

	public String remove(int k) {
		return list.remove(k);
	}

	public void move(int index, int newIndex) {
		String temp = list.get(index);
		if(index > newIndex) {
			list.remove(index);
			list.add(newIndex, temp);
		}
		else {
			list.add(newIndex, temp);
			list.remove(index);
		}
	}

	public void shuffle() {
		for(int n = list.size(); n >= 2; n--) {
			int index = (int) (Math.random() * n);
			String temp = list.get(index);
			list.remove(index);
			list.add(index, list.get(n));
			list.remove(n);
			list.add(n, temp);
		}
	}
}