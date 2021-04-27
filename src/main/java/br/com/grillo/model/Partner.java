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
@Entity
@Table(name = "mn_partner", uniqueConstraints = {
        @UniqueConstraint(columnNames = "document")
})
public class Partner implements Serializable {

    private static final long serialVersionUID = -6634787956064782936L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter
    private Long code;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String document;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updatedDate;

    @Column(columnDefinition = "BINARY(16)", nullable = false, updatable = false)
    @Setter
    private UUID externalCode;

}
