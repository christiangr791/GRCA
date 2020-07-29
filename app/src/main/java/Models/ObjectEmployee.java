package Models;

import com.google.gson.annotations.SerializedName;

public class ObjectEmployee {

    @SerializedName("data")
    private DataEmployee data;

    public DataEmployee getData() {
        return data;
    }

    public void setData(DataEmployee data) {
        this.data = data;
    }
}
