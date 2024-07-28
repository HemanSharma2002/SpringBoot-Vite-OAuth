package org.example.springbootstarteroauthvitebasicsetup.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResponseUserDto {
    private Boolean success;
    private String message;
    private User user;
}
