package br.com.wilgner.brotreinos.model.entities.workout;

import br.com.wilgner.brotreinos.model.entities.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class WorkoutSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private DayOfWeek dayOfWeek;

    @NotNull
    private LocalDate workoutDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "workoutSession", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Execution> exercicios = new ArrayList<>();

    public WorkoutSession() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
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

    public List<Execution> getExercicios() {
        return exercicios;
    }

    public void setExercicios(List<Execution> exercicios) {
        this.exercicios = exercicios;
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