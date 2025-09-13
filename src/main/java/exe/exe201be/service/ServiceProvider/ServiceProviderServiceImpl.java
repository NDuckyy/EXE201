package exe.exe201be.service.ServiceProvider;

import exe.exe201be.exception.AppException;
import exe.exe201be.exception.ErrorCode;
import exe.exe201be.pojo.ServiceProvider;
import exe.exe201be.pojo.type.Status;
import exe.exe201be.repository.ServiceProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceProviderServiceImpl implements ServiceProviderService {
    @Autowired
    private ServiceProviderRepository serviceProviderRepository;

    @Override
    public List<ServiceProvider> getAllServiceProviders() {
        return serviceProviderRepository.findAll();
    }

    @Override
    public ServiceProvider getServiceProviderById(String id) {
        ServiceProvider serviceProvider = serviceProviderRepository.findById(id).orElse(null);
        if(serviceProvider == null){
            throw new AppException(ErrorCode.SERVICE_PROVIDER_NOT_FOUND);
        }
        return serviceProvider;
    }

    @Override
    public ServiceProvider createServiceProvider(ServiceProvider serviceProvider) {
        return null;
    }

    @Override
    public ServiceProvider updateServiceProvider(String id, ServiceProvider serviceProvider) {
        return null;
    }

    @Override
    public void changeStatusServiceProvider(String id) {
        ServiceProvider serviceProvider = serviceProviderRepository.findById(id).orElse(null);
        if(serviceProvider == null){
            throw new AppException(ErrorCode.SERVICE_PROVIDER_NOT_FOUND);
        }
        if(serviceProvider.getStatus() == Status.ACTIVE){
            serviceProvider.setStatus(Status.INACTIVE);
            serviceProviderRepository.save(serviceProvider);
        } else {
            serviceProvider.setStatus(Status.ACTIVE);
            serviceProviderRepository.save(serviceProvider);
        }
    }
}
