# 2.3 How to identify business situations

A business situation can be started immediately when an event occurs, if the business situation is not ongoing.

To use a business situation, add a rule to a rule agent to detect the start of it and to define what action to take.

```
when a sensor event occurs 
if there is no missing sensor event detection 
  where this missing sensor event detection is ongoing,
then
identify a new missing sensor event detection where 
  the beginning date is now,
  the end date is 1 days after now;
```

The default start date is `now`, but it can be in the future, or in even in the past. When the activation event occurs, the business situation begins. If the start date is before or the same as `now`, then the business situation goes into the `beginning` state. The business situation state that can be used in rules are `beginning`, `ongoing`, `ending`, and `aborting`.

The following example shows a business situation where `a patient staying in hospital` begins when `a patient` is admitted to a hospital. The `patient staying in hospital` is time-related and is linked to the entity `patient` by its identification key or social security number in this case \(`a patient is a business entity identified by a ssn`\).

```
when a patient admission occurs 
if there is no patient staying in hospital
  where this patient staying in hospital is ongoing,
then 
  set 'the patient' to a new patient
    where the name is the name of this patient admission ;
  identify a new patient staying in hospital 
    where the beginning date is now;
else 
  print "Received a patient admission for a patient already in hospital";
```

Note, the business situation does not have an end date. An end date is not mandatory.

[ ![Next icon](../images/forward_32.png) **Next**](../docs/tsk_dsi_bs_update.md)

[![Back to table of contents icon](../images/home_32.png) **Back to table of contents**](../README.md)

