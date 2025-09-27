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
        ServiceProvider existingProvider = serviceProviderRepository.findByUserId(userId);
        if (existingProvider != null) {
            throw new AppException(ErrorCode.USER_ALREADY_A_SERVICE_PROVIDER);
        }
        if (serviceProviderRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.SERVICE_PROVIDER_NAME_ALREADY_EXISTS);
        }
        if (serviceProviderRepository.existsByContactEmail(request.getContactEmail())) {
            throw new AppException(ErrorCode.SERVICE_PROVIDER_CONTACT_EMAIL_ALREADY_EXISTS);
        }
        if (serviceProviderRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new AppException(ErrorCode.SERVICE_PROVIDER_PHONE_NUMBER_ALREADY_EXISTS);
        }

        ServiceProvider serviceProvider = ServiceProvider.builder()
                .userId(userId)
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
    public ServiceProviderResponse updateServiceProvider(ObjectId id, CreateServiceProviderRequest serviceProvider) {
        ServiceProvider existingProvider = serviceProviderRepository.findById(id).orElse(null);
        if (existingProvider == null) {
            throw new AppException(ErrorCode.SERVICE_PROVIDER_NOT_FOUND);
        }
        if (!existingProvider.getName().equals(serviceProvider.getName()) &&
                serviceProviderRepository.existsByName(serviceProvider.getName())) {
            throw new AppException(ErrorCode.SERVICE_PROVIDER_NAME_ALREADY_EXISTS);
        }
        if (!existingProvider.getContactEmail().equals(serviceProvider.getContactEmail()) &&
                serviceProviderRepository.existsByContactEmail(serviceProvider.getContactEmail())) {
            throw new AppException(ErrorCode.SERVICE_PROVIDER_CONTACT_EMAIL_ALREADY_EXISTS);
        }
        if (!existingProvider.getPhoneNumber().equals(serviceProvider.getPhoneNumber()) &&
                serviceProviderRepository.existsByPhoneNumber(serviceProvider.getPhoneNumber())) {
            throw new AppException(ErrorCode.SERVICE_PROVIDER_PHONE_NUMBER_ALREADY_EXISTS);
        }

        if (serviceProvider.getName() != null) {
            existingProvider.setName(serviceProvider.getName());
        }
        if (serviceProvider.getContactEmail() != null) {
            existingProvider.setContactEmail(serviceProvider.getContactEmail());
        }
        if (serviceProvider.getPhoneNumber() != null) {
            existingProvider.setPhoneNumber(serviceProvider.getPhoneNumber());
        }
        if (serviceProvider.getAddress() != null) {
            existingProvider.setAddress(serviceProvider.getAddress());
        }
        if (serviceProvider.getWebsite() != null) {
            existingProvider.setWebsite(serviceProvider.getWebsite());
        }
        serviceProviderRepository.save(existingProvider);
        return ServiceProviderResponse.builder()
                .id(existingProvider.getId().toHexString())
                .name(existingProvider.getName())
                .contactEmail(existingProvider.getContactEmail())
                .phoneNumber(existingProvider.getPhoneNumber())
                .address(existingProvider.getAddress())
                .website(existingProvider.getWebsite())
                .build();
    }

    @Override
    public void changeStatusServiceProvider(String id) {
        ObjectId objectId = new ObjectId(id);
        ServiceProvider serviceProvider = serviceProviderRepository.findById(objectId).orElse(null);
        if (serviceProvider == null) {
            throw new AppException(ErrorCode.SERVICE_PROVIDER_NOT_FOUND);
        }
        if (serviceProvider.getStatus() == Status.ACTIVE) {
            UserGlobalRole userGlobalRole = userGlobalRepository.findByUserId(serviceProvider.getUserId());
            Role role = roleRepository.findByKey("USER");
            userGlobalRole.setRoleId(role.getId());
            userGlobalRepository.save(userGlobalRole);

            serviceProvider.setStatus(Status.INACTIVE);
            serviceProviderRepository.save(serviceProvider);
        } else {
            UserGlobalRole userGlobalRole = userGlobalRepository.findByUserId(serviceProvider.getUserId());
            Role role = roleRepository.findByKey("PROVIDER");
            userGlobalRole.setRoleId(role.getId());
            userGlobalRepository.save(userGlobalRole);

            serviceProvider.setStatus(Status.ACTIVE);
            serviceProviderRepository.save(serviceProvider);
        }
    }
}
