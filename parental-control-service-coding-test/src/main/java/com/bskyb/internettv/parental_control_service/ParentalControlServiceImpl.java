package com.bskyb.internettv.parental_control_service;

import com.bskyb.internettv.thirdparty.MovieService;
import com.bskyb.internettv.thirdparty.TechnicalFailureException;
import com.bskyb.internettv.thirdparty.TitleNotFoundException;

import java.util.Arrays;
import java.util.List;

/**
 * The service communicates with the Movie Meta Data service to determine whether a movie is suitable for the customer.
 */
public class ParentalControlServiceImpl implements ParentalControlService {

    /** List of supported parental control levels from the least restrictive to the most restrictive. */
    private static final List<String> PARENTAL_CONTROL_LEVELS = Arrays.asList("U", "PG", "12", "15", "18");

    /** Access to movie meta data service. */
    private MovieService movieService;

    
    
    
    public ParentalControlServiceImpl(MovieService movieService) {
		super();
		this.movieService = movieService;
    }

	/**
     * Determine whether a movie is suitable for the customer based on their parental control level setting.
     *
     * @param customerParentalControlLevel Customer parental control level setting
     * @param movieId Id of the movie
     * @return True if the customer has sufficient parental control level for the movie, false otherwise
     * @throws InvalidParentalControlLevelException
     * @throws TitleNotFoundException
     * @throws TechnicalFailureException
     */
    public boolean canWatchMovie(String customerParentalControlLevel, String movieId)
            throws InvalidParentalControlLevelException, TitleNotFoundException, TechnicalFailureException {

        // check the index of customer parental control level
        int customerPCLIndex = PARENTAL_CONTROL_LEVELS.indexOf(customerParentalControlLevel);
        if (customerPCLIndex == -1) {
            throw new InvalidParentalControlLevelException(customerParentalControlLevel);
        }

        // check the index of minimal parental control level required by the movie
        String movieParentalControlLevel = movieService.getParentalControlLevel(movieId);
        int moviePCLIndex = PARENTAL_CONTROL_LEVELS.indexOf(movieParentalControlLevel);
        if (moviePCLIndex == -1) {
            throw new InvalidParentalControlLevelException(movieParentalControlLevel);
        }

        // compare the indexes --
        // the movie parental control level should be equal to or less than
        // the customer's preference parental control level
        return moviePCLIndex <= customerPCLIndex;
    }
}
