# Fire detection using 2 criteria example

In this example, a fire in a room \(the bound entity\) is detected by using two events. If both the presence of smoke and high temperature in a room is detected, then it is likely that a fire is alight in the room.

The example uses two business situations as a way to detect the events in a sliding window.

The projects for the example solution can be found in the folder **[firedetection2](../firedetection2)**.

1.   Define the classes for the business situations. 
One situation corresponds to the fire detection. One situation corresponds to the fire.


```BMD
a fire detection is a business situation.
a fire detection has a smoking (a boolean ).
a fire detection has an high temperature (a boolean).

a fire is a business situation.
```

2.   Add rules to create and identify the business situations. 
The `fire detection` business situation is created when `a smoke` or `a high temperature` event occurs. The `fire detection` business situation has a duration of 1 minute after the initial event that started it. Because redundant detections are not wanted, the `fire detection` is not created when a fire is already `ongoing`. It is also possible to stop the `fire detection` in a separate rule when a fire exists. It is possible that a number of detections are ongoing at the same time, each one representing a sliding window. A sliding window that stops at regular intervals is more costly than setting up a detection that corresponds to a tumbling window \(repeats at non-overlapping intervals\), but at least the detection is certain to arrive in the quickest possible time.


```BERL
when a smoke event occurs
if there is no fire
then identify a new fire detection 
    where the beginning date is this smoke event,
    the end date is 1 minute after this smoke event;
```

```BERL
when a temperature event occurs
    where the temperature of this temperature event is more than 200
if there is no fire
then identify a new fire detection 
    where the beginning date is this temperature event,
    the end date is 1 minute after this temperature event;
```

3.   Add rules to update `the detection` business situations that are ongoing. 

```BERL
when a temperature event occurs
definitions
    set 'the detection' to a fire detection where this fire detection is ongoing ;
if the temperature of this temperature event is more than 200
then
    make it true that 'the detection' is high temperature;
else
    make it false that 'the detection' is high temperature;
```

```BERL
when a smoke event occurs
definitions
    set 'the detection' to a fire detection where this fire detection is ongoing ;
then
    make it true that 'the detection' is smoking;
```

4.   Add rules to end and start the business situations. If one of the detections is both high temperature and has smoke, then a fire is detected. When a fire is detected, all detections are aborted.

```BERL
if there is at least one fire detection where this fire detection is ongoing
    and this fire detection is high temperature and this fire detection is smoking,
then
    identify a new fire 
    where the beginning date is now;
```

```BERL
definitions
    set 'the detection' to a fire detection ;
if there is at least one fire
then
    abort 'the detection';
```

5.   Add a rule to emit events when a room is on fire. 

```BERL
if there is at least one fire where this fire is beginning ,
then
    emit a new fire event 
    where the room number is the number of 'the room';
```

## Test the behavior of the rules

As situations are not directly visible through the TestDriver, the test can only be based on the entity state or emitted events. Here, the only effect of situation detection is to emit the final fire event. Thus tests will be concentrated on the presence or absence of a fire event.

See [test project](./Fire%20detection%202%20criteria%20Test)

[![Next icon](../images/forward_32.png) **Next**](../firedetectionmc/firedetectionmulticriteria.md)

[![Back to table of contents icon](../images/home_32.png) **Back to table of contents**](../README.md)

