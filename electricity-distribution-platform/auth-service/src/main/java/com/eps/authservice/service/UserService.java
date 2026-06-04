package com.eps.authservice.service;

import com.eps.authservice.dto.LoginRequestDTO;
import com.eps.authservice.dto.LoginResponseDTO;
import com.eps.authservice.dto.UserRequestDTO;
import com.eps.authservice.dto.UserResponseDTO;
import com.eps.authservice.exception.InvalidCredentialsException;
import com.eps.authservice.exception.UserAlreadyExistsException;
import com.eps.authservice.exception.UserNotFoundException;
import com.eps.authservice.model.User;
import com.eps.authservice.repository.UserRepository;
import com.eps.authservice.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtil jwtUtil;

  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtUtil = jwtUtil;
  }

  public UserResponseDTO registerUser(UserRequestDTO userRequestDTO) {
    if (userRepository.existsByUsername(userRequestDTO.getUsername())) {
      throw new UserAlreadyExistsException("Username already exists: " + userRequestDTO.getUsername());
    }
    if (userRepository.existsByEmail(userRequestDTO.getEmail())) {
      throw new UserAlreadyExistsException("Email already exists: " + userRequestDTO.getEmail());
    }

    User user = new User(
        userRequestDTO.getUsername(),
        userRequestDTO.getEmail(),
        passwordEncoder.encode(userRequestDTO.getPassword()),
        userRequestDTO.getRole());

    User savedUser = userRepository.save(user);
    return convertToResponseDTO(savedUser);
  }

  public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
    User user = userRepository.findByUsername(loginRequestDTO.getUsername())
        .orElseThrow(() -> new InvalidCredentialsException("Invalid username or password"));

    if (!passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword())) {
      throw new InvalidCredentialsException("Invalid username or password");
    }

    if (!user.getActive()) {
      throw new InvalidCredentialsException("User account is inactive");
    }

    String accessToken = jwtUtil.generateToken(user);
    String refreshToken = jwtUtil.generateRefreshToken(user.getUsername());

    return new LoginResponseDTO(accessToken, refreshToken);
  }

  public UserResponseDTO getUserById(UUID id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
    return convertToResponseDTO(user);
  }

  public UserResponseDTO getUserByUsername(String username) {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));
    return convertToResponseDTO(user);
  }

  public List<UserResponseDTO> getAllUsers() {
    return userRepository.findAll().stream()
        .map(this::convertToResponseDTO)
        .toList();
  }

  public UserResponseDTO updateUser(UUID id, UserRequestDTO userRequestDTO) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));

    if (!user.getUsername().equals(userRequestDTO.getUsername()) &&
        userRepository.existsByUsername(userRequestDTO.getUsername())) {
      throw new UserAlreadyExistsException("Username already exists: " + userRequestDTO.getUsername());
    }

    if (!user.getEmail().equals(userRequestDTO.getEmail()) &&
        userRepository.existsByEmail(userRequestDTO.getEmail())) {
      throw new UserAlreadyExistsException("Email already exists: " + userRequestDTO.getEmail());
    }

    user.setUsername(userRequestDTO.getUsername());
    user.setEmail(userRequestDTO.getEmail());
    user.setRole(userRequestDTO.getRole());

    if (userRequestDTO.getPassword() != null && !userRequestDTO.getPassword().isEmpty()) {
      user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
    }

    User updatedUser = userRepository.save(user);
    return convertToResponseDTO(updatedUser);
  }

  public void deleteUser(UUID id) {
    if (!userRepository.existsById(id)) {
      throw new UserNotFoundException("User not found with ID: " + id);
    }
    userRepository.deleteById(id);
  }

  public String validateAndRefreshToken(String accessToken) {
    if (!jwtUtil.validateToken(accessToken)) {
      throw new InvalidCredentialsException("Invalid or expired token");
    }
    String username = jwtUtil.extractUsername(accessToken);
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UserNotFoundException("User not found"));
    return jwtUtil.generateToken(user);
  }

  private UserResponseDTO convertToResponseDTO(User user) {
    return new UserResponseDTO(
        user.getId(),
        user.getUsername(),
        user.getEmail(),
        user.getRole(),
        user.getActive());
  }
}
