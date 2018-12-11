# 2.4 How to update/end/abort business situations

To end a business situation, add a rule to process the event to end it. You need to end a business situation only if you have actions to take as a result of it ending.

In the following example, the patient release event is used to end the `patient staying in hospital` business situation.

```
when a patient release occurs 
definitions
  set 'situation' to a patient staying in hospital ;
then 
  end situation ;
```

You might want to aggregate something in the business situation, or change the end date.

To handle cancellations, you can also use `abort` keyword to force an irregular end to a business situation.

```
when a stop sensor event occurs 
definitions 
  set 'detection' to a missing sensor event detection   
    where this missing sensor event detection is ongoing ;
then
  abort detection ; 
```

These examples are too simple to really show the benefit of business situations. However, the business situations can easily be upgraded by doing one of the following things.

-   Adding state to the situation \(for example, storing the patient room number\).
-   Sending a notification in a rule at the beginning/ongoing/ending of the business situation with data such as the hospital address.
-   Aggregating data over the duration of the business situation to update an entity. 

[![Next icon](../images/forward_32.png) **Next**](../docs/tsk_dsi_bs_emit.md)

[![Back to table of contents icon](../images/home_32.png) **Back to table of contents**](../README.md)

