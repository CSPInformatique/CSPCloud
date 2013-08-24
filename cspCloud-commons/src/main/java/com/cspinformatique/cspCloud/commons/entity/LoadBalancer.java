package com.cspinformatique.cspCloud.commons.entity;

import java.util.ArrayList;
import java.util.List;

public class LoadBalancer {
	public static final String TYPE_CLUSTERS = "CLUSTERS";
	public static final String TYPE_INSTANCES = "INSTANCES";
	
	private Application application;
	private List<Cluster> clusters;
	private int id;
	private List<Instance> instances;
	private String type;
	
	public LoadBalancer(){
		
	}
	
	public LoadBalancer(
		Application application, 
		List<Cluster> clusters,
		int id, 
		List<Instance> instances,
		String type
	){
		this.application = application;
		this.clusters = clusters;
		this.id = id;
		this.instances = instances;
		this.type = type;
	}

	public Application getApplication() {
		return application;
	}

	public void setApplication(Application application) {
		this.application = application;
	}

	public List<Cluster> getClusters() {
		return clusters;
	}

	public void setClusters(List<Cluster> clusters) {
		this.clusters = clusters;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<Instance> getInstances() {
		return instances;
	}

	public void setInstances(List<Instance> instances) {
		this.instances = instances;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public void removeInstance(Instance instanceToRemove){
		for(int i = 0; i < this.instances.size(); i++){
			Instance instance = this.instances.get(i);
			if(	instance.getApplication().getId() == instanceToRemove.getApplication().getId() && 
				instance.getServer().getId() == instanceToRemove.getServer().getId()
			){
				this.instances.remove(i);
				
				return;
			}
		}
	}
	
	public void removeInstances(){
		this.instances = new ArrayList<Instance>();
	}
}
