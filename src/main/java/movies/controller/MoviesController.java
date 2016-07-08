package movies.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import movies.models.Movie;
import movies.services.InMemoryMoviesService;
import movies.utils.IdGenerator;

@RestController
@RequestMapping("/api")
public class MoviesController {
	public MoviesController() throws Exception {
		this(new InMemoryMoviesService());
	}
	//GET api/movies -> return all movies 
	//List<Movie> movies;
	private InMemoryMoviesService movieService;
	private IdGenerator idGenerator;
	@Autowired
	public MoviesController(InMemoryMoviesService service) throws Exception {
		int count = 20;
		this.movieService = service;
		
		for(int i = 0;i<count;i++) {
			Movie movie = new Movie();
			String title = "Movie #:" + (i + 1);
			String description = "Description for movie: " + (i + 1);
			this.movieService.add(title, description);
		}
	}
	
	@RequestMapping("/movies")
	public List<Movie> getAll() {
		return this.movieService.getAll();
	}
	
	//POST /api/movies -> create new movie
	@RequestMapping(value="/movies", method=RequestMethod.POST, produces="application/json")
	public Movie addMovie(@RequestBody Movie newMovie) {
		this.movieService.add(newMovie.getTitle(), newMovie.getDescription());
		return newMovie;
	}
	//GET api/movies/MOVIE_ID -> detailed info about a movie
	@RequestMapping(value="/movies/{id}")
	public Movie getById(@PathVariable(value="id") int id) throws NoSuchRequestHandlingMethodException {
			return this.movieService.movieById(id);	
	}
	//PUT /api/movies/MOVIE_ID -> vote for movie
	@RequestMapping(value = "/movies/{movieId}", method = RequestMethod.PUT)
	public Movie voteForMovie(@PathVariable(value="movieId") int id,
			@RequestBody int rating) throws NoSuchRequestHandlingMethodException {
		return this.movieService.vote(id, rating);
	}
	@RequestMapping(value = "/movies/{movieId}", method = RequestMethod.DELETE)
	public boolean deleteMovieById(@PathVariable(value="movieId") int id) throws NoSuchRequestHandlingMethodException {
		return this.movieService.deleteMovie(id);
	}
}
