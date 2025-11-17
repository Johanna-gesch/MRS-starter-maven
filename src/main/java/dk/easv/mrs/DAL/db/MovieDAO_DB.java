package dk.easv.mrs.DAL.db;

import dk.easv.mrs.BE.Movie;
import dk.easv.mrs.DAL.IMovieDataAccess;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MovieDAO_DB implements IMovieDataAccess {

    private DBConnector databaseConnector;

    public MovieDAO_DB() throws IOException {
        databaseConnector = new DBConnector();
    }

    @Override
    public List<Movie> getAllMovies() throws SQLException {
        //Create return data structure:
        ArrayList<Movie> allMovies = new ArrayList<>();

        //Create a connection:
        try(Connection connection = databaseConnector.getConnection()) {

            //Create SQL command:
            String sql = "SELECT * FROM Movie;";

            //Create some kind of statement:
            Statement statement = connection.createStatement();

            //Do relevant treatment of statement:
            if(statement.execute(sql)) {
                ResultSet resultSet= statement.getResultSet();
                while(resultSet.next()) {
                    int id = resultSet.getInt("id");
                    int year = resultSet.getInt("year");
                    String title = resultSet.getString("title");

                    Movie movie = new Movie(id,year,title);
                    allMovies.add(movie);
                }
            }
        }
        return allMovies;
    }

    @Override
    public Movie createMovie(Movie newMovie) throws Exception {
        return null;
    }

    @Override
    public void updateMovie(Movie movie) throws Exception {

    }

    @Override
    public void deleteMovie(Movie movie) throws Exception {

    }

}
