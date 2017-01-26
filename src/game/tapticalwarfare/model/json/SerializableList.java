package game.tapticalwarfare.model.json;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SerializableList implements Serializable {

	private static final long serialVersionUID = 4155219403992651209L;

	private List<Serializable> list;
	
	public SerializableList(){
	}
	
	public SerializableList(List<Serializable> list) {
		this.list = list;
	}

	public List<Serializable> getList() {
		return list;
	}

	public void setList(List<Serializable> list) {
		this.list = list;
	}
	
	public void add(Serializable s){
		if(this.list == null){
			this.list = new ArrayList<Serializable>();
		}
		this.list.add(s);
	}
}
