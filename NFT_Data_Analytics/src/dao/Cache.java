package dao;

import java.util.HashMap;
import java.util.Map;

public class Cache {
    private Map<String, DAOService> services;

    public Cache(){
        services = new HashMap<String, DAOService>();
    }

    public DAOService getService(String serviceName){
    	DAOService service = services.get(serviceName);
        if(service != null){
            System.out.println("Returning cached  "+ serviceName+" object");
            return service;
        }
        return null;
    }

    public void addService(DAOService newService){
        boolean exists = services.containsKey(newService.getName());
        if(!exists){
            services.put(newService.getName(), newService);
        }
    }
}
