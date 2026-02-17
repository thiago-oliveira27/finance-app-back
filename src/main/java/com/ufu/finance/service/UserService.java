package com.ufu.finance.service;

import com.ufu.finance.dto.UserDTO;
import com.ufu.finance.dto.UserResponseDTO;
import com.ufu.finance.entity.User;
import com.ufu.finance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // BCrypt com força 12 (padrão é 10; 12 é mais seguro sem impacto perceptível)
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    /**
     * Retorna o encoder para que outros serviços possam validar senhas
     */
    public BCryptPasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    /**
     * Cadastra um novo usuário com senha criptografada
     */
    public UserResponseDTO cadastrarUsuario(UserDTO userDTO) {
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new RuntimeException("Email já está em uso");
        }

        User user = new User();
        user.setNome(userDTO.getNome().trim());
        user.setEmail(userDTO.getEmail().toLowerCase().trim());
        user.setSenha(passwordEncoder.encode(userDTO.getSenha())); // 🔒 hash BCrypt

        User savedUser = userRepository.save(user);
        return new UserResponseDTO(savedUser);
    }

    /**
     * Busca um usuário por ID
     */
    public UserResponseDTO buscarPorId(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        return new UserResponseDTO(user);
    }

    /**
     * Lista todos os usuários
     */
    public List<UserResponseDTO> listarTodos() {
        return userRepository.findAll()
                .stream()
                .map(UserResponseDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Busca usuário por email (usado no login)
     */
    public Optional<User> buscarPorEmail(String email) {
        return userRepository.findByEmail(email.toLowerCase().trim());
    }
}