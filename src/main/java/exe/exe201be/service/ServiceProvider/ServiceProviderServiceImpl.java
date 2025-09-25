package exe.exe201be.service.ServiceProvider;

import exe.exe201be.dto.request.CreateServiceProviderRequest;
import exe.exe201be.dto.response.ServiceProviderResponse;
import exe.exe201be.exception.AppException;
import exe.exe201be.exception.ErrorCode;
import exe.exe201be.pojo.Role;
import exe.exe201be.pojo.ServiceProvider;
import exe.exe201be.pojo.UserGlobalRole;
import exe.exe201be.pojo.type.Status;
import exe.exe201be.repository.RoleRepository;
import exe.exe201be.repository.ServiceProviderRepository;
import exe.exe201be.repository.UserGlobalRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceProviderServiceImpl implements ServiceProviderService {
    @Autowired
    private ServiceProviderRepository serviceProviderRepository;

    @Autowired
    private UserGlobalRepository userGlobalRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public List<ServiceProvider> getAllServiceProviders() {
        return serviceProviderRepository.findAll();
    }

    @Override
    public ServiceProvider getServiceProviderById(String id) {
        ObjectId objectId = new ObjectId(id);
        ServiceProvider serviceProvider = serviceProviderRepository.findById(objectId).orElse(null);
        if (serviceProvider == null) {
            throw new AppException(ErrorCode.SERVICE_PROVIDER_NOT_FOUND);
        }
        return serviceProvider;
    }

    @Override
    public ServiceProviderResponse createServiceProvider(ObjectId userId, CreateServiceProviderRequest request) {
        if (serviceProviderRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.SERVICE_PROVIDER_NAME_ALREADY_EXISTS);
        }
        if (serviceProviderRepository.existsByContactEmail(request.getContactEmail())) {
            throw new AppException(ErrorCode.SERVICE_PROVIDER_CONTACT_EMAIL_ALREADY_EXISTS);
        }
        if (serviceProviderRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new AppException(ErrorCode.SERVICE_PROVIDER_PHONE_NUMBER_ALREADY_EXISTS);
        }

        UserGlobalRole userGlobalRole = userGlobalRepository.findByUserId(userId);
        Role role = roleRepository.findByKey("PROVIDER");
        userGlobalRole.setRoleId(role.getId());
        userGlobalRepository.save(userGlobalRole);

        ServiceProvider serviceProvider = ServiceProvider.builder()
                .name(request.getName())
                .contactEmail(request.getContactEmail())
                .phoneNumber(request.getPhoneNumber())
                .address(request.getAddress())
                .website(request.getWebsite())
                .status(Status.INACTIVE)
                .build();

        serviceProviderRepository.save(serviceProvider);
        return ServiceProviderResponse.builder()
                .id(serviceProvider.getId().toHexString())
                .name(serviceProvider.getName())
                .contactEmail(serviceProvider.getContactEmail())
                .phoneNumber(serviceProvider.getPhoneNumber())
                .address(serviceProvider.getAddress())
                .website(serviceProvider.getWebsite())
                .build();
    }

    @Override
    public ServiceProvider updateServiceProvider(String id, ServiceProvider serviceProvider) {
        return null;
    }

    @Override
    public void changeStatusServiceProvider(String id) {
        ObjectId objectId = new ObjectId(id);
        ServiceProvider serviceProvider = serviceProviderRepository.findById(objectId).orElse(null);
        if (serviceProvider == null) {
            throw new AppException(ErrorCode.SERVICE_PROVIDER_NOT_FOUND);
        }
        if (serviceProvider.getStatus() == Status.ACTIVE) {
            serviceProvider.setStatus(Status.INACTIVE);
            serviceProviderRepository.save(serviceProvider);
        } else {
            serviceProvider.setStatus(Status.ACTIVE);
            serviceProviderRepository.save(serviceProvider);
        }
    }
}
