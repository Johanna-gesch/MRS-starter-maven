package dk.easv.mrs.GUI.Controller;

//project imports
import dk.easv.mrs.BE.Movie;
import dk.easv.mrs.GUI.Model.MovieModel;

//javaFX imports
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;

//java imports
import java.net.URL;
import java.util.ResourceBundle;

public class MovieViewController implements Initializable {

    private MovieModel movieModel;

    @FXML
    private ListView<Movie> lstMovies;

    @FXML
    private TextField txtMovieSearch, txtTitle, txtYear;

    public MovieViewController()  {

        try {
            movieModel = new MovieModel();
        } catch (Exception e) {
            displayError(e);
            e.printStackTrace();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        lstMovies.setItems(movieModel.getObservableMovies());

        lstMovies.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, selectedMovie) ->
        {
            if (selectedMovie != null) {
                txtTitle.setText(selectedMovie.getTitle());
                txtYear.setText("" + selectedMovie.getYear());
            }
        });

        //sætter search field til at være parameter i metoden searchMovie(param) i movieModel
        txtMovieSearch.textProperty().addListener((observableValue, oldValue, newValue) -> {
            try {
                movieModel.searchMovie(newValue);
            } catch (Exception e) {
                displayError(e);
                e.printStackTrace();
            }
        });

    }

    private void displayError(Throwable t)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Something went wrong");
        alert.setHeaderText(t.getMessage());
        alert.showAndWait();
    }

    /**
     * Event handler to add movie written in the related textfields to the list
     * @param actionEvent
     */
    @FXML
    private void onBtnCreateClick(ActionEvent actionEvent) {
        String title = txtTitle.getText();
        int year = Integer.parseInt(txtYear.getText());

        Movie newMovie = new Movie(-1, year, title);

        try {
            movieModel.createMovie(newMovie);
        } catch (Exception e) {
            displayError(e);
        }

        lstMovies.getSelectionModel().select(lstMovies.getItems().size() - 1);
        lstMovies.scrollTo(lstMovies.getItems().size() - 1);
    }

    /**
     * Event handler to handle updating a movie
     * @param actionEvent
     */
    @FXML
    private void onBtnUpdateClick(ActionEvent actionEvent) {
        Movie selectedMovie = lstMovies.getSelectionModel().getSelectedItem();

        if (selectedMovie != null) {
            selectedMovie.setTitle(txtTitle.getText());
            selectedMovie.setYear(Integer.parseInt(txtYear.getText()));
        }

        try {
            movieModel.updateMovie(selectedMovie);
        } catch (Exception e) {
            displayError(e);
        }
    }

    /**
     * Deletes selected movie
     * @param actionEvent
     */
    @FXML
    private void onBtnDeleteClick(ActionEvent actionEvent) {
        Movie selectedMovie = lstMovies.getSelectionModel().getSelectedItem();

        if (selectedMovie != null) {
            try {
                movieModel.deleteMovie(selectedMovie);
            } catch (Exception e) {
                displayError(e);
            }
        }
    }
}
