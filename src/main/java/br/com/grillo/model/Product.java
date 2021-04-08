package br.com.grillo.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity(name = "mn_product")
public class Product implements Serializable {

    private static final long serialVersionUID = -9014722988729748133L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long code;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updatedDate;

    @Column(nullable = false)
    private char status;

    @JoinColumn(name = "category_code", referencedColumnName = "code")
    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    @Column(columnDefinition = "BINARY(16)", nullable = false, updatable = false)
    private UUID externalCode;

}
