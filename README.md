# Moviez

**Architecture Followed**
  MVVM

**Libraries Used**
1) Gson - User to parse Json object to data class
2) Dagger2 - Used for dependency injection
3) RxJava - Used to handle background process
4) Picasso - Used to load url images in application
5) Liveadata And ViewModel - Used for data from api response
6) Retrofit - Used to make api calls
7) CircleImageView - Used to crew member profile image

**Decisions**
1) Allowed screen rotation
2) Added network check befor making api call
3) Added observer on network check, so that application can respond on change of network status
4) Used ViewPager2 to implement viewpager feature

**Features**
1) Movie Listing Screen(Launcher Screen)
   * Fetch popular and now playing movies via api calls made concurrently using zipwith() operator of RxJava
   * Populated now playing movies api data into viewpager2 and is attached with circleindicator
   * Populated Popular Movies api data in recyclerview. The recyclerview support pagination.
   * On click of movie item, user is redirected to movie detail screen.
   
2) Movie Detail Screen
   * Fetch movie detail and similar movies data via api calls made concurrently using zipwith() operator of RxJava
   * Populated movie detail data in the ui
   * Populated production companies data into horizontal recyclerview
   * populated similar movies data into horizontal recyclerview
   * On click of similar movie item, user is redirected to movie detail screen of the item.
   * This screen has two buttons(Casts and Reviews) below Production Companies recyclerview
   * On click of "Cast" Button, user is redirected to Movie Cast screen
   * On click of "Review" Button, user is redirected to Movie Review screen
  
3) Movie Cast Screen
   * Fetch movie cast data via api call
   * Populate the movie cast data into grid recyclerview

4) Movie Review Screen
   * Fetch movie review data via api call
   * Populate the movie review data into vertical recyclerview
