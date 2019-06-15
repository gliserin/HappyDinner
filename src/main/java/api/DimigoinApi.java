package api;

import model.DimiBob;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DimigoinApi {

    @GET("dimibobs/{date}")
    Call<DimiBob> dimiBob(@Path("date") String date);

}
