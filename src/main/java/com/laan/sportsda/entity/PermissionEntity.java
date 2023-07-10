package com.laan.sportsda.entity;

import com.laan.sportsda.enums.PermissionDescription;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@Entity
@Table(name = "permission")
public class PermissionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "description")
    @Enumerated(EnumType.STRING)
    private PermissionDescription description;

    @ManyToMany(mappedBy = "permissionEntities")
    @ToString.Exclude
    private List<RoleEntity> roleEntities;

    @Column(name = "version")
    @Version
    private Long version;

}
