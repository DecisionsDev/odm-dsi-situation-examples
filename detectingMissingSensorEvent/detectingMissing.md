## Detecting the absence of events

In this example, a missing sensor event is detected along with a broken sensor business situation.

A sensor sends heartbeats when it records measurements. If the sensor stops sending heartbeats for a day, then it is thought to be broken. It remains broken until it sends a new heartbeat. In addition, the sensor can be stopped, so the detection of missing events must be stopped too.

The projects for the example solution can be found in the folder **[detectingMissingSensorEvent](../detectingMissingSensorEvent)**.


1.   Define the classes for the business situations. 

```BMD
a missing sensor event detection is a business situation.
a broken sensor is a business situation.
```

2.   Add rules to create and identify the business situations. 

```BERL
when a sensor heartbeat occurs
if there is no missing sensor event detection
    where this missing sensor event detection is ongoing,
then
identify a new missing sensor event detection where
    the beginning date is now,
    the end date is 1 days after now;
```

3.   Add a rule to update the `missing sensor event detection` business situation. 
Note the test that checks that the heartbeat happens after the beginning date of the detection removes the risk of processing events that arrive out of order.

```BERL
when a sensor heartbeat occurs
definitions
    set 'detection' to a missing sensor event detection
    where the beginning date of this missing sensor event detection is before this sensor heartbeat;
then
    set the end date of detection to 1 day after now;
```

4.   Add a rule to be notified of the end of the business situation. 
When the end date is equal or after `now`, the business situation state changes to `ending`. The state can be used in a rule to detect that no event occurred:

```BERL
definitions
  set 'detection' to a missing sensor event detection
    where this missing sensor event detection is ending;
then
    emit a new missing detected event
    where the user is 'the user',
    the message is "no sensor event in the last day before " + the end date of detection ;
```

5.   Add a rule to stop the business situation. 
Sometimes a business situation can be `aborted`, which is not a regular ending. In this example, if the sensor is stopped you no longer need to look for future sensor events. The solution also has rules to manage the broken sensor business situation. The broken sensor business situation starts when a `missing sensor event detection` is ending, and it stops either when the sensor is stopped `when a stop sensor event occurs`, or when a heartbeat is received `when a sensor heartbeat occurs`.


```BERL
when a stop sensor event occurs
definitions
  set 'detection' to a missing sensor event detection 
    where this missing sensor event detection is ongoing;
then
    abort detection;
```

6. Add rules to manage the `broken sensor` business situation

There are rules to manage the `broken sensor` business situation.
It starts when a `missing sensor event detection` is ending, and it stops either when the sensor is stopped (when a stop sensor event occurs), or when an heartbeat is received (when a sensor heartbeat occurs).

[![Next icon](../images/forward_32.png) **Next**](../promotion/promotion.md)

[![Back to table of contents icon](../images/home_32.png) **Back to table of contents**](../README.md)



