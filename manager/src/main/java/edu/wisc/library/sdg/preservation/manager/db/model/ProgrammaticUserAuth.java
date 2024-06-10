package edu.wisc.library.sdg.preservation.manager.db.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("programmatic_user_auth")
public class ProgrammaticUserAuth {

    @Id
    @Column("username")
    private String username;

    @Column("password")
    private String password;

    @Column("created_timestamp")
    private LocalDateTime createdTimestamp;

    @Column("updated_timestamp")
    private LocalDateTime updatedTimestamp;

    public ProgrammaticUserAuth() {
    }

    @PersistenceCreator
    public ProgrammaticUserAuth(String username, String password, LocalDateTime createdTimestamp, LocalDateTime updatedTimestamp) {
        this.username = username;
        this.password = password;
        this.createdTimestamp = createdTimestamp;
        this.updatedTimestamp = updatedTimestamp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(LocalDateTime createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public LocalDateTime getUpdatedTimestamp() {
        return updatedTimestamp;
    }

    public void setUpdatedTimestamp(LocalDateTime updatedTimestamp) {
        this.updatedTimestamp = updatedTimestamp;
    }

    @Override
    public String toString() {
        return "ProgrammaticUserAuth{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", createdTimestamp=" + createdTimestamp +
                ", updatedTimestamp=" + updatedTimestamp +
                '}';
    }

}
