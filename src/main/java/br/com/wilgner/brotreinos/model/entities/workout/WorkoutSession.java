package br.com.wilgner.brotreinos.model.entities.workout;

import br.com.wilgner.brotreinos.model.entities.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class WorkoutSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotNull
    private LocalDate workoutDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "workoutSession", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Execution> executions = new ArrayList<>();

    public WorkoutSession() {
    }

    public WorkoutSession(String name, LocalDate workoutDate, List<Execution> executions) {
        this.name = name;
        this.workoutDate = workoutDate;
        this.executions = new ArrayList<>();
        executions.forEach(this::addExecution); // agora usa o parâmetro
    }

    public WorkoutSession(String name, LocalDate workoutDate, User user, List<Execution> executions) {
        this.name = name;
        this.workoutDate = workoutDate;
        this.user = user;
        this.executions = new ArrayList<>();
        executions.forEach(this::addExecution);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getWorkoutDate() {
        return workoutDate;
    }

    public void setWorkoutDate(LocalDate workoutDate) {
        this.workoutDate = workoutDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Execution> getExecutions() {
        return executions;
    }

    public void setExecutions(List<Execution> executions) {
        this.executions.clear();
        if (executions != null) {
            executions.forEach(this::addExecution);
        }
    }

    // setar execution no workout
    public void addExecution(Execution execution) {
        execution.setWorkoutSession(this);
        this.executions.add(execution);
    }



    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        WorkoutSession workoutSession = (WorkoutSession) o;
        return Objects.equals(id, workoutSession.id);
    }

    @Override
    public int hashCode(){
        return Objects.hash(id);
    }
}