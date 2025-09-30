package br.com.wilgner.brotreinos.model.entities.workout;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Execution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String exerciseName;

    @Column
    private String comment;

    @ManyToOne
    @JoinColumn(name = "workout_session_id")
    private WorkoutSession workoutSession;

    @OneToMany(mappedBy = "execution", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Serie> series = new ArrayList<>();

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public WorkoutSession getWorkoutSession() {
        return workoutSession;
    }

    public void setWorkoutSession(WorkoutSession workoutSession) {
        this.workoutSession = workoutSession;
    }

    public List<Serie> getSeries() {
        return series;
    }

    public void setSeries(List<Serie> series) {
        this.series.clear();
        if (series != null) {
            series.forEach(this::addSerie);
        }
    }

    public void addSerie(Serie serie) {
        serie.setExecution(this);
        this.series.add(serie);
    }

    public Execution() {
    }

    public Execution(String exerciseName, String comment,WorkoutSession workoutSession, List<Serie> series) {
        this.exerciseName = exerciseName;
        this.comment = comment;
        this.workoutSession = workoutSession;
        this.series = new ArrayList<>();
        series.forEach(this::addSerie);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Execution that = (Execution) o;

        if (id == null || that.id == null) return false;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

}
