MyTwitterApp
============

A simple twitter client for Android. Integrates with Twitter Rest API and OAuth for login.

# User Stories:

* User can sign in using OAuth login flow
* User can view last 25 tweets from their home timeline
* User should be able to see the user, body and timestamp for tweet
* User can compose a new tweet
* User can click a “Compose” icon in the Action Bar on the top right
* User will have a Compose view opened
* User can enter a message and hit a button to Post
* User should be taken back to home timeline with new tweet visible
* User can load more tweets once they reach the bottom
  - Endless scroll
* User can open the twitter app offline and see recent tweets
  - Tweets are persisted into sqlite and displayed from the local DB
* User can pull down the list to refresh.
  - Uses <https://github.com/erikwt/PullToRefresh-ListView> 
