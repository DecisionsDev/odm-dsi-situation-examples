# Fire detection using a number of criteria example

a fire in a room \(the bound entity\) is detected by using different sensors. If three different clues are reported in a room, then it is likely that a fire is alight in the room.

The example uses two business situations as a way to detect the events in a sliding window.

The projects for the example solution can be found in the folder **[firedetectionmc](../firedetectionmc)**.

1.   Define the classes for the business situations. 
One situation corresponds to the fire detection. One situation corresponds to the fire.

```BMD
a criteria can be one of :
  heat, heat increase, flame, smoke, CO, CO2.

a fire detection is a business situation.
a fire detection has several different criterias.
```

2.   Add rules to create and identify the business situations. The `fire detection` business situation is created when event of a number of kinds occurs. ere, there is a distinction between sensors that produce directly a `detected event` and those that produces a value, like a thermometer. The first kind of event is grouped under a common event class named `direct detection event`. The `fire detection` business situation has a duration of 1 minute after the initial event that started it. Because redundant detections are not wanted, the `fire detection` is not created when a fire is already `ongoing`. It is also possible to stop the `fire detection` in a separate rule when a fire exists. It is possible that a number of detections are ongoing at the same time, each one representing a sliding window. A sliding window that stops at regular intervals is more costly than setting up a detection that corresponds to a tumbling window \(repeats at non-overlapping intervals\), but at least the detection is certain to arrive in the quickest possible time.

```BERL
when a direct detection event occurs
if there is no fire 
then identify a new fire detection where
    the beginning date is this direct detection event,
    the end date is 1 minute after this direct detection event;
```

```BERL
when a temperature event occurs
    where the temperature of this temperature event is more than 200
if there is no fire
then identify a new fire detection where
    the beginning date is this temperature event,
    the end date is 1 minute after this temperature event;
```

3.   Add rules to update `the detection` business situations that are ongoing. 
There is one rule per kind of incoming event. The one that deals with temperature is special.
Note that some events, like the carbon monoxyde event carrying the ppm of CO in the air, may store a bit more information about the detection that may be of interest. This information can easily be stored in the fire detection situation if needed.

```BERL
when a temperature event occurs
definitions
    set 'the detection' to a fire detection where this fire detection is ongoing ;
if the temperature of this temperature event is more than 200
then
    add heat to the criterias of 'the detection';
else
    remove heat from the criterias of 'the detection';
```

```BERL
when a smoke event occurs
definitions
    set 'the detection' to a fire detection where this fire detection is ongoing ;
then
    add smoke to the criterias of 'the detection';
```

```BERL
when a carbon monoxyde event occurs
definitions
    set 'the detection' to a fire detection where this fire detection is ongoing ;
then
    add CO to the criterias of 'the detection';
```


4.   Add rules to end and start the business situations. If one of the detections has at least 3 criteria, then a fire is detected. When a fire is detected, all detections are aborted. To pass information from the 'fire detection' situation to the 'fire' situation, there has to be a reference to which detection is used. This is quite different from the 2 criteria sample, where only the existence of such detection was used. As there typically may be several detections at the same time, the fire detection is protected with 'if there is no fire' to avoid detecting several fires at the same time.

```BERL
definitions
    set 'the detection' to a fire detection where this fire detection is ongoing
    and the number of elements in the criterias of this fire detection is at least 3;
if there is no fire
then
    define 'fire' as a new fire where
    the beginning date is now;
    set the criterias of fire to the criterias of 'the detection';
    identify fire ;
```

Once a fire is detected, all detections are aborted

```BERL
definitions
    set 'the detection' to a fire detection ;
if there is at least one fire
then
    abort 'the detection';
```

5.   Add a rule to emit events when a room is on fire. 

```BERL
definitions
    set 'fire' to a fire where this fire is beginning;
then
    define 'fire event' as a new fire event where 
    the room number is the number of 'the room';
    set the criterias of 'fire event' to the criterias of 'fire';
    emit 'fire event';
```

## Testing

As situations are not directly visible through the TestDriver, the test can only be based on the entity state or emitted events. Here, the only effect of situation detection is to emit the final fire event. Thus tests will be concentrated on the presence or absence of a fire event.


[![Next icon](../images/forward_32.png) **Next**](../shippingpackage/shippingpackage.md)

[![Back to table of contents icon](../images/home_32.png) **Back to table of contents**](../README.md)
