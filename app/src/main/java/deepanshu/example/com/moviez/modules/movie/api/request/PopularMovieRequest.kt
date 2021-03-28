package deepanshu.example.com.moviez.modules.movie.api.request

class PopularMovieRequest private constructor(builder: Builder) {
    val page: Int

    init {
        page = builder.page
    }

    class Builder {
        var page: Int = 1

        fun setPage(page: Int): Builder {
            this.page = page
            return this
        }

        fun build(): PopularMovieRequest {
            return PopularMovieRequest(this)
        }
    }
}