package com.majwic.repository;

import com.majwic.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    boolean existsByEmail(String email);
    Optional<Profile> findByEmail(String email);
}
