package movies.services;

import java.util.ArrayList;
import java.util.List;

import javax.xml.crypto.NoSuchMechanismException;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import movies.models.Movie;
import movies.utils.DataStorage;
import movies.utils.IdGenerator;
@Service
public class InMemoryMoviesService implements IMovieService {
	private IdGenerator idGenerator;
	public InMemoryMoviesService() {
		this.movies = new ArrayList<Movie>();
		this.idGenerator = new IdGenerator();
	}
	
	public List<Movie> getAll() {
		return DataStorage.movies;
	}
	
	public Movie add(String title, String description) {
		Movie movie = new Movie();
		movie.setId(idGenerator.getNextId());
		movie.setRating(1);
		movie.setVotesCount(0);
		movie.setTitle(title);
		movie.setDescription(description);
		DataStorage.movies.add(movie);
		return movie;	
	}

	public Movie movieById(int id) throws NoSuchRequestHandlingMethodException {
		Movie movie = null;
		for(Movie currentMovie: DataStorage.movies) {
			if(currentMovie.getId() == id) {
				movie = currentMovie;
			}
		}
		if(movie == null) {
			throw new NoSuchRequestHandlingMethodException("Not  found such movie to delete", Movie.class);
		}
		return movie;
	}

	public Movie vote(int id, int rating) throws NoSuchRequestHandlingMethodException {
		Movie movie = this.movieById(id);
		double oldRatingsSum = movie.getRating() * movie.getVotesCount();
		
		int newVotesCount = movie.getVotesCount() + 1;
		
		double newRating = (oldRatingsSum + rating)/newVotesCount;
		
		movie.setRating(newRating);
		movie.setVotesCount(movie.getVotesCount() + 1);
		return movie;
		
	}

	public boolean deleteMovie(int id) throws NoSuchRequestHandlingMethodException {
		int index = -1;
		for(int i = 0; i<DataStorage.movies.size();i++) {
			Movie movie = this.movieById(i+1);
			if(movie.getId() == id) {
				index = i;
				break;
			}	
		}
		if(index < 0) {
			throw new NoSuchRequestHandlingMethodException("Not  found such movie to delete", Movie.class);
		}
		DataStorage.movies.remove(index);
		return true;
	}
	
}
