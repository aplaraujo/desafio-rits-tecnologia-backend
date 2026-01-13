package io.github.aplaraujo.services;

import io.github.aplaraujo.entities.Client;
import io.github.aplaraujo.entities.Role;
import io.github.aplaraujo.entities.enums.RoleEnum;
import io.github.aplaraujo.repositories.ClientRepository;
import io.github.aplaraujo.repositories.RoleRepository;
import io.github.aplaraujo.security.UserDetailsImpl;
import io.github.aplaraujo.services.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Client client = clientRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Email not found!"));
        return UserDetailsImpl.build(client);
    }

    public String addUser(Client client) {
        client.setPassword(passwordEncoder.encode(client.getPassword()));
        Set<Role> roles = new HashSet<>();
        if (client.getAuthority() != null && !client.getAuthority().isEmpty()) {
            for (String auth : client.getAuthority()) {
                try {
                    RoleEnum roleEnum = RoleEnum.valueOf(auth);

                    Role role = roleRepository.findByAuthority(roleEnum)
                            .orElseGet(() -> {
                                Role newRole = new Role();
                                newRole.setAuthority(roleEnum);
                                return roleRepository.save(newRole);
                            });
                    roles.add(role);
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException("Invalid role: " + auth);
                }
            }
        }
        client.setRoles(roles);
        clientRepository.save(client);
        return "Client added successfully!";
    }
}
