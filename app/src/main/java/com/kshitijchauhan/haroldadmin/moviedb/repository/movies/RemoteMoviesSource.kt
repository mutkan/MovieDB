package com.kshitijchauhan.haroldadmin.moviedb.repository.movies

import com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.service.account.*
import com.kshitijchauhan.haroldadmin.moviedb.repository.data.remote.service.movie.MovieService
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.toActor
import com.kshitijchauhan.haroldadmin.moviedb.utils.extensions.toMovie
import io.reactivex.Single

class RemoteMoviesSource(
    private val movieService: MovieService,
    private val accountService: AccountService
) {

    fun getMovieDetails(id: Int): Single<Movie> {
        return movieService.getMovieDetails(id)
            .map { movieResponse ->
                movieResponse.toMovie()
            }
    }

    fun getMovieAccountStates(movieId: Int): Single<AccountState> {
        return movieService.getAccountStatesForMovie(movieId)
            .map { movieStatesResponse ->
                AccountState(
                    isWatchlisted = movieStatesResponse.isWatchlisted ?: false,
                    isFavourited = movieStatesResponse.isFavourited ?: false,
                    movieId = movieId
                )
            }
    }

    fun getMovieCast(movieId: Int): Single<Cast> {
        return movieService.getCreditsForMovie(movieId)
            .map { creditsResponse ->
                Cast(
                    castMembersIds = creditsResponse.cast.map { castMember -> castMember.id },
                    movieId = movieId)
                    .apply {
                        castMembers = creditsResponse.cast.map { castMember -> castMember.toActor() }
                    }
            }
    }

    fun toggleMovieFavouriteStatus(
        isFavourite: Boolean,
        movieId: Int,
        accountId: Int
    ): Single<ToggleFavouriteResponse> {
        return ToggleMediaFavouriteStatusRequest(
            MediaTypes.MOVIE.mediaName,
            movieId,
            isFavourite
        ).let { request ->
            accountService.toggleMediaFavouriteStatus(accountId, request)
        }
    }

    fun toggleMovieWatchlistStatus(
        isWatchlisted: Boolean,
        movieId: Int,
        accountId: Int
    ): Single<ToggleWatchlistResponse> {
        return ToggleMediaWatchlistStatusRequest(
            MediaTypes.MOVIE.mediaName,
            movieId,
            isWatchlisted
        ).let { request ->
            accountService.toggleMediaWatchlistStatus(accountId, request)
        }
    }
}