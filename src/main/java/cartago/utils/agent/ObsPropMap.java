package cartago.utils.agent;

import cartago.ArtifactId;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ObsPropMap {

	private HashMap<String,ArtifactObsProperty> props;

	public ObsPropMap(){
		props = new HashMap<String,ArtifactObsProperty>();
	}
	
	public synchronized void addProperties(ArtifactId id, List<cartago.ArtifactObsProperty> list){
		for (cartago.ArtifactObsProperty p: list){
			add(new ArtifactObsProperty(id,p.getFullId(),p.getName(),p.getValues()));
		}
	}

	public synchronized void updateProperty(ArtifactId aid, cartago.ArtifactObsProperty obs){
		// long id = obs.getId();
		ArtifactObsProperty p = props.get(obs.getFullId());
		if (p != null) {
			p.updateValues(obs.getValues());
		}
	}

	public synchronized void remove(cartago.ArtifactObsProperty obs){
		props.remove(obs.getFullId());
	}

	public synchronized void removeProperties(ArtifactId id){
		Iterator<Map.Entry<String, ArtifactObsProperty>> it =  props.entrySet().iterator();
		while (it.hasNext()){
			ArtifactObsProperty p =  (ArtifactObsProperty) it.next();
			if (p.getArtifactId().equals(id)){
					it.remove();
			}
		}
	}
	
	public synchronized void add(ArtifactId aid, cartago.ArtifactObsProperty prop){
		add(new ArtifactObsProperty(aid,prop.getFullId(),prop.getName(),prop.getValues()));
	}

	public synchronized void add(ArtifactObsProperty prop){
		props.put(prop.getFullId(), prop);
	}

	public synchronized ArtifactObsProperty getByName(String name){
		for (var p: props.values()){
			if (p.getName().equals(name)){
				return p;
			}
		}
		return null;
	}

	
	/*
	public synchronized ArtifactObsProperty removeByName(String name){
		ArrayList<ArtifactObsProperty> list = props.get(name);
		if (list != null){
			ArtifactObsProperty prop = list.remove(0);
			idListMap.remove(prop.getId());
			return prop;
		} else {
			return null;
		}
	}*/

	public synchronized ArtifactObsProperty get(String name, Object... values){
		for (var p: props.values()){
			if (p.match(name, values)){
				return p;
			}
		}
		return null;
	}
	
	public synchronized ArtifactObsProperty getPropValue(String name, Object... values){
		return get(name,values);
	}


	public synchronized ArtifactObsProperty getPropValueByName(String name){
		return getByName(name);
	}

}
