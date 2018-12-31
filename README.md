# Popular movies
Udacity popular movies project

## Project Overview

In this project, I completed the Udacity **popular movies** project.

## Stage 2 (NEW)
In this project, I completed the Udacity **popular movies 2** project.

### What i did ?
**User Interface Function**:

- In the movies detail screen, a user can tap a button (for example, a star) to mark it as a Favorite. Tap the button on a favorite movie will unfavorite it.

**Network API Implementation**:

- In a background thread, app queries the /movie/popular or /movie/top_rated API for the sort criteria specified in the settings menu.
- App requests for related videos for a selected movie via the /movie/{id}/videos endpoint in a background thread and displays those details when the user selects a movie.
- App requests for user reviews for a selected movie via the /movie/{id}/reviews endpoint in a background thread and displays those details when the user selects a movie.

**Data Persistence**:

- The titles and IDs of the user’s favorite movies are stored in a native SQLite database using **Room**
- Data is updated whenever the user favorites or unfavorites a movie. No other persistence libraries are used.
- When the "favorites" setting option is selected, the main view displays the entire favorites collection based on movie ids stored in the database.

**Android Architecture Components**: 

- If Room is used, database is not re-queried unnecessarily. LiveData is used to observe changes in the database and update the UI accordingly.
- If Room is used, database is not re-queried unnecessarily after rotation. Cached LiveData from ViewModel is used instead.

### Run
To run the project you need to set a valid `API_KEY`. So go to `app/src/main/java/com/wolasoft/popularmovies/data/api/ApiConnector.java`
file (`line 19`) and change the `API_KEY` variable value with yours.

### Resource
Thanks to

- https://developer.android.com/topic/libraries/architecture/adding-components
- https://developer.android.com/jetpack/docs/guide
- https://stackoverflow.com/questions/574195/android-youtube-app-play-video-intent

## Stage 1
In this project, I completed the Udacity **popular movies 1** project.

### What i did ?
- App is written solely in the Java Programming Language.
- Movies are displayed in the main layout via a grid of their corresponding movie poster thumbnails.
- UI contains a settings menu to toggle the sort order of the movies by: most popular, highest rated.
- UI contains a screen for displaying the details for a selected movie.
- Movie details layout contains title, release date, movie poster, vote average, and plot synopsis.
- App utilizes stable release versions of all libraries, Gradle, and Android Studio.

### Run
To run the project you need to set a valid `API_KEY`. So go to `app/src/main/java/com/wolasoft/popularmovies/utils/HttpUtils.java`
file (`line 17`) and change the `API_KEY` variable value with yours.