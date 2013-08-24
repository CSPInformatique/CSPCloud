package com.cspinformatique.cspCloud.commons.factory;

import java.util.ArrayList;

import com.cspinformatique.cspCloud.commons.entity.Application;
import com.cspinformatique.cspCloud.commons.entity.Cluster;
import com.cspinformatique.cspCloud.commons.entity.Instance;
import com.cspinformatique.cspCloud.commons.entity.LoadBalancer;

public abstract class LoadBalancerFactory {
	public static LoadBalancer getNewLoadBalancer(Application application, String type){
		return new LoadBalancer(application, new ArrayList<Cluster>(), 0, new ArrayList<Instance>(), type);
	}
}
