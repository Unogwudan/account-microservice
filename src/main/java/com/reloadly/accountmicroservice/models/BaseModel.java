package com.reloadly.accountmicroservice.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@MappedSuperclass
public abstract class BaseModel<T> {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	private LocalDateTime created = LocalDateTime.now();

    private LocalDateTime modified = LocalDateTime.now();

}
