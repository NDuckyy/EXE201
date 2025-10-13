package exe.exe201be.service.ServicePackage;

import exe.exe201be.dto.request.CreateServicePackageRequest;
import exe.exe201be.dto.request.SearchRequest;
import exe.exe201be.dto.response.SearchResponse;
import exe.exe201be.dto.response.ServicePackageResponse;
import exe.exe201be.pojo.ServicePackage;
import org.bson.types.ObjectId;

import java.util.List;

public interface ServicePackageService {
    List<ServicePackageResponse> getAllServicePackages();
    ServicePackageResponse getServicePackageById(String id);
    ServicePackageResponse createServicePackage(ObjectId providerId,CreateServicePackageRequest servicePackage);
    ServicePackageResponse updateServicePackage(ObjectId id, CreateServicePackageRequest servicePackage);
    void changeStatusServicePackage(String id);
    List<ServicePackageResponse> getServicePackagesByProviderId(ObjectId providerId);
    SearchResponse<ServicePackageResponse> searchServicePackages(SearchRequest request);
}
