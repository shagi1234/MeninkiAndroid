package tm.store.meninki.api.data.response;

public class DataCheckSms {
    String accessToken;
    String validTo;
    String refreshToken;
    String userId;

    public String getAccessToken() {
        return accessToken;
    }

    public String getValidTo() {
        return validTo;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getUserId() {
        return userId;
    }
}
