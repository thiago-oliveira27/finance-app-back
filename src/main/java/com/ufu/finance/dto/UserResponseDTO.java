package com.ufu.finance.dto;

import com.ufu.finance.entity.User;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
public class UserResponseDTO {
    
    private Long id;
    private String nome;
    private String email;
    private LocalDateTime dataInclusao;
    
    public UserResponseDTO() {
    }
    
    public UserResponseDTO(User user) {
        this.id = user.getId();
        this.nome = user.getNome();
        this.email = user.getEmail();
        this.dataInclusao = user.getDataInclusao();
    }
}
