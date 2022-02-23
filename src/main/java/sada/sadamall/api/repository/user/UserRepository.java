package sada.sadamall.api.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sada.sadamall.api.entity.user.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByOauthId(String oauthId);
}
