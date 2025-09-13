package exe.exe201be.service.ServicePackage;

import exe.exe201be.dto.request.SearchRequest;
import exe.exe201be.dto.response.SearchResponse;
import exe.exe201be.exception.AppException;
import exe.exe201be.exception.ErrorCode;
import exe.exe201be.pojo.ServicePackage;
import exe.exe201be.pojo.type.Status;
import exe.exe201be.repository.ServicePackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicePackageServiceImpl implements ServicePackageService {
    @Autowired
    private ServicePackageRepository servicePackageRepository;

    @Override
    public List<ServicePackage> getAllServicePackages() {
        return servicePackageRepository.findAll();
    }

    @Override
    public ServicePackage getServicePackageById(String id) {
        ServicePackage servicePackage = servicePackageRepository.findById(id).orElse(null);
        if (servicePackage == null) {
            throw new AppException(ErrorCode.SERVICE_PACKAGE_NOT_FOUND);
        }
        return servicePackage;
    }

    @Override
    public ServicePackage createServicePackage(ServicePackageService servicePackage) {
        return null;
    }

    @Override
    public ServicePackage updateServicePackage(String id, ServicePackageService servicePackage) {
        return null;
    }

    @Override
    public void changeStatusServicePackage(String id) {
        ServicePackage servicePackage = servicePackageRepository.findById(id).orElse(null);
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
    public SearchResponse<ServicePackage> searchServicePackages(SearchRequest request) {
        Pageable pageable = PageRequest.of(
                request.getPage() - 1,
                request.getSize(),
                Sort.by(Sort.Direction.fromString(request.getSortDir()), request.getSortBy()));
        Page<ServicePackage> servicePackages = servicePackageRepository.findByNameContainingIgnoreCase(request.getKeyword(), pageable);
        if (servicePackages.hasContent()) {
            return SearchResponse.<ServicePackage>builder()
                    .content(servicePackages.getContent())
                    .page(servicePackages.getNumber() + 1)
                    .size(servicePackages.getSize())
                    .totalPages(servicePackages.getTotalPages())
                    .totalElements(servicePackages.getTotalElements())
                    .build();
        } else {
            throw new AppException(ErrorCode.SERVICE_PACKAGE_NOT_FOUND);
        }
    }
}
