package com.reloadly.accountmicroservice.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@Builder
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "account")
public class Account extends BaseModel<Account> {

    @NotNull
    private String firstName;

    @NotNull
    private String surname;

    private String otherName;

    @Email
    @NotNull
    @Column(unique = true)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull
    @Column(length = 100)
    private String password;

    @Pattern(regexp = "(\\+)?[0-9]{6,20}$", message = "Phone number must be between 6 and 20 digits")
    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(nullable = false)
    private Boolean active;

//    @Column(nullable = false)
//    private Boolean deleted = Boolean.FALSE;

//    @ApiModelProperty(hidden = true)
//    @Transient
//    private List<Permission> permissions;

//    @ManyToOne
//    @JoinColumn(name = "role")
//    @Cascade(value = org.hibernate.annotations.CascadeType.MERGE)
//    private Role userRole;

}