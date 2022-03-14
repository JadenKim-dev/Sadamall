package sada.sadamall.api.entity.user;

import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

@Getter
@NoArgsConstructor
@Entity
public class UserRefreshToken {
    @JsonIgnore
    @Id @GeneratedValue
    @Column(name="refresh_token_id")
    private Long id;

    @Column(unique = true)
    @NotNull
    @Size(max = 64)
    private String oauthId;

    @NotNull
    @Size(max = 256)
    private String refreshToken;

    public static UserRefreshToken newToken(String oauthId, String refreshToken) {
        return new UserRefreshToken(oauthId, refreshToken);

    }

    private UserRefreshToken(String oauthId, String refreshToken) {
        this.oauthId = oauthId;
        this.refreshToken = refreshToken;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRefreshToken that = (UserRefreshToken) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getOauthId(), that.getOauthId()) && Objects.equals(getRefreshToken(), that.getRefreshToken());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getOauthId(), getRefreshToken());
    }

    @Override
    public String toString() {
        return "UserRefreshToken{" +
                "id=" + id +
                ", oauthId='" + oauthId + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                '}';
    }
}
