package movies.controller;

import java.util.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import movies.models.Movie;
import movies.utils.IdGenerator;

@RestController
@RequestMapping("/api")
public class MoviesController {
	//GET api/movies -> return all movies 
	List<Movie> movies;
	IdGenerator idGenerator;
	public MoviesController() {
		this.movies = new ArrayList<Movie>();
		this.idGenerator = new IdGenerator();
		int count = 10;
		
		for(int i = 0;i<count;i++) {
			Movie movie = new Movie();
			movie.setId(idGenerator.getNextId());
			movie.setTitle("Movie #:" + (i + 1));
			movie.setDescription("Description for movie: " + (i + 1));
			movie.setRating(1 +(i%5));
			movie.setVotesCount(5);
			this.movies.add(movie);
		}
	}
	@RequestMapping("/movies")
	public List<Movie> getAll() {
		return this.movies;
	}
	//POST /api/movies -> create new movie
	@RequestMapping(value="/movies", method=RequestMethod.POST, produces="application/json")
	public Movie addMovie(@RequestBody Movie newMovie) {
		this.movies.add(newMovie);
		return newMovie;
	}
	//GET api/movies/MOVIE_ID -> detailed info about a movie
	@RequestMapping(value="/movies/{id}")
	public Movie getById(@PathVariable(value="id") int id) throws NoSuchRequestHandlingMethodException {
		for(Movie movie: this.movies) {
			if(movie.getId()== id) {
				return movie;
			}
		}
		throw new NoSuchRequestHandlingMethodException("Such movie not found", Movie.class);	
	}
	//PUT /api/movies/MOVIE_ID -> vote for movie
	@RequestMapping(value = "/movies/{movieId}", method = RequestMethod.PUT)
	public Movie voteForMovie(@PathVariable(value="movieId") int id,
			@RequestBody int rating) throws NoSuchRequestHandlingMethodException {
		Movie movie = this.getById(id);
		double oldRatingsSum = movie.getRating() * movie.getVotesCount();
		
		int newVotesCount = movie.getVotesCount() + 1;
		
		double newRating = (oldRatingsSum + rating)/newVotesCount;
		
		movie.setRating(newRating);
		movie.setVotesCount(movie.getVotesCount() + 1);
		return movie;
	}
	@RequestMapping(value = "/movies/{movieId}", method = RequestMethod.DELETE)
	public boolean deleteMovieById(@PathVariable(value="movieId") int id) throws NoSuchRequestHandlingMethodException {
		int index = -1;
		for(int i = 0; i<this.movies.size();i++) {
			Movie movie = this.getById(i+1);
			if(movie.getId() == id) {
				index = i;
				break;
			}	
		}
		if(index < 0) {
			throw new NoSuchRequestHandlingMethodException("Not  found such movie to delete", Movie.class);
		}
		this.movies.remove(index);
		return true;
	}
	
}
