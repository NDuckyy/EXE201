package exe.exe201be.service;

import exe.exe201be.pojo.ServiceProvider;

import java.util.List;

public interface ServiceProviderService {
    List<ServiceProvider>  getAllServiceProviders();
    ServiceProvider getServiceProviderById(String id);
    ServiceProvider createServiceProvider(ServiceProvider serviceProvider);
    ServiceProvider updateServiceProvider(String id, ServiceProvider serviceProvider);
    void changeStatusServiceProvider(String id);
}
