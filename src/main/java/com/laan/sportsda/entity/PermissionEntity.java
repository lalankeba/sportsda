package com.laan.sportsda.entity;

import com.laan.sportsda.enums.PermissionDescription;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
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

}
