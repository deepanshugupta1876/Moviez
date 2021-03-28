package deepanshu.example.com.moviez.core.network


class ApiResponse<T> private constructor(val status: Status, val data: T?, val message: String) {
    companion object {
        fun <T> success(data: T): ApiResponse<T> {
            return ApiResponse(Status.SUCCESS, data, "success")
        }

        fun <T> error(message: String, data: T?): ApiResponse<T?> {
            return ApiResponse(Status.ERROR, data, message)
        }

        fun <T> loading(data: T?): ApiResponse<T?> {
            return ApiResponse(Status.LOADING, data, "Loading")
        }
    }
}