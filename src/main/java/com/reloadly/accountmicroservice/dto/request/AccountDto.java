package com.reloadly.accountmicroservice.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto {

    @NotNull(message = "First Name Cannot be null")
    private String firstName;

    @NotNull(message = "Surname Cannot be null")
    private String surname;

    private String otherName;

    @Email
    @NotNull
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull
    @Column(length = 100)
    private String password;

    @Pattern(regexp = "(\\+)?[0-9]{6,20}$", message = "Phone number must be between 6 and 20 digits")
    private String phoneNumber;

}
