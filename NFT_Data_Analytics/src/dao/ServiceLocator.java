package dao;

public class ServiceLocator {
	private static Cache cache = new Cache();
    
    private ServiceLocator() {
        throw new IllegalAccessError("Can't construct this class directly");
    }
 
    public static DAOService getService(String serviceName) {
 
    	DAOService service = cache.getService(serviceName);
 
        if (service != null) {
            System.out.println("Get service from cache: " + serviceName);
            return service;
        }
 
        System.out.println("Create a new service and add to cache: " + serviceName);
        InitialContext context = new InitialContext();
        service = (DAOService) context.lookup(serviceName);
        cache.addService(service);
        return service;
    }
}
