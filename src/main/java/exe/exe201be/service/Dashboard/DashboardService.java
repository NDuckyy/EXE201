package exe.exe201be.service.Dashboard;

import exe.exe201be.dto.response.DashboardProviderResponse;
import exe.exe201be.dto.response.TotalResponse;
import org.bson.types.ObjectId;

public interface DashboardService {
    TotalResponse countData(ObjectId managerId);

    DashboardProviderResponse getDashboardProvider(ObjectId providerId);
}
