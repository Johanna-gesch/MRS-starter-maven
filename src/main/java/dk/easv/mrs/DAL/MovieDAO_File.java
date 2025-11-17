package dk.easv.mrs.DAL;

//project imports
import dk.easv.mrs.BE.Movie;

//java imports
import java.io.*;
import java.nio.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

//java.nio imports
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.nio.file.StandardOpenOption.*;

public class MovieDAO_File implements IMovieDataAccess {

    private static final String MOVIES_FILE = "data/movie_titles.txt";
    private final Path filePath = Path.of(MOVIES_FILE);

    //The @Override annotation is not required, but is recommended for readability
    // and to force the compiler to check and generate error msg. if needed etc.
    //@Override
    public List<Movie> getAllMovies() throws IOException {
        //Read all lines from file
        List<String> lines = Files.readAllLines(filePath);
        //Instantiate new list of movies
        List<Movie> movies = new ArrayList<>();

        //add each line as a movie to the new list of movies
        for (String line : lines) {
            String[] seperatedLine = line.split(","); //Laver linjen om til et array af strings for hvert komma.
            //hver linje er nu et array med flg. pladser:
            //seperatedLine[0] = linjenr
            //seperatedLine[1] = årstal
            //seperatedLine[2] = titelnavn
            int id = Integer.parseInt(seperatedLine[0]); //instantierer den første streng i linje-arrayet, og laver den om til int.
            int year = Integer.parseInt(seperatedLine[1]); //instantierer den anden streng i linje-arrayet, og laver den om til int.
            String title = seperatedLine[2]; //instantierer den tredje streng i linje-arrayet.

            if(seperatedLine.length > 3) { //hvis linje-arrayet er OVER 3 objekter lang (der er mere end bare id, årstal og titel.
                //vi starter ved den fjerde plads i arrayet og fortsætter så længe vi er på en plads i arrayet.
                for(int i = 3; i < seperatedLine.length; i++) {
                    title += "," + seperatedLine[i]; //vi fortsætter fra den sidste plads i linje-arrayet og tilføjer komma plus hvad end objekt der ellers er i linjen. Vi tilføjer det til title variablen.
                }
            }

            Movie movie = new Movie(id, year, title); //vi instantierer en ny film med de parametre som vi instantierede før (id, year, title).
            movies.add(movie); //vi tilføjer filmen til den nye liste af film vi oprettede i starten.
        }
        return movies;
    }

    @Override
    public Movie createMovie(Movie newMovie) throws Exception {
        List<String> movies = Files.readAllLines(filePath);

        if (movies.size() > 0) {
            //get next id
            String[] seperatedLine = movies.get(movies.size() - 1).split(","); //gå til sidste objekt i movies listen, og split den i et array af stings for hvert komma.
            int nextID = Integer.parseInt(seperatedLine[0]) + 1;

            //add new movie
            String newMovieLine = nextID + "," + newMovie.getYear() + "," + newMovie.getTitle();
            Files.write(filePath, (newMovieLine + "\r\n").getBytes(), APPEND);

            return new Movie(nextID, newMovie.getYear(), newMovie.getTitle());
        } /*else if (movies.size() == 0) {
            return new Movie(1, year, title); //er ikke så godt hvis 2 trykker create new på samme tid - så får 2 film det samme id... virker bedre med databaser.
        }*/
        return null;
    }

    @Override
    public void updateMovie(Movie movie) throws Exception {
        try
        {
            // Read all movie lines in list
            List<String> movies = Files.readAllLines(filePath);

            // Iterate through all lines and look for the right one (movie)
            for (int i = 0; i < movies.size(); i++)
            {
                // Split each line into atomic parts
                String[] separatedLine = movies.get(i).split(",");

                // Make sure we have a valid movie with all parts
                if(separatedLine.length == 3) {
                    // individual movie items
                    int id = Integer.parseInt(separatedLine[0]);

                    // Check if the id is equal to movie.getId()
                    if (id == movie.getId()) {
                        String updatedMovieLine = movie.getId() + "," + movie.getYear() + "," + movie.getTitle();
                        movies.set(i, updatedMovieLine);
                        break;
                    }
                }
            }

            // Create new temp file
            Path tempPathFile = Paths.get(MOVIES_FILE + "_TEMP");
            Files.createFile(tempPathFile);

            // For all lines...
            for (String line: movies)
                Files.write(tempPathFile, (line + "\r\n").getBytes(),APPEND);

            // Overwrite the old file with temp file
            Files.copy(tempPathFile, filePath, REPLACE_EXISTING);
            Files.deleteIfExists(tempPathFile);

        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("An error occurred");
        }
    }

    @Override
    public void deleteMovie(Movie movie) throws Exception {
        try
        {
            // Read all movie lines in list
            List<String> movies = Files.readAllLines(filePath);
            movies.remove(movie.getId() + "," + movie.getYear() + "," + movie.getTitle());


            // Create new temp file
            Path tempPathFile = Paths.get(MOVIES_FILE+ "_TEMP");
            Files.createFile(tempPathFile);

            // For all lines...
            for (String line: movies)
                Files.write(tempPathFile, (line + "\r\n").getBytes(),APPEND);

            // Overwrite the old file with temp file
            Files.copy(tempPathFile, filePath, REPLACE_EXISTING);
            Files.deleteIfExists(tempPathFile);


        } catch (IOException e) {
            throw new Exception("An error occurred");
        }

    }
}