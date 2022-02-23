package sada.sadamall.api.entity.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;
import sada.sadamall.oauth.entity.ProviderType;
import sada.sadamall.oauth.entity.RoleType;
import sada.sadamall.api.converter.BooleanToYNConverter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "TB_USER")
public class User {
    public static final String NO_PASSWORD = "NO_PASSWORD";

    @JsonIgnore
    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @Column(name = "OAUTH_ID", unique = true)
    @NotNull
    @Size(max = 64)
    private String oauthId;

    @NotNull
    @Size(max = 100)
    private String username;

    @JsonIgnore
    @NotNull
    @Size(min = 10, max = 128)
    private String password;

    @Email
    @Size(max = 512)
    private String email;

    @NotNull
    @Convert(converter = BooleanToYNConverter.class)
    private boolean isEmailVerified;

    @Size(max = 512)
    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    @NotNull
    private ProviderType providerType;

    @Enumerated(EnumType.STRING)
    @NotNull
    private RoleType roleType;

    @NotNull
    private LocalDateTime createdAt;

    @NotNull
    private LocalDateTime modifiedAt;

    @Builder
    public User(String oauthId, String username, String email, boolean isEmailVerified, String profileImageUrl,
                ProviderType providerType, RoleType roleType,
                LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.oauthId = oauthId;
        this.username = username;
        this.password = NO_PASSWORD;
        this.email = email;
        this.isEmailVerified = isEmailVerified;
        this.profileImageUrl = profileImageUrl;
        this.providerType = providerType;
        this.roleType = roleType;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public void updateUsername(String username) {
        this.username = username;
    }

    public void updateProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return isEmailVerified() == user.isEmailVerified() && Objects.equals(getId(), user.getId()) && Objects.equals(getOauthId(), user.getOauthId()) && Objects.equals(getUsername(), user.getUsername()) && Objects.equals(getPassword(), user.getPassword()) && Objects.equals(getEmail(), user.getEmail()) && Objects.equals(getProfileImageUrl(), user.getProfileImageUrl()) && getProviderType() == user.getProviderType() && getRoleType() == user.getRoleType() && Objects.equals(getCreatedAt(), user.getCreatedAt()) && Objects.equals(getModifiedAt(), user.getModifiedAt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getOauthId(), getUsername(), getPassword(), getEmail(), isEmailVerified(), getProfileImageUrl(), getProviderType(), getRoleType(), getCreatedAt(), getModifiedAt());
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", oauthId='" + oauthId + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", isEmailVerified=" + isEmailVerified +
                ", profileImageUrl='" + profileImageUrl + '\'' +
                ", providerType=" + providerType +
                ", roleType=" + roleType +
                ", createdAt=" + createdAt +
                ", modifiedAt=" + modifiedAt +
                '}';
    }
}
