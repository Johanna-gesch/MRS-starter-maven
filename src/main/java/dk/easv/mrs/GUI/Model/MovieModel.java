package dk.easv.mrs.GUI.Model;

//project imports
import dk.easv.mrs.BE.Movie;
import dk.easv.mrs.BLL.MovieManager;

//javaFX imports
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

//java imports
import java.util.List;

public class MovieModel {

    private ObservableList<Movie> moviesToBeViewed;

    private MovieManager movieManager;

    public MovieModel() throws Exception {
        movieManager = new MovieManager();
        moviesToBeViewed = FXCollections.observableArrayList();
        moviesToBeViewed.addAll(movieManager.getAllMovies());
    }

    public ObservableList<Movie> getObservableMovies() {
        return moviesToBeViewed;
    }

    public void searchMovie(String query) throws Exception {
        List<Movie> searchResults = movieManager.searchMovies(query);
        moviesToBeViewed.clear();
        moviesToBeViewed.addAll(searchResults);
    }

    public Movie createMovie(Movie newMovie) throws Exception {
        Movie movieCreated = movieManager.createMovie(newMovie);
        moviesToBeViewed.add(movieCreated);
        return movieCreated;
    }

    public void updateMovie(Movie movieToBeUpdated) throws Exception {
        movieManager.updateMovie(movieToBeUpdated);

        int index = moviesToBeViewed.indexOf(movieToBeUpdated);
        moviesToBeViewed.set(index, movieToBeUpdated);
    }

    public void deleteMovie(Movie selectedMovie) throws Exception {
        movieManager.deleteMovie(selectedMovie);
        moviesToBeViewed.remove(selectedMovie);
    }
}
