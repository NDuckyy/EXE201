package exe.exe201be.service.ServicePackage;

import exe.exe201be.dto.request.SearchRequest;
import exe.exe201be.dto.response.SearchResponse;
import exe.exe201be.dto.response.ServicePackageResponse;
import exe.exe201be.pojo.ServicePackage;

import java.util.List;

public interface ServicePackageService {
    List<ServicePackageResponse> getAllServicePackages();
    ServicePackage getServicePackageById(String id);
    ServicePackage createServicePackage(ServicePackageService servicePackage);
    ServicePackage updateServicePackage(String id, ServicePackageService servicePackage);
    void changeStatusServicePackage(String id);
    SearchResponse<ServicePackage> searchServicePackages(SearchRequest request);
}
