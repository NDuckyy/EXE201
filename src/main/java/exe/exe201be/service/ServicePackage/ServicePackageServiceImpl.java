package exe.exe201be.service.ServicePackage;

import exe.exe201be.dto.request.SearchRequest;
import exe.exe201be.dto.response.SearchResponse;
import exe.exe201be.dto.response.ServicePackageResponse;
import exe.exe201be.dto.response.ServiceProviderResponse;
import exe.exe201be.exception.AppException;
import exe.exe201be.exception.ErrorCode;
import exe.exe201be.pojo.ServicePackage;
import exe.exe201be.pojo.ServiceProvider;
import exe.exe201be.pojo.type.Status;
import exe.exe201be.repository.ServicePackageRepository;
import exe.exe201be.repository.ServiceProviderRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ServicePackageServiceImpl implements ServicePackageService {
    @Autowired
    private ServicePackageRepository servicePackageRepository;

    @Autowired
    private ServiceProviderRepository serviceProviderRepository;

    @Override
    public List<ServicePackageResponse> getAllServicePackages() {
        List<ServicePackage> servicePackages = servicePackageRepository.findAll();
        if (servicePackages.isEmpty()) {
            return null;
        }

        Set<ObjectId> serviceProviders = servicePackages.stream()
                .map(ServicePackage::getProviderId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Map<ObjectId, ServiceProvider> providersById = serviceProviderRepository.findAllById(serviceProviders).stream()
                .collect(Collectors.toMap(ServiceProvider::getId, Function.identity()));

        return servicePackages.stream()
                .map(sp -> {
                    ServiceProvider provider = providersById.get(sp.getProviderId());
                    return getServicePackageResponse(sp, provider);
                })
                .collect(Collectors.toList());
    }

    @Override
    public ServicePackageResponse getServicePackageById(String id) {
        ObjectId objectId = new ObjectId(id);
        ServicePackage servicePackage = servicePackageRepository.findById(objectId).orElse(null);
        if (servicePackage == null) {
            throw new AppException(ErrorCode.SERVICE_PACKAGE_NOT_FOUND);
        }
        ServiceProvider serviceProvider = serviceProviderRepository.findById(servicePackage.getProviderId()).orElse(null);
        return getServicePackageResponse(servicePackage, serviceProvider);
    }

    @Override
    public ServicePackageResponse createServicePackage(ServicePackageService servicePackage) {
        return null;
    }

    @Override
    public ServicePackageResponse updateServicePackage(String id, ServicePackageService servicePackage) {
        return null;
    }

    @Override
    public void changeStatusServicePackage(String id) {
        ObjectId objectId = new ObjectId(id);
        ServicePackage servicePackage = servicePackageRepository.findById(objectId).orElse(null);
        if (servicePackage == null) {
            throw new AppException(ErrorCode.SERVICE_PACKAGE_NOT_FOUND);
        } else {
            if (servicePackage.getStatus() == Status.ACTIVE) {
                servicePackage.setStatus(Status.INACTIVE);
                servicePackageRepository.save(servicePackage);
            } else {
                servicePackage.setStatus(Status.ACTIVE);
                servicePackageRepository.save(servicePackage);
            }
        }
    }

    @Override
    public SearchResponse<ServicePackageResponse> searchServicePackages(SearchRequest request) {
        Pageable pageable = PageRequest.of(
                request.getPage() - 1,
                request.getSize(),
                Sort.by(Sort.Direction.fromString(request.getSortDir()), request.getSortBy()));
        Page<ServicePackage> servicePackages = servicePackageRepository.findByNameContainingIgnoreCase(request.getKeyword(), pageable);

        if (servicePackages.hasContent()) {
            return SearchResponse.<ServicePackageResponse>builder()
                    .content(servicePackages.stream().map(sp -> {
                        ServiceProvider provider = serviceProviderRepository.findById(sp.getProviderId()).orElse(null);
                        return getServicePackageResponse(sp, provider);
                    }).collect(Collectors.toList()))

                    .page(servicePackages.getNumber() + 1)
                    .size(servicePackages.getSize())
                    .totalPages(servicePackages.getTotalPages())
                    .totalElements(servicePackages.getTotalElements())
                    .build();
        } else {
            throw new AppException(ErrorCode.SERVICE_PACKAGE_NOT_FOUND);
        }
    }

    private ServicePackageResponse getServicePackageResponse(ServicePackage sp, ServiceProvider provider) {
        ServiceProviderResponse providerResponse = (provider == null) ? null :
                ServiceProviderResponse.builder()
                        .id(provider.getId().toHexString())
                        .name(provider.getName())
                        .contactEmail(provider.getContactEmail())
                        .phoneNumber(provider.getPhoneNumber())
                        .address(provider.getAddress())
                        .website(provider.getWebsite())
                        .build();

        return ServicePackageResponse.builder()
                .id(sp.getId().toHexString())
                .providerId(providerResponse)
                .name(sp.getName())
                .description(sp.getDescription())
                .price(sp.getPrice())
                .currency(sp.getCurrency())
                .durationMonths(sp.getDurationMonths())
                .discountPercent(sp.getDiscountPercent())
                .features(sp.getFeatures())
                .image(sp.getImage())
                .status(sp.getStatus())
                .build();
    }
}
