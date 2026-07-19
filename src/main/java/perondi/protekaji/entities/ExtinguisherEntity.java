package perondi.protekaji.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UuidGenerator;
import perondi.protekaji.entities.enums.ExtinguisherType;
import perondi.protekaji.entities.enums.Unit;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "extinguisher")
public class ExtinguisherEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private BatchEntity batch_id;

    @Column(nullable = false, unique = true)
    private String serie_number;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExtinguisherType extinguisher_type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Unit unit;

    @Column(nullable = false)
    private String manufacturer;
}