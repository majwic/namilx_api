package com.majwic.configuration;

import com.majwic.model.Profile;
import com.majwic.model.Role;
import com.majwic.repository.ProfileRepository;
import com.majwic.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class RoleDataInitializer {

    @Bean
    public CommandLineRunner initData(RoleRepository roleRepo, ProfileRepository profileRepo) {
        return (_) -> {
            if (roleRepo.count() == 0) {
                Role userRole = new Role();
                userRole.setName("USER");
                roleRepo.save(userRole);

                Role adminRole = new Role();
                adminRole.setName("ADMIN");
                roleRepo.save(adminRole);

                Role moderatorRole = new Role();
                moderatorRole.setName("MODERATOR");
                roleRepo.save(moderatorRole);

                Profile profile = new Profile();
                profile.setEmail("admin@admin.admin");
                profile.setPassword("${admin.password}");
                profile.setDisplayName("admin");
                profile.setRoles(Arrays.asList(userRole, adminRole, moderatorRole));
                profileRepo.save(profile);
            }
        };
    }
}
