package com.example.messengerserver.controller;

import com.example.messengerserver.model.User;
import com.example.messengerserver.repository.UserRepository;
import com.example.messengerserver.repository.GroupRepository;
import com.example.messengerserver.repository.GroupMemberRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final GroupMemberRepository groupMemberRepository;

    public UserController(UserRepository userRepository,
                          GroupRepository groupRepository,
                          GroupMemberRepository groupMemberRepository) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.groupMemberRepository = groupMemberRepository;
    }

    @GetMapping("/users")
    public List<UserDTO> getAllUsers(Authentication auth) {
        Long currentUserId = (Long) auth.getPrincipal();

        return userRepository.findAll().stream()
                .filter(u -> !Objects.equals(u.getId(), currentUserId))
                .map(u -> new UserDTO(u.getId(), u.getUsername()))
                .collect(Collectors.toList());
    }

    @GetMapping("/users/me")
    public ResponseEntity<CurrentUserDto> getCurrentUser(Authentication auth) {
        Long userId = (Long) auth.getPrincipal();
        return userRepository.findById(userId)
                .map(u -> ResponseEntity.ok(new CurrentUserDto(u.getId(), u.getUsername(), u.getEmail())))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<String> getUsername(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(u -> ResponseEntity.ok(u.getUsername()))
                .orElse(ResponseEntity.notFound().build());
    }

    public record UserDTO(Long id, String username) {}
    public record CurrentUserDto(Long id, String username, String email) { }

}
