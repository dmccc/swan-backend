package io.github.myifeng.swan.auth.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ROLE")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@ToString(of = {"id", "name"})
public class Role {
    @Id
    @GeneratedValue(generator="uuidGenerator")
    @GenericGenerator(name = "uuidGenerator", strategy = "uuid")
    @Column(name = "ID", length = 48)
    private String id;

    @Column(name = "NAME", length = 200)
    private String name;

    @Column(name = "DESCRIPTION", length = 200)
    private String description;

    @JsonIgnore
    @Builder.Default
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "ACCOUNT_ROLE",
            joinColumns = @JoinColumn(name = "ROLE_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "ACCOUNT_ID", referencedColumnName = "ID"),
            foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT),
            inverseForeignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT)
    )
    private List<Account> accounts = new ArrayList<>();
}
