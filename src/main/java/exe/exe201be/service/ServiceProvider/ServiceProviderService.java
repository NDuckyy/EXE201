package exe.exe201be.service.ServiceProvider;

import exe.exe201be.dto.request.CreateServiceProviderRequest;
import exe.exe201be.dto.response.ServiceProviderResponse;
import exe.exe201be.pojo.ServiceProvider;
import org.bson.types.ObjectId;

import java.util.List;

public interface ServiceProviderService {
    List<ServiceProvider>  getAllServiceProviders();
    ServiceProvider getServiceProviderById(String id);
    ServiceProviderResponse createServiceProvider(ObjectId userId, CreateServiceProviderRequest request);
    ServiceProviderResponse updateServiceProvider(ObjectId id, CreateServiceProviderRequest serviceProvider);
    void changeStatusServiceProvider(String id);

    List<ServiceProvider>  getAllInactiveServiceProviders();
}
