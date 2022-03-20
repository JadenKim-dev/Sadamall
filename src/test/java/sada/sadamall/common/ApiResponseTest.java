package sada.sadamall.common;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static sada.sadamall.common.ApiResponse.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ApiResponseTest {
    @Test
    public void success() {
        ApiResponse<String> apiResponse = ApiResponse.success("name", "user1");
        ApiResponseHeader header = apiResponse.getHeader();
        Map<String, String> body = apiResponse.getBody();

        assertThat(header.getCode()).isEqualTo(SUCCESS);
        assertThat(header.getMessage()).isEqualTo(SUCCESS_MESSAGE);
        assertThat(body).isEqualTo(Map.of("name", "user1"));
    }

    @Test
    public void fail() {
        ApiResponse<String> apiResponse = ApiResponse.fail();
        ApiResponseHeader header = apiResponse.getHeader();
        Map<String, String> body = apiResponse.getBody();

        assertThat(header.getCode()).isEqualTo(FAILED);
        assertThat(header.getMessage()).isEqualTo(FAILED_MESSAGE);
        assertThat(body).isEqualTo(null);
    }

    @Test
    public void invalidAccessToken() {
        ApiResponse<String> apiResponse = ApiResponse.invalidAccessToken();
        ApiResponseHeader header = apiResponse.getHeader();
        Map<String, String> body = apiResponse.getBody();

        assertThat(header.getCode()).isEqualTo(FAILED);
        assertThat(header.getMessage()).isEqualTo(INVALID_ACCESS_TOKEN_MESSAGE);
        assertThat(body).isEqualTo(null);
    }

    @Test
    public void invalidRefreshToken() throws Exception {
        ApiResponse<String> apiResponse = ApiResponse.invalidRefreshToken();
        ApiResponseHeader header = apiResponse.getHeader();
        Map<String, String> body = apiResponse.getBody();

        assertThat(header.getCode()).isEqualTo(FAILED);
        assertThat(header.getMessage()).isEqualTo(INVALID_REFRESH_TOKEN_MESSAGE);
        assertThat(body).isEqualTo(null);
    }

    @Test
    public void notExpiredTokenYet() {
        ApiResponse<String> apiResponse = ApiResponse.notExpiredTokenYet();
        ApiResponseHeader header = apiResponse.getHeader();
        Map<String, String> body = apiResponse.getBody();

        assertThat(header.getCode()).isEqualTo(FAILED);
        assertThat(header.getMessage()).isEqualTo(NOT_EXPIRED_TOKEN_YET_MESSAGE);
        assertThat(body).isEqualTo(null);
    }
}
