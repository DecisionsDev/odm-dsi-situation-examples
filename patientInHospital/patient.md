## Patient staying in hospital example

The goal of this example is to show a basic usage of a business situation.

The business situation is initiated when a patient is admitted to a hospital and ends with the release of the same patient from the hospital.
This very simple example will show how to create a situation in the Business Model Definition, how to identify the situation in a rule, and how to end it.
It also show to notify on a regular basis while a situation is ongoing.


The projects for the example solution can be found in the folder **[patientInHospital](../patientInHospital)**.

1.   Define the  business situation

```BMD
a patient staying in hospital is a business situation.
```

2.   Add a rule to initialize and identify the business situation.
When a business situation is identified, it immediately has the `init` state.
If the beginning date is before or the same as `now`, then the business situation goes into the `beginning` state. 
The `patient staying in hospital` situation does not have an end date. Setting the beginning date is mandatory. 

```BERL
when a patient admission occurs
  if there is no patient staying in hospital
  then
    identify a new patient staying in hospital
    where the beginning date is now;
```

3.   End the business situation. 
In this simple example, the business situation ends when `a patient release` event is received. 
The end date of the situation is set at the patient release time, so it could used in a rule that relies on ending situation.

```BERL
when a patient release occurs
definitions
  set 'situation' to a patient staying in hospital;
then
  set the end date of situation to the timestamp of this patient release;
  end situation;
```

4.   Send a notification each day the patient remains in hospital by testing the current time against the time when the situation began. 
The following rule sends a notification each day at the time that the patient is admitted instead of relying on a constant time like midday.
The rule avoids sending a notification when the patient is admitted by checking  that the current time is after the day of the admission.

```BERL
definitions
  set situation to a patient staying in hospital
    where this patient staying in hospital is ongoing ;
if now is after the beginning date of situation
and the time of day of now is the time of day of the beginning date of situation
then
  define 'duration' as the duration between the beginning date of situation and now;
  emit a new daily notification where
    the ssn is the ssn of 'the patient';
  print "patient " + the name of 'the patient' + " in hospital for " + 'duration';
```

## Testing

As situations are not directly visible through the TestDriver, the test can only be based on the entity state or emitted events. Here, the daily notifications are the only outgoing events. The tests focus on the number of outgoing events.


[![Next icon](../images/forward_32.png) **Next**](../firedetection2/firedetection.md)

[![Back to table of contents icon](../images/home_32.png) **Back to table of contents**](../README.md)



