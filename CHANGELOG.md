### version 2.1.2 (TBD)

### version 2.1.1 (June 3rd, 2019)
* Fixed issue #33: fixed NPE in non Rx adapter when a controller returns null (non updated feature)

### version 2.1.0 (April 26th, 2019)
* Better preservation of internal back pressure strategy

### version 2.0.0 (March 5th, 2019)
* migrate to Android X (no longer supports android support library)
* upgrade to RxJava2

### version 1.0.13 (August 16th, 2018)
move the view item check for issue #12 into FeatureAdapter class

### version 1.0.12 (August 7th, 2018)

* add a check in case a view item is associated to a non registered view type delegate:
https://github.com/groupon/FeatureAdapter/issues/12


### version 1.0.11 (April 13th, 2018)

* initial release
* solves issue #10: make animator controller thread safe.

### version 1.0.10 (29 Jan 2018)

* fix an issue when the downstream is consuming the objects to fast and it doesn't wait for the result

### version 1.0.9 (25 Jan 2018)

* limit the number of updates in rx feature adapter. Take only the latest object in a window of time provided by the feature controllers processing time.

### version 1.0.8 (23 Jan 2018)

* fix multiple updates in the model. It was creating too many updates indeed.

### version 1.0.7 (23 Jan 2018)

* fix multiple updates in the model
* update the view item cache size so that all views are in the cache and they ain't recycled.

### version 1.0.6 (Dec. 14th 2017)

* Fix view type init for group adapter: issues #43

### version 1.0.5 (Dec. 7th 2017)

* Added GroupAdapterViewTypeDelegate module: issue #41

### version 1.0.4 (Dec. 6th, 2017)

* Create a method in FeatureAdapter to get AdapterViewTypeDelegate for a certain ViewType: issue #39

### version 1.0.3 (Nov. 29th, 2017)

* The adapter needs a method to return a view type delegate for a viewHolder object: issue #35
* Create a way to add animators + item decorators for features: #34

### version 1.0.2 (Nov. 27th, 2017)

* Make the sample more green beautiful: issue #13
* Add an error handler to feature adapter instances to catch all throwables: #30
* getFirstItemPositionForType should use a view type delegate not a int for the viewtype: #31

### version 1.0.1 (Oct. 30th, 2017)

* Better comparator API: issue #25
* modernize the build system: #26
* payloads: #24
* fix parallax bug: #22

### version 1.0.0 (August 8th, 2017)

* initial release: 1.0.0
