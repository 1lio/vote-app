package utils

object AppConstants  {

    @Suppress("AuthLeak")
    //const val DB_HOST = "mongodb+srv://admin:1234@cluster0.ohhsh.mongodb.net/test"
    const val DB_HOST = "mongodb+srv://admin:1234@cluster0.ohhsh.mongodb.net/test?retryWrites=true&w=majority"
    const val SECRET_KEY = "samplesecretkey"
}