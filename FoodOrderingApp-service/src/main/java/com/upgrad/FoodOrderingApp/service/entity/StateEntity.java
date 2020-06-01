package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "state")
@NamedQueries(
        {
                @NamedQuery(name = "StateEntity.getAllStates", query = "select s from StateEntity s order by s.stateName asc"),
                @NamedQuery(name = "StateEntity.findStateByUUID", query = "select s from StateEntity s where s.uuid=:uuid")
        }
)
public class StateEntity implements Serializable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "UUID")
    @NotNull
    @Size(max = 200)
    private String uuid;

    @Column(name = "STATE_NAME")
    @Size(max = 30)
    private String stateName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public StateEntity(String stateUuid, String stateName) {
        this.uuid = stateUuid;
        this.stateName = stateName;
        return;
    }

    public StateEntity() {

    }
}

