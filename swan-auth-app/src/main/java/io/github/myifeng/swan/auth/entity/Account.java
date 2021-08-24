package io.github.myifeng.swan.auth.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ACCOUNT")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@ToString(of = {"id", "username"})
public class Account {
    @Id
    @GeneratedValue(generator="uuidGenerator")
    @GenericGenerator(name = "uuidGenerator", strategy = "uuid")
    @Column(name="ID", length = 48)
    private String id;

    @Column(name = "USERNAME", length = 200)
    private String username;

    @Column(name = "MOBILE", length = 32)
    private String mobile;

    @Column(name = "PASSWORD", length = 200)
    private String password;

    @Column(name = "NICK_NAME", length = 200)
    private String nickName;

    @Column(name = "GENDER", length = 4)
    private String gender;

    @Temporal(TemporalType.DATE)
    @Column(name = "BIRTH_DAY")
    private Date birthDay;

    @Column(name = "REGISTER_DATE")
    private Date registerDate;

    @PrePersist
    public void setDefault() {
        if (registerDate == null) {
            setRegisterDate(new Date());
        }
    }

    @Builder.Default
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "ACCOUNT_ROLE",
            joinColumns = @JoinColumn(name = "ACCOUNT_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "ROLE_ID", referencedColumnName = "ID"),
            foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT),
            inverseForeignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT)
    )
    private Set<Role> roles = new HashSet<>();

    @Builder.Default
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "ACCOUNT_AUTHORITY",
            joinColumns = @JoinColumn(name = "ACCOUNT_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "AUTHORITY_ID", referencedColumnName = "ID"),
            foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT),
            inverseForeignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT)
    )
    private Set<Authority> authorities = new HashSet<>();

}
