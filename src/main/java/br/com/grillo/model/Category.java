package br.com.grillo.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity(name = "mn_category")
public class Category implements Serializable {

    private static final long serialVersionUID = 3097493607885136438L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long code;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private char status;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updatedDate;

    @Column(columnDefinition = "BINARY(16)", nullable = false, updatable = false)
    private UUID externalCode;

}

