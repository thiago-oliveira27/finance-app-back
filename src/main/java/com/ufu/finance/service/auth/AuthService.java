package com.ufu.finance.service.auth;

import com.ufu.finance.dto.AuthResponseDTO;
import com.ufu.finance.dto.LoginDTO;
import com.ufu.finance.entity.User;
import com.ufu.finance.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    /**
     * Realiza o login do usuário e retorna o token JWT.
     * A mensagem de erro é genérica para não revelar se o email existe ou não.
     */
    public AuthResponseDTO login(LoginDTO loginDTO) {
        User user = userService.buscarPorEmail(loginDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("Email ou senha inválidos"));

        // BCrypt compara a senha digitada com o hash armazenado
        boolean senhaCorreta = userService.getPasswordEncoder()
                .matches(loginDTO.getSenha(), user.getSenha());

        if (!senhaCorreta) {
            throw new RuntimeException("Email ou senha inválidos");
        }

        String token = jwtService.generateToken(user.getId(), user.getNome());
        return new AuthResponseDTO(token, user.getId(), user.getNome());
    }
}
