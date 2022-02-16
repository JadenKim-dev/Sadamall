package sada.sadamall.api.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minidev.json.annotate.JsonIgnore;
import sada.sadamall.oauth.entity.ProviderType;
import sada.sadamall.oauth.entity.RoleType;
import sada.sadamall.util.converter.BooleanToYNConverter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
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
}