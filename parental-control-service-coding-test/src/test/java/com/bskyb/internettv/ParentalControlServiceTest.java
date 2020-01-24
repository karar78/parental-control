package com.bskyb.internettv;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import com.bskyb.internettv.parental_control_service.InvalidParentalControlLevelException;
import com.bskyb.internettv.parental_control_service.ParentalControlServiceImpl;
import com.bskyb.internettv.thirdparty.MovieService;
import com.bskyb.internettv.thirdparty.TechnicalFailureException;
import com.bskyb.internettv.thirdparty.TitleNotFoundException;

public class ParentalControlServiceTest {

	@Mock
	private MovieService movieService;

	private ParentalControlServiceImpl parentalControlServiceImpl;

	@BeforeEach
	void setup() {
		initMocks(this);
		parentalControlServiceImpl = new ParentalControlServiceImpl(movieService);
	}

	@Test
	void testCanWatchMovie()
			throws TitleNotFoundException, TechnicalFailureException, InvalidParentalControlLevelException {

		// Mocking MovieService with custom values for testing.
		when(movieService.getParentalControlLevel("adultMovie")).thenReturn("18");

		// This must return false as MovieService return 18 for this movie whereas
		// client parental control is set to PG
		assertFalse(parentalControlServiceImpl.canWatchMovie("PG", "adultMovie"));

		// This must return true as MovieService return 18 for this movie which is
		// similar level to client parental control which is 18
		assertTrue(parentalControlServiceImpl.canWatchMovie("18", "adultMovie"));
	}

	@Test
	void testCanWatchMovie_ThrowsInvalidParentalControlLevelException() throws Exception {
		// Mocking MovieService with custom values for testing.
		when(movieService.getParentalControlLevel("Tom & Jerry")).thenReturn("U");
		Assertions.assertThrows(InvalidParentalControlLevelException.class,
				() -> parentalControlServiceImpl.canWatchMovie("60", "Tom & Jerry"));
	}

}
